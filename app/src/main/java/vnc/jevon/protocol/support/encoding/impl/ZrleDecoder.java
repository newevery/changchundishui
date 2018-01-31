package vnc.jevon.protocol.support.encoding.impl;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vnc.jevon.protocol.support.canvas.DefaultDrawable;
import vnc.jevon.protocol.support.canvas.ImageRect;
import vnc.jevon.protocol.support.encoding.ImageDecoder;
import vnc.jevon.protocol.support.io.MemInStream;
import vnc.jevon.protocol.support.io.ZlibInStream;
import vnc.jevon.util.UI;
import global.ConstDefs;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Arrays;

public class ZrleDecoder implements ImageDecoder {

    private static ZrleDecoder decoder;
    private DefaultDrawable drawable;
    private int[] zrleTilePixels;
    private ZlibInStream is;
    private byte[] zrleBuf;
    private int[] zRLERectPalette = new int[128];
    private byte[] readPixelsBuffer = new byte[128];
    private Paint paint = new Paint();
    private boolean isInterrupt = false;
    private static final Logger logger = LoggerFactory.getLogger(ZrleDecoder.class);
    private Thread thread;
    public  boolean destoring = false;

    void init() {
        thread = new Thread() {
            @Override
            public void run() {
                long ms = System.currentTimeMillis();
                while (true && !isInterrupt) {
                    synchronized (drawable.framebuffer) {
                        if(drawable.needrefreash)
                            refreshView(drawable.rect.getRect());
                    }
                    try {
                        long l = 180 - (System.currentTimeMillis() - ms);
                        if (l > 0) {
                            sleep(l);
                        }
                        ms = System.currentTimeMillis();
                    } catch (Exception e) {
                    }
                }
            }
        };
    }

    public static ZrleDecoder getInstance() {
        if (decoder == null) {
            decoder = new ZrleDecoder();
            decoder.init();
        }
        return decoder;
    }

    public ZrleDecoder() {
        drawable = DefaultDrawable.getInstance();
    }

    @Override
    public void decode(DataInputStream dis, ImageRect rect) throws IOException {

        if (this.is == null) {
            is = new ZlibInStream();
        }
        drawable.rect = rect;
        int nBytes = dis.readInt();
        if (nBytes > 64 * 1024 * 1024)
            throw new IOException("ZRLE decoder: illegal compressed data size");

        if (zrleBuf == null || zrleBuf.length < nBytes) {
            clearBuf();
            zrleBuf = new byte[nBytes + 4096];
        }
        dis.readFully(zrleBuf, 0, nBytes);
        is.setUnderlying(new MemInStream(zrleBuf, 0, nBytes), nBytes);

        for (int ty = rect.y; ty < rect.y + rect.height; ty += 64) {
            int th = Math.min(rect.y + rect.height - ty, 64);

            for (int tx = rect.x; tx < rect.x + rect.width; tx += 64) {
                int tw = Math.min(rect.x + rect.width - tx, 64);
                int mode = is.readU8();
                boolean rle = (mode & 128) != 0;
                int paletteSize = mode & 127;
                readPixels(is, zRLERectPalette, paletteSize);

                if (paletteSize == 1) {
                    int pix = zRLERectPalette[0];
                    int c = (UI.ClientInfo.bytesPerPixel == 1) ? UI.ClientInfo.palette[0xFF & pix] : (0xFF000000 | pix);
                    paint.setColor(c);
                    paint.setStyle(Paint.Style.FILL);
                    drawable.drawRect(tx, ty, tw, th, paint);
                    continue;
                }
                if (!rle) {
                    if (paletteSize == 0) {
                        readRawPixels(tw, th);
                    } else {
                        readPackedPixels(tw, th, zRLERectPalette, paletteSize);
                    }
                } else {
                    if (paletteSize == 0) {
                        readPlainRLEPixels(tw, th);
                    } else {
                        readPackedRLEPixels(tw, th, zRLERectPalette);
                    }
                }
                handleUpdatedZrleTile(tx, ty, tw, th);
            }
        }

        is.reset();

        if (!thread.isAlive()) {
            isInterrupt = false;
            thread.start();
        }
    }

    private void handleUpdatedZrleTile(int x, int y, int w, int h) {
        int offsetSrc = 0;
        synchronized (drawable.framebuffer) {
            for (int j = 0; j < h; j++) {
                System.arraycopy(zrleTilePixels, offsetSrc, drawable.framebuffer, drawable.offset(x, (y + j)), w); // 0
                offsetSrc += w;
            }
            drawable.needrefreash = true;
        }

    }

    private void readPackedRLEPixels(int tw, int th, int[] palette) throws IOException {
        int ptr = 0;
        int end = ptr + tw * th;
        if (zrleTilePixels == null || end > zrleTilePixels.length)
            zrleTilePixels = new int[end];
        while (ptr < end) {
            int index = is.readU8();
            int len = 1;
            if ((index & 128) != 0) {
                int b;
                do {
                    b = is.readU8();
                    len += b;
                } while (b == 255);

                if (!(len <= end - ptr))
                    throw new IOException("ZRLE decoder: assertion failed" + " (len <= end - ptr)");
            }

            index &= 127;
            int pix = palette[index];

            if (UI.ClientInfo.bytesPerPixel == 1) {
                while (len-- > 0)
                    zrleTilePixels[ptr++] = UI.ClientInfo.palette[0xFF & pix];
            } else {
                while (len-- > 0)
                    zrleTilePixels[ptr++] = pix;
            }
        }
    }

    private void readPlainRLEPixels(int tw, int th) throws IOException {
        int ptr = 0;
        int end = ptr + tw * th;
        if (zrleTilePixels == null || end > zrleTilePixels.length)
            zrleTilePixels = new int[end];
        while (ptr < end) {
            int pix = readPixel(is);
            int len = 1;
            int b;
            do {
                b = is.readU8();
                len += b;
            } while (b == 255);

            if (!(len <= end - ptr))
                throw new IOException("ZRLE decoder: assertion failed" + " (len <= end-ptr)");

            if (UI.ClientInfo.bytesPerPixel == 1) {
                while (len-- > 0)
                    zrleTilePixels[ptr++] = UI.ClientInfo.palette[0xFF & pix];
            } else {
                while (len-- > 0)
                    zrleTilePixels[ptr++] = pix;
            }
        }
    }

    private int readPixel(ZlibInStream is) throws IOException {
        int pix;
        if (UI.ClientInfo.bytesPerPixel == 1) {
            pix = is.readU8();
        } else {
            int p1 = is.readU8();
            int p2 = is.readU8();
            int p3 = is.readU8();
            pix = (p3 & 0xFF) << 16 | (p2 & 0xFF) << 8 | (p1 & 0xFF);
        }
        return pix;
    }

    private void readPackedPixels(int tw, int th, int[] palette, int paletteSize) throws IOException {
        int bppp = ((paletteSize > 16) ? 8 : ((paletteSize > 4) ? 4 : ((paletteSize > 2) ? 2 : 1)));
        int ptr = 0;
        int len = tw * th;
        if (zrleTilePixels == null || len > zrleTilePixels.length)
            zrleTilePixels = new int[len];

        for (int i = 0; i < th; i++) {
            int eol = ptr + tw;
            int b = 0;
            int nbits = 0;

            while (ptr < eol) {
                if (nbits == 0) {
                    b = is.readU8();
                    nbits = 8;
                }
                nbits -= bppp;
                int index = (b >> nbits) & ((1 << bppp) - 1) & 127;
                if (UI.ClientInfo.bytesPerPixel == 1) {
                    if (index >= UI.ClientInfo.palette.length)
                        logger.error("zrlePlainRLEPixels palette lookup out of bounds " + index + " (0x"
                                + Integer.toHexString(index) + ")");
                    zrleTilePixels[ptr++] = UI.ClientInfo.palette[0xFF & palette[index]];
                } else {
                    zrleTilePixels[ptr++] = palette[index];
                }
            }
        }
    }

    private void readRawPixels(int tw, int th) throws IOException {
        int len = tw * th;
        if (zrleTilePixels == null || len > zrleTilePixels.length)
            zrleTilePixels = new int[len];
        readPixels(is, zrleTilePixels, tw * th);
    }

    /**
     * 读取像素
     *
     * @param is
     * @param size
     * @return
     * @throws IOException
     */
    private void readPixels(ZlibInStream is, int[] pixels, int size) throws IOException {
        if (UI.ClientInfo.bytesPerPixel == 1) {
            if (size > readPixelsBuffer.length) {
                readPixelsBuffer = new byte[size];
            }
            is.readBytes(readPixelsBuffer, 0, size);
            for (int i = 0; i < size; i++) {
                pixels[i] = readPixelsBuffer[i] & 0xFF;
            }
        } else {
            final int l = size * 3;
            if (l > readPixelsBuffer.length) {
                readPixelsBuffer = new byte[l];
            }
            is.readBytes(readPixelsBuffer, 0, l);
            for (int i = 0; i < size; i++) {
                final int idx = i * 3;
                pixels[i] = ((readPixelsBuffer[idx + 2] & 0xFF) << 16 | (readPixelsBuffer[idx + 1] & 0xFF) << 8 | (readPixelsBuffer[idx] & 0xFF));
            }
        }
    }

    private synchronized void refreshView(Rect rect) {
        SurfaceHolder holder = UI.view.imageView.getHolder();
        
        synchronized (drawable.framebuffer) {

            Bitmap bm=Bitmap.createBitmap (drawable.framebuffer, 
                    0, 
                    UI.serverinfo.frameBufferWidth, 
                    UI.serverinfo.frameBufferWidth, 
                    UI.serverinfo.frameBufferHeight,
                    Bitmap.Config.RGB_565); 
        	
            Canvas canvas = holder.lockCanvas();
            if (canvas == null) {
                logger.debug("canvas  is null");
                isInterrupt = true;
                drawable.init();
                is = null;
                clearBuf();
                init();
                return;
            }
            
            canvas.drawBitmap(bm,null,new Rect(0,0,ConstDefs.DisplayWidth,ConstDefs.DisplayHeight), new Paint()); 
            
            drawable.needrefreash = false;
            holder.unlockCanvasAndPost(canvas);
        }
    }

    private void clearBuf() {
        if (zrleBuf != null) {
            Arrays.fill(zrleBuf, (byte) 0);
            zrleBuf = null;
        }
    }

    public void destory() {
        destoring = true;
        if (thread != null && thread.isAlive()) {
            thread.interrupt();
        }
        if (is != null) {
            is = null;
        }
        clearBuf();
        destoring = false;
    }
}

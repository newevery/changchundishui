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
import vnc.jevon.util.UI;
import global.ConstDefs;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Arrays;

public class RawDecoder implements ImageDecoder {
    private static RawDecoder decoder;
    public boolean destoring = false;
    private DefaultDrawable drawable;
    private byte[] buffer = new byte[128];

    private static final Logger logger = LoggerFactory.getLogger(RawDecoder.class);

    public static RawDecoder getInstance() {
        if (decoder == null) {
            decoder = new RawDecoder();
        }
        return decoder;
    }

    public RawDecoder() {
        drawable = DefaultDrawable.getInstance();
    }

    /*
     * (non-Javadoc)
     *
     * @see vnc.jevon.protocol.support.encoding.ImageDecoder#decode(java.io.
     * DataInputStream, vnc.jevon.protocol.support.canvas.ImageRect)
     */
    @Override
    public void decode(DataInputStream is, ImageRect rect) throws IOException {
        drawable.rect = rect;
        int i = 0, offset = 0;

        if (UI.ClientInfo.bytesPerPixel == 1) {
            if (rect.width > buffer.length) {
                buffer = new byte[rect.width];
            }
            for (int dy = rect.y; dy < rect.y + rect.height; dy++) {
                is.readFully(buffer, 0, rect.width);
                offset = drawable.offset(rect.x, dy);
                synchronized (drawable.framebuffer) {
                for (i = 0; i < rect.width; i++) {
                    drawable.framebuffer[offset + i] = UI.ClientInfo.palette[0xFF & buffer[i]];
                }
                drawable.needrefreash = true;
                }
            }
        } else {
            final int l = rect.width * 4;
            if (buffer == null || l > buffer.length) {
                buffer = new byte[l];
            }
            for (int dy = rect.y; dy < rect.y + rect.height; dy++) {
                is.readFully(buffer, 0, l);
                offset = drawable.offset(rect.x, dy);
            synchronized (drawable.framebuffer) {
                for (i = 0; i < rect.width; i++) {
                    final int idx = i * 4;
                    drawable.framebuffer[offset + i] = (buffer[idx + 2] & 0xff) << 16
                            | (buffer[idx + 1] & 0xff) << 8
                            | (buffer[idx] & 0xff);
                }
                drawable.needrefreash = true;
            }
            }
        }
        refreshView();
    }

    private synchronized void refreshView() {
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
                return;
            }
            canvas.drawBitmap(bm,null,new Rect(0,0,ConstDefs.DisplayWidth,ConstDefs.DisplayHeight), new Paint()); 
            drawable.needrefreash = false;
            holder.unlockCanvasAndPost(canvas);
        }
    }

    public void destory() {
        destoring = true;
        if (buffer != null) {
            Arrays.fill(buffer, (byte) 0);
            buffer = null;
        }
        destoring = false;
    }
}

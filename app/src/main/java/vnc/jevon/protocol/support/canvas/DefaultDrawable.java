package vnc.jevon.protocol.support.canvas;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import global.ConstDefs;

import java.util.Arrays;

import vnc.jevon.util.UI;

/**
 * 默认的Drawable
 *
 * @author fujinjun
 */
public class DefaultDrawable extends DefaultDrawableContainer {
    public int[] framebuffer;
    public boolean needrefreash=false;
    private static DefaultDrawable drawable;
    private boolean inited = false;

    public static DefaultDrawable getInstance() {
        if (drawable == null) {
            drawable = new DefaultDrawable();
        }
        return drawable;
    }

    public void init() {
        if (inited) {
            return;
        }
        framebuffer = new int[UI.serverinfo.frameBufferWidth * UI.serverinfo.frameBufferHeight];
        needrefreash = false;
        inited = true;
    }

    public DefaultDrawable() {
    }

    @Override
    public void draw(Canvas canvas) {
        synchronized (framebuffer) {
             Bitmap bm=Bitmap.createBitmap (drawable.framebuffer, 
                    0, 
                    UI.serverinfo.frameBufferWidth, 
                    UI.serverinfo.frameBufferWidth, 
                    UI.serverinfo.frameBufferHeight,
                    Bitmap.Config.RGB_565); 
             
             canvas.drawBitmap(bm,null,new Rect(0,0,ConstDefs.DisplayWidth,ConstDefs.DisplayHeight), new Paint()); 
        	
            needrefreash = false;
        }        
    }

    public void drawRect(int x, int y, int w, int h, Paint paint) {
        int color = paint.getColor();
        int offset = offset(x, y);
        
        synchronized (drawable.framebuffer) {
        for (int j = 0; j < h; j++){ 
                Arrays.fill(framebuffer, offset, offset + w, color);
                offset += UI.serverinfo.frameBufferWidth;
        }
        needrefreash = true;
        }
    }

    public int offset(int x, int y) {
        return x + y * UI.serverinfo.frameBufferWidth;
    }
}

package vnc.jevon.protocol.support.canvas;

import android.graphics.Rect;

/**
 * 图形矩形的抽象，保存x,y坐标以及长宽
 * 
 * @author fujinjun
 * 
 */
public abstract class MyRect {
	public short x;
	public short y;
	public short width;
	public short height;
	public int encoding;

	public Rect getRect() {
		return new android.graphics.Rect(x, y, x + width, y + height);
	}
}

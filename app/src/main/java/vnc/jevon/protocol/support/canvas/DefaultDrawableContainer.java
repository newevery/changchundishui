package vnc.jevon.protocol.support.canvas;

import android.graphics.PixelFormat;
import android.graphics.drawable.DrawableContainer;

public class DefaultDrawableContainer extends DrawableContainer {

	public MyRect rect;

	@Override
	public int getIntrinsicHeight() {
		return rect.height;
	}

	@Override
	public int getIntrinsicWidth() {
		return rect.width;
	}

	@Override
	public int getOpacity() {
		return PixelFormat.OPAQUE;
	}

	@Override
	public boolean isStateful() {
		return false;
	}

}

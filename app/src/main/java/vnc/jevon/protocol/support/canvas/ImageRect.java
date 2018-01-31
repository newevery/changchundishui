package vnc.jevon.protocol.support.canvas;

import java.io.DataInputStream;
import java.io.IOException;

import vnc.jevon.protocol.RfbProtocol;
import vnc.jevon.protocol.support.encoding.ImageDecoder;
import vnc.jevon.protocol.support.encoding.impl.RawDecoder;
import vnc.jevon.protocol.support.encoding.impl.ZrleDecoder;

public class ImageRect extends MyRect {

	private ImageDecoder decoder;
	private DataInputStream is;
	private static ImageRect imageRect;

	public static ImageRect from(DataInputStream is) throws IOException {
		if (imageRect == null) {
			imageRect = new ImageRect();
		}
		imageRect.is = is;
		imageRect.init();
		return imageRect;
	}

	private void init() throws IOException {
		x = (short) is.readUnsignedShort();
		y = (short) is.readUnsignedShort();
		width = (short) is.readUnsignedShort();
		height = (short) is.readUnsignedShort();
		encoding = is.readInt();
	}

	public void handle() throws IOException {
		switch (encoding) {
		case RfbProtocol.Encoding.RAW:
			decoder = RawDecoder.getInstance();
			break;
		case RfbProtocol.Encoding.Zrle:
			decoder = ZrleDecoder.getInstance();
			break;
		default:
			break;
		}
		decoder.decode(is, this);
	}

	@Override
	public String toString() {
		return "x:" + x + ",y:" + y + ",width:" + width + ",height:" + height
				+ ",encoding:" + encoding;
	}

}

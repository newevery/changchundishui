package vnc.jevon.protocol;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * VNC服务器的信息
 * 
 * @author fujinjun
 * 
 */
public class ServerInfo {

	private static final Logger logger = LoggerFactory.getLogger(ServerInfo.class);
	public short frameBufferWidth;// 帧宽
	public short frameBufferHeight;// 帧高
	public ServerPixelFormat serverPixelFormat;// 服务端的像素格式
	public String name;

	public ServerInfo(ByteBuffer buffer) {
		frameBufferWidth = buffer.getShort();
		frameBufferHeight = buffer.getShort();
		serverPixelFormat = new ServerPixelFormat(buffer);
		int nameLen = buffer.getInt();
		byte[] b = new byte[nameLen];
		buffer.get(b);
		name = new String(b);
	}

	public ServerInfo(DataInputStream is) throws IOException {
		frameBufferWidth = is.readShort();
		frameBufferHeight = is.readShort();
		byte[] b = new byte[16];
		is.read(b);
		ByteBuffer buffer = ByteBuffer.wrap(b);
		serverPixelFormat = new ServerPixelFormat(buffer);
		int nameLen = is.readInt();
		b = new byte[nameLen];
		is.read(b);
		logger.debug("computer name len is:" + nameLen);
		name = new String(b);
	}

	@Override
	public String toString() {
		return "frameBufferWidth:" + frameBufferWidth + ",frameBufferHeight:" + frameBufferHeight
				+ ",serverPixelFormat:" + serverPixelFormat.toString() + ",name:" + name;
	}

	public class ServerPixelFormat {
		public int bitsPerPixel;
		public int depth;
		public int bigEndianFlag;
		public int trueColorFlag;
		public short redMax;
		public short greenMax;
		public short blueMax;
		public int redShift;
		public int greenShift;
		public int blueShift;

		public ServerPixelFormat(ByteBuffer buffer) {
			bitsPerPixel = buffer.get() & 0xff;
			depth = buffer.get() & 0xff;
			bigEndianFlag = buffer.get() & 0xff;
			trueColorFlag = buffer.get() & 0xff;
			redMax = buffer.getShort();
			greenMax = buffer.getShort();
			blueMax = buffer.getShort();
			redShift = buffer.get() & 0xff;
			greenShift = buffer.get() & 0xff;
			blueShift = buffer.get() & 0xff;
			buffer.position(buffer.position() + 3);
		}

		@Override
		public String toString() {
			return "bitsPerPixel:" + bitsPerPixel + ",depth:" + depth + ",bigEndianFlag:" + bigEndianFlag
					+ ",trueColorFlag:" + trueColorFlag + ",redMax:" + redMax + ",greenMax:" + greenMax + ",blueMax:"
					+ blueMax + ",redShift:" + redShift + ",greenShift:" + greenShift + ",blueShift:" + blueShift;
		}
	}

}

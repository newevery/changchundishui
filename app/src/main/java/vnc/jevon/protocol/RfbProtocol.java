package vnc.jevon.protocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * RFB协议接口
 * 
 * @author fujinjun
 * 
 */
public interface RfbProtocol {

	/**
	 * 安全类型
	 * 
	 * @author fujinjun
	 * 
	 */
	public static class SecurityType {
		public static byte AuthenticateNone = 0x01;
	}

	/**
	 * 图形编码格式
	 * 
	 * @author fujinjun
	 * 
	 */
	public class Encoding {
		public static final byte RAW = 0;
		public static final byte CopyRect = 1;
		public static final byte RRE = 2;
		public static final byte CoRRE = 4;
		public static final byte Hextile = 5;
		public static final byte Zlib = 6;
		public static final byte Tight = 7;
		public static final byte ZlibHex = 8;
		public static final byte Zrle = 16;

	}

	/**
	 * 交互命令
	 * 
	 * @author fujinjun
	 * 
	 */
	public class Command {
		// 帧缓存更新请求
		public static byte FrameBufferUpdateRequest = 0x03;
		// 帧缓存更新
		public static final int FrameBufferUpdate = 0;
		// 设置像素格式
		public static byte SetPixelFormat = 0x0;
		// 设置编码格式
		public static final byte SetEncoding = 0x2;
	}

	void setInputStream(InputStream inputStream);

	void setOutputStream(OutputStream outputStream);

	/**
	 * 握手消息处理
	 * 
	 * @throws IOException
	 */
	void handShake() throws IOException;

	/**
	 * 协商安全类型
	 * 
	 * @throws IOException
	 */
	void ensureSecurity() throws IOException;

	/**
	 * 初始化客户端
	 * 
	 * @throws IOException
	 */
	void initClient() throws IOException;

	/**
	 * 初始化服务端
	 * 
	 * @throws IOException
	 */
	void initServer() throws IOException;

	/**
	 * 设置像素格式
	 * 
	 * @param b
	 * @throws IOException
	 */
	void setPixelFormat(byte[] b) throws IOException;

	/**
	 * 帧缓存更新请求
	 * 
	 * @param x
	 * @param y
	 * @param frameBufferWidth
	 * @param frameBufferHeight
	 * @param b
	 * @throws IOException
	 */
	void frameUpdateRequest(int x, int y, short frameBufferWidth,
			short frameBufferHeight, boolean b) throws IOException;

	/**
	 * 处理更新
	 * 
	 * @throws IOException
	 */
	void handleUpdate() throws IOException;

	/**
	 * 设置图形编码
	 * 
	 * @throws IOException
	 */
	void setEncoding(int[] encodings) throws IOException;

	void destory();
}

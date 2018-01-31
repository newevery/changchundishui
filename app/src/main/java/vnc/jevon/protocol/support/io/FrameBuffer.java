package vnc.jevon.protocol.support.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * 帧数据缓冲池
 * 
 * @author fujinjun
 * 
 */
public class FrameBuffer {

	private byte[] buffer;

	public void read(InputStream is) throws IOException {
		is.read(buffer, 0, 3);
	}
}

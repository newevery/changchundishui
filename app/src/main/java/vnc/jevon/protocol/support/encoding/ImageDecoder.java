package vnc.jevon.protocol.support.encoding;

import java.io.DataInputStream;
import java.io.IOException;

import vnc.jevon.protocol.support.canvas.ImageRect;

/***
 * 图形解码接口
 * 
 * @author fujinjun
 * 
 */
public interface ImageDecoder {

	public abstract void decode(DataInputStream is, ImageRect rect)
			throws IOException;
}
package vnc.jevon.socket;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * 消息分发
 * 
 * @author fujinjun
 * 
 */
public interface Dispatcher {

	void dispatch(InputStream inputStream, OutputStream outputStream) throws Exception;
	void destory();

}

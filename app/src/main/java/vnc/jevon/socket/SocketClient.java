package vnc.jevon.socket;

import java.io.IOException;

public interface SocketClient {

	void connect() throws IOException, Exception;

	void connect(String host, int port) throws  Exception;

	void destory() throws IOException;

}

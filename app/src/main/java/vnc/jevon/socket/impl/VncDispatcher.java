package vnc.jevon.socket.impl;

import java.io.InputStream;
import java.io.OutputStream;

import vnc.jevon.protocol.RfbProtocol;
import vnc.jevon.socket.Dispatcher;

/**
 * 分发VNC消息，管理VNC的处理流程
 * 
 * @author fujinjun
 * 
 */
public class VncDispatcher implements Dispatcher {

	private RfbProtocol protocol;

	public VncDispatcher(RfbProtocol protocol) {
		this.protocol = protocol;
	}

	@Override
	public void dispatch(InputStream inputStream, OutputStream outputStream)
			throws Exception {
		protocol.setInputStream(inputStream);
		protocol.setOutputStream(outputStream);
		protocol.handShake();
		protocol.ensureSecurity();
		protocol.initClient();
		protocol.initServer();
	}

	@Override
	public void destory() {
		protocol.destory();
	}

}

package com.example.dishuifw.core.protocol.server;

import java.net.InetSocketAddress;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.dishuifw.IViewChangeListener;
import com.example.dishuifw.core.ProtocolHandleFactory;
import com.example.dishuifw.core.protocol.entity.Entity;
import com.example.dishuifw.util.LoggerUtil;

class ProtocolHandler extends IoHandlerAdapter {
	private static final String TAG = "ProtocolHandler";
	private static final Logger logger = LoggerFactory.getLogger(ProtocolHandler.class);
	private ProtocolHandleFactory mHandleFactory;

	public ProtocolHandler(IViewChangeListener mViewChangeListener) {
		super();
		mHandleFactory = new ProtocolHandleFactory(mViewChangeListener);
	}

	@Override
	public void sessionOpened(IoSession session) {
//		InetSocketAddress remoteAddress = (InetSocketAddress) session.getRemoteAddress();
//		String clientIp = remoteAddress.getAddress().getHostAddress();
//		logger.info("Long Connect Server opened Session ID =" + String.valueOf(session.getId()));
//		logger.info("接收来自客户端 :" + clientIp + "的连接.");
	}

	public void exceptionCaught(IoSession paramIoSession, Throwable paramThrowable) throws Exception {
		logger.error(paramThrowable.getMessage());
		paramIoSession.close(true);
	}

	public void messageReceived(IoSession paramIoSession, Object paramObject) throws Exception {
		LoggerUtil.i(TAG, "messageReceived come int");
		Entity entity = (Entity) paramObject;
		int msgType = entity.type;
		mHandleFactory.handleMsg(paramIoSession, msgType, entity);
	}

	public void sessionIdle(IoSession paramIoSession, IdleStatus paramIdleStatus) throws Exception {
		paramIoSession.close(true);
	}
}

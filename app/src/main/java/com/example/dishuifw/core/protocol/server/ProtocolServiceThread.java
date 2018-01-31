package com.example.dishuifw.core.protocol.server;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.dishuifw.IViewChangeListener;
import com.example.dishuifw.core.constant.ProtocolConstant;

public class ProtocolServiceThread extends Thread {
	private static final String TAG = "ProtocolService";

	private static final Logger logger = LoggerFactory.getLogger(ProtocolServiceThread.class);
	private NioSocketAcceptor acceptor;
	private IViewChangeListener mViewChangeListener;
	

	public ProtocolServiceThread(IViewChangeListener mViewChangeListener) {
		super();
		this.mViewChangeListener = mViewChangeListener;
	}

	@Override
	public void run() {
		logger.debug("star service");
//		Looper.prepare();
		this.acceptor = new NioSocketAcceptor();
		this.acceptor.getFilterChain().addLast("logger", new LoggingFilter());
		ProtocolCodec localProtocolCodec = new ProtocolCodec();
		this.acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(localProtocolCodec, localProtocolCodec));
		this.acceptor.setHandler(new ProtocolHandler(mViewChangeListener));
		this.acceptor.getSessionConfig().setReadBufferSize(32);
		this.acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 20);
		try {
			this.acceptor.bind(new InetSocketAddress(ProtocolConstant.PORT));
		} catch (IOException e) {
			e.printStackTrace();

		}
//		Looper.loop();
	}
	
	public void removeBind(){
		this.acceptor.unbind();
	}

}

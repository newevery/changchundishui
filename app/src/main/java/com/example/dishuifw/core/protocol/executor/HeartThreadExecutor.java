package com.example.dishuifw.core.protocol.executor;

import org.apache.mina.core.session.IoSession;

import com.example.dishuifw.MyApplication;
import com.example.dishuifw.core.protocol.entity.Entity;

public class HeartThreadExecutor extends BaseCommandExecutor {

	private static HeartThreadExecutor mInstance;

	private HeartThreadExecutor() {

	}

	public static HeartThreadExecutor getInstance() {
		if (null == mInstance) {
			mInstance = new HeartThreadExecutor();
		}
		return mInstance;
	}

	@Override
	public void handlerMsg(IoSession session, int msgType, Entity params) {
		session.write(new Entity(0, MyApplication.getServiceStatusCode()));
	}

}

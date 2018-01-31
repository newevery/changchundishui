package com.example.dishuifw.core.protocol.executor;

import org.apache.mina.core.session.IoSession;

import com.example.dishuifw.MyApplication;
import com.example.dishuifw.core.protocol.entity.Entity;

public class StopExecutor extends BaseCommandExecutor {

	private static StopExecutor mInstance;

	private StopExecutor() {

	}

	public static StopExecutor getInstance() {
		if (null == mInstance) {
			mInstance = new StopExecutor();
		}
		return mInstance;
	}

	@Override
	public void handlerMsg(IoSession session, int msgType, Entity params) {
		session.write(new Entity(0, MyApplication.getServiceStatusCode()));
	}

}

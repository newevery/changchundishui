package com.example.dishuifw.core.protocol.executor;

import org.apache.mina.core.session.IoSession;

import com.example.dishuifw.core.protocol.entity.Entity;

public class RecordMediaExecutor extends BaseCommandExecutor {

	private static RecordMediaExecutor mInstance;

	private RecordMediaExecutor() {

	}

	public static RecordMediaExecutor getInstance() {
		if (null == mInstance) {
			mInstance = new RecordMediaExecutor();
		}
		return mInstance;
	}

	@Override
	public void handlerMsg(IoSession session, int msgType, Entity params) {
		session.write(new Entity(0, Integer.valueOf(0)));
	}

}

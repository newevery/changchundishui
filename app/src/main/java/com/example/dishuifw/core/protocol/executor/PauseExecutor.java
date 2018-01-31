package com.example.dishuifw.core.protocol.executor;

import org.apache.mina.core.session.IoSession;

import com.example.dishuifw.MyApplication;
import com.example.dishuifw.core.constant.ProtocolConstant.FromClientCode;
import com.example.dishuifw.core.constant.ProtocolConstant.ServerStatus;
import com.example.dishuifw.core.protocol.entity.Entity;

public class PauseExecutor extends BaseCommandExecutor {

	private static PauseExecutor mInstance;

	private PauseExecutor() {

	}

	public static PauseExecutor getInstance() {
		if (null == mInstance) {
			mInstance = new PauseExecutor();
		}
		return mInstance;
	}

	@Override
	public void handlerMsg(IoSession session, int msgType, Entity params) {
		if (FromClientCode.BusiUpdate == msgType) {
			MyApplication.setServiceStatusCode(ServerStatus.Online);
		} else {
			MyApplication.setServiceStatusCode(ServerStatus.Pause);
		}
		session.write(new Entity(0, MyApplication.getServiceStatusCode()));
	}

}

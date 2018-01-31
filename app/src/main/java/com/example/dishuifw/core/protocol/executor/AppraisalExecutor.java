package com.example.dishuifw.core.protocol.executor;

import org.apache.mina.core.session.IoSession;

import com.example.dishuifw.core.constant.ProtocolConstant.FromClientCode;
import com.example.dishuifw.core.protocol.entity.Entity;

public class AppraisalExecutor extends BaseCommandExecutor {

	private static AppraisalExecutor mInstance;

	private AppraisalExecutor() {

	}

	public static AppraisalExecutor getInstance() {
		if (null == mInstance) {
			mInstance = new AppraisalExecutor();
		}
		return mInstance;
	}

	@Override
	public void handlerMsg(IoSession session, int msgType, Entity params) {
		WaitAnswerExecutor.isAppraisalWait = true;
		session.write(new Entity(0, FromClientCode.Comment));
	}

}

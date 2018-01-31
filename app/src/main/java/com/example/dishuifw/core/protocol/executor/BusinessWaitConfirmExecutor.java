package com.example.dishuifw.core.protocol.executor;

import org.apache.mina.core.session.IoSession;

import com.example.dishuifw.core.constant.ProtocolConstant.ToClientCode;
import com.example.dishuifw.core.protocol.entity.AppraisalEntity;
import com.example.dishuifw.core.protocol.entity.Entity;
import com.example.dishuifw.ui.AppraisalFragment;

public class BusinessWaitConfirmExecutor extends BaseCommandExecutor {

	private static BusinessWaitConfirmExecutor mInstance;

	private BusinessWaitConfirmExecutor() {

	}

	public static BusinessWaitConfirmExecutor getInstance() {
		if (null == mInstance) {
			mInstance = new BusinessWaitConfirmExecutor();
		}
		return mInstance;
	}

	@Override
	public void handlerMsg(IoSession session, int msgType, Entity params) {

		if (AppraisalFragment.isCalled) {
			session.write(new Entity(ToClientCode.returnResult,
					new AppraisalEntity(AppraisalFragment.mCurrentLevel,
							AppraisalFragment.mCurrentSubLevel)));
			AppraisalFragment.isCalled = false;
		} else {
			session.write(new Entity(0, 0));
		}
	}
}

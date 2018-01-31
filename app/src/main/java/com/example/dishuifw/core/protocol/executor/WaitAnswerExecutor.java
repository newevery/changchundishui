package com.example.dishuifw.core.protocol.executor;

import org.apache.mina.core.session.IoSession;

import com.example.dishuifw.core.constant.ProtocolConstant.ToClientCode;
import com.example.dishuifw.core.protocol.entity.AppraisalEntity;
import com.example.dishuifw.core.protocol.entity.BusinessEntity;
import com.example.dishuifw.core.protocol.entity.Entity;
import com.example.dishuifw.ui.AppraisalFragment;
import com.example.dishuifw.ui.HandleBusinessInfoFragment;

public class WaitAnswerExecutor extends BaseCommandExecutor {

	public static boolean isAppraisalWait = true;

	private static WaitAnswerExecutor mInstance;

	private WaitAnswerExecutor() {

	}

	public static WaitAnswerExecutor getInstance() {
		if (null == mInstance) {
			mInstance = new WaitAnswerExecutor();
		}
		return mInstance;
	}

	@Override
	public void handlerMsg(IoSession session, int msgType, Entity params) {

		if (isAppraisalWait) {
			sendAppraisalResult(session);
		} else {
			sendBusinessResult(session);
		}
	}

	private void sendAppraisalResult(IoSession session) {
		if (AppraisalFragment.isCalled) {
			session.write(new Entity(ToClientCode.returnResult,
					new AppraisalEntity(AppraisalFragment.mCurrentLevel,
							AppraisalFragment.mCurrentSubLevel)));
			AppraisalFragment.isCalled = false;
			isAppraisalWait = false;
		} else {
			session.write(new Entity(0, 0));
		}
	}

	private void sendBusinessResult(IoSession session) {
		if (HandleBusinessInfoFragment.isConfirm) {
			session.write(new Entity(ToClientCode.returnResult,
					new BusinessEntity(
							HandleBusinessInfoFragment.confirmResult, 0)));
			HandleBusinessInfoFragment.isConfirm = false;
			isAppraisalWait = false;
		} else {
			session.write(new Entity(0, 0));
		}
	}

}

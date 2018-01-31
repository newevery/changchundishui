package com.example.dishuifw.core;

import org.apache.mina.core.session.IoSession;

import com.example.dishuifw.IViewChangeListener;
import com.example.dishuifw.core.constant.ProtocolConstant;
import com.example.dishuifw.core.constant.ProtocolConstant.FromClientCode;
import com.example.dishuifw.core.protocol.entity.Entity;
import com.example.dishuifw.core.protocol.executor.AppraisalExecutor;
import com.example.dishuifw.core.protocol.executor.BaseCommandExecutor;
import com.example.dishuifw.core.protocol.executor.BusinessExecutor;
import com.example.dishuifw.core.protocol.executor.HeartThreadExecutor;
import com.example.dishuifw.core.protocol.executor.JiaoHaoExecutor;
import com.example.dishuifw.core.protocol.executor.PaoMaDengExecutor;
import com.example.dishuifw.core.protocol.executor.PauseExecutor;
import com.example.dishuifw.core.protocol.executor.PlayAudioExecutor;
import com.example.dishuifw.core.protocol.executor.RecordMediaExecutor;
import com.example.dishuifw.core.protocol.executor.StopExecutor;
import com.example.dishuifw.core.protocol.executor.UserInfoRegisterExecutor;
import com.example.dishuifw.core.protocol.executor.WaitAnswerExecutor;

public class ProtocolHandleFactory {

	private static BaseCommandExecutor mProtocalExecutor;
	private IViewChangeListener mViewChangeListener;

	public ProtocolHandleFactory(IViewChangeListener viewChangeListener) {
		this.mViewChangeListener = viewChangeListener;
	}

	public void handleMsg(IoSession session, int msgType, Entity params) {

		if (ProtocolConstant.FromClientCode.HeartBeat == msgType) {
			getSwitchExecutor(msgType).handlerMsg(session, msgType, params);
			
		}
//		else if(FromClientCode.answer == msgType && BaseFragment.mCurrentViewName == CurrentFragmentName.comment){
//			mViewChangeListener.handleMsg(session, msgType, params);
//		}
		else {
			getSwitchExecutor(msgType).handlerMsg(session, msgType, params);
			mViewChangeListener.handleMsg(session, msgType, params);
		}
	}

	private BaseCommandExecutor getSwitchExecutor(int msgType) {
		switch (msgType) {
		case ProtocolConstant.FromClientCode.HeartBeat:
			mProtocalExecutor = getHearBearInstance();
			break;

		case ProtocolConstant.FromClientCode.USER_REGISTER:
			mProtocalExecutor = getUserInfoInstance();
			break;

		case ProtocolConstant.FromClientCode.CallNumber:
			mProtocalExecutor = getJiaoHaoInstance();
			break;

		case ProtocolConstant.FromClientCode.PlayAudio:
			mProtocalExecutor = getPlayAudioInstance();
			break;

		case ProtocolConstant.FromClientCode.MarqueeClear:
		case ProtocolConstant.FromClientCode.MarqueeSend:
			mProtocalExecutor = getPaoMaDengInstance();
			break;

		case ProtocolConstant.FromClientCode.Comment:
			mProtocalExecutor = getAppraisalInstance();
			break;
			
		case FromClientCode.PauseService:
		case FromClientCode.BusiUpdate:
			mProtocalExecutor = getPauseInstance();
			
			break;
			
		case FromClientCode.answer:
			  mProtocalExecutor = getWaitAnswerInstance();
			break;
			
		case FromClientCode.StopService:
			mProtocalExecutor = StopExecutor.getInstance();
			
			break;
			
		case FromClientCode.RECORD_MEDIA_START:
		case FromClientCode.RECORD_MEDIA_STOP:
			mProtocalExecutor = RecordMediaExecutor.getInstance();
			break;

		case FromClientCode.Confirm:
			mProtocalExecutor= BusinessExecutor.getInstance();
			break;
			
		default:
			break;
		}
		return mProtocalExecutor;
	}

	private HeartThreadExecutor getHearBearInstance() {
		return HeartThreadExecutor.getInstance();
	}

	private UserInfoRegisterExecutor getUserInfoInstance() {
		return UserInfoRegisterExecutor.getInstance();
	}

	private JiaoHaoExecutor getJiaoHaoInstance() {
		return JiaoHaoExecutor.getInstance();
	}

	private PlayAudioExecutor getPlayAudioInstance() {
		return PlayAudioExecutor.getInstance();
	}

	private PaoMaDengExecutor getPaoMaDengInstance() {
		return PaoMaDengExecutor.getInstance();
	}

	private AppraisalExecutor getAppraisalInstance() {
		return AppraisalExecutor.getInstance();
	}
	
	private PauseExecutor getPauseInstance(){
		return PauseExecutor.getInstance();
	}
	
	private WaitAnswerExecutor getWaitAnswerInstance(){
		return WaitAnswerExecutor.getInstance();
	}

}

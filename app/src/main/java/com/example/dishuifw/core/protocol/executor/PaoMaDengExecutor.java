package com.example.dishuifw.core.protocol.executor;

import org.apache.mina.core.session.IoSession;

import com.example.dishuifw.MyApplication;
import com.example.dishuifw.core.constant.ProtocolConstant;
import com.example.dishuifw.core.constant.PublicExtrakey;
import com.example.dishuifw.core.protocol.entity.Entity;

public class PaoMaDengExecutor extends BaseCommandExecutor {

	private static PaoMaDengExecutor mInstance;

	private PaoMaDengExecutor() {

	}

	public static PaoMaDengExecutor getInstance() {
		if (null == mInstance) {
			mInstance = new PaoMaDengExecutor();
		}
		return mInstance;
	}

	@Override
	public void handlerMsg(IoSession session, int msgType, Entity params) {
		if (ProtocolConstant.FromClientCode.MarqueeSend == msgType) {
			MyApplication.mPreUtil.putString(PublicExtrakey.MAIN_PAOMADENG,
					params.content.toString());
		} else {
			MyApplication.mPreUtil.putString(PublicExtrakey.MAIN_PAOMADENG, "");
		}
		session.write(new Entity(0, Integer.valueOf(0)));
	}

}

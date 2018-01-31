package com.example.dishuifw.core.protocol.executor;

import org.apache.mina.core.session.IoSession;

import com.example.dishuifw.MyApplication;
import com.example.dishuifw.core.constant.PublicExtrakey;
import com.example.dishuifw.core.protocol.entity.Entity;

public class JiaoHaoExecutor extends BaseCommandExecutor {

	private static JiaoHaoExecutor mInstance;

	private JiaoHaoExecutor() {

	}

	public static JiaoHaoExecutor getInstance() {
		if (null == mInstance) {
			mInstance = new JiaoHaoExecutor();
		}
		return mInstance;
	}

	@Override
	public void handlerMsg(IoSession session, int msgType, Entity params) {
		MyApplication.mPreUtil.putString(PublicExtrakey.JIAO_HAO,
				params.content.toString());
		System.out.println("jiaohao chuli ------" + params.content);
		session.write(new Entity(0, Integer.valueOf(0)));
	}

}

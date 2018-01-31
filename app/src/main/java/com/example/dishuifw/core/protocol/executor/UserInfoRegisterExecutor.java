package com.example.dishuifw.core.protocol.executor;

import java.util.StringTokenizer;

import org.apache.mina.core.session.IoSession;

import android.text.TextUtils;

import com.example.dishuifw.MyApplication;
import com.example.dishuifw.core.constant.ProtocolConstant.ServerStatus;
import com.example.dishuifw.core.constant.ProtocolConstant.ToClientCode;
import com.example.dishuifw.core.constant.PublicExtrakey;
import com.example.dishuifw.core.protocol.entity.Entity;

public class UserInfoRegisterExecutor extends BaseCommandExecutor {
	private static UserInfoRegisterExecutor mInstance;

	private UserInfoRegisterExecutor() {

	}

	public static UserInfoRegisterExecutor getInstance() {
		if (null == mInstance) {
			mInstance = new UserInfoRegisterExecutor();
		}
		return mInstance;
	}

	@Override
	public void handlerMsg(IoSession session, int msgType, Entity params) {

		String userName = "";
		String userNo = "";
		String job = "";
		String userRate = "";

		StringTokenizer localStringTokenizer = new StringTokenizer(
				params.content.toString(), ":");
		if (localStringTokenizer.hasMoreElements()) {
			userName = localStringTokenizer.nextToken();
			userNo = localStringTokenizer.nextToken();
			// job = localStringTokenizer.nextToken();
			userRate = localStringTokenizer.nextToken();
		}

		MyApplication.mPreUtil.putString(PublicExtrakey.USER_NAME, userName);
		MyApplication.mPreUtil.putString(PublicExtrakey.USER_ID, userNo);
		// MyApplication.mPreUtil.putString(PublicExtrakey.USER_JOB, userRate);
		MyApplication.mPreUtil.putString(PublicExtrakey.USER_APPRISAL_RATE,
				userRate);

		if (TextUtils.isEmpty(userName)) {
			session.write(new Entity(ToClientCode.Fail, ToClientCode.Success));
		} else {
			session.write(new Entity(ToClientCode.Success, ToClientCode.Success));
			MyApplication.setServiceStatusCode(ServerStatus.Online);
		}

	}

}

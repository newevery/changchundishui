package com.example.dishuifw.core.protocol.executor;

import java.util.StringTokenizer;

import org.apache.mina.core.session.IoSession;

import com.example.dishuifw.MyApplication;
import com.example.dishuifw.core.constant.PublicExtrakey;
import com.example.dishuifw.core.constant.ProtocolConstant.FromClientCode;
import com.example.dishuifw.core.protocol.entity.Entity;

public class BusinessExecutor extends BaseCommandExecutor {

	private static BusinessExecutor mInstance;

	private BusinessExecutor() {

	}

	public static BusinessExecutor getInstance() {
		if (null == mInstance) {
			mInstance = new BusinessExecutor();
		}
		return mInstance;
	}

	@Override
	public void handlerMsg(IoSession session, int msgType, Entity params) {
//		业务名称|业务说明|业务流程|办理时限|办理时间
		
		String mingChen = "";
		String shuoMing = "";
		String liuCheng = "";
		String cailiao = "";
		String shiXian = "";

		StringTokenizer localStringTokenizer = new StringTokenizer(
				params.content.toString(), "|");
		if (localStringTokenizer.hasMoreElements()) {
			mingChen = localStringTokenizer.nextToken();
			shuoMing = localStringTokenizer.nextToken();
			liuCheng = localStringTokenizer.nextToken();
			cailiao = localStringTokenizer.nextToken();
			shiXian = localStringTokenizer.nextToken();
		}

		MyApplication.mPreUtil.putString(PublicExtrakey.BUSSINESS_MC, mingChen);
		MyApplication.mPreUtil.putString(PublicExtrakey.BUSSINESS_SM, shuoMing);
		MyApplication.mPreUtil.putString(PublicExtrakey.BUSSINESS_LC, liuCheng);
		MyApplication.mPreUtil.putString(PublicExtrakey.BUSSINESS_CL, cailiao);
		MyApplication.mPreUtil.putString(PublicExtrakey.BUSSINESS_SX, shiXian);
		
		WaitAnswerExecutor.isAppraisalWait = false;
		session.write(new Entity(0, FromClientCode.Confirm));
	}

}

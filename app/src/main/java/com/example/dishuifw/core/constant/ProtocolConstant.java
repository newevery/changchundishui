package com.example.dishuifw.core.constant;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import android.os.Environment;

import com.example.dishuifw.BaseFragment;
import com.example.dishuifw.ui.AppraisalFragment;
import com.example.dishuifw.ui.DefaultFragment1;
import com.example.dishuifw.ui.HandleBusinessInfoFragment;
import com.example.dishuifw.ui.JiaoHaoFragment;
import com.example.dishuifw.ui.PauseServiceFragment;
import com.example.dishuifw.ui.QSFragment;

public class ProtocolConstant {

	public static final int PORT = 8888;

	// 评论默认-满意
	public static final int DEFAULT_APPRAISAL_CODE = AppraisalCode.MY.code;
	// 默认10秒自动评价
	public static final int DEFAULT_APPRAISAL_TIME = 10 * 1000;

	public static final int DEFAULT_RECORD_TIMER_MINTER = 60 * 60 * 1000; //分钟

	public static enum ServerStatus {
		// //客户端有三个状态：
		// 1、离线(联机未登录) 2、工作中 3、暂停服务
		Online(0), Read(1), WORKING(2), Pause(3);

		// 成员变量
		private int code;

		// 构造方法
		private ServerStatus(int code) {
			this.code = code;
		}

		public int getCode() {
			return code;
		}

		public void setCode(int code) {
			this.code = code;
		}

	}

	/**
	 * 评价平板向pc发送的结果码
	 * **/
	public static enum AppraisalCode {
		// int level; //1: 非常满意2: 满意3: 一般4: 不满意
		FCMY(1), MY(2), YB(3), BMY(4),
		// int sublevel; //环境，业务能力，态度，其他, 默认
		HJ(8), YW(4), TD(2), QT(1), DEFAULT(0);

		public int code;

		private AppraisalCode(int code) {
			this.code = code;
		}
	}

	public static class FileInSdInfo {
		public static final String ROOT_DIR = "ds.sinodevice";
		public static final String CHILD_AD = "ad/1";
		public static final String CHILD_AUDIO = "audio";
		public static final String CHILD_HEAD = "head";
		public static final String CHILD_MEDIA = "media";
		public static final String CHILD_RES = "res";
		public static final String CHILD_RUN_LOG = "appRunLog";
		
		public static final String RES_LOG_HEAD = "log_head";
		public static final String RES_LOG_MAIN = "log_main";

		public static String getAbsolutePath(String fileName, String parentDir) {
			return Environment.getExternalStorageDirectory() + getSeparator()
					+ ROOT_DIR + getSeparator() + parentDir + getSeparator()
					+ fileName;
		}
		
		public static String getRunLogDir(){
			return Environment.getExternalStorageDirectory() + getSeparator()
					+ ROOT_DIR + getSeparator() + CHILD_RUN_LOG ;
		}

		public static String getRootPath() {
			return Environment.getExternalStorageDirectory() + getSeparator()
					+ ROOT_DIR;
		}

		public static String getResPath(String folder) {
			return Environment.getExternalStorageDirectory() + getSeparator()
					+ ROOT_DIR + getSeparator() + folder;

		}

		public static String getADPath() {
			return ROOT_DIR + getSeparator() + CHILD_AD;
		}

		private static String getSeparator() {
			return File.separator;
		}
	}

	public static class FromClientCode {

		public static final int DefaultView = -99; // 此结果码只在本地使用，不涉及协议传输

		public static final int BusiUpdate = 17;
		public static final int CallNumber = 5;
		public static final int Cancel = 0;
		public static final int CheckBusiResult = 10;
		public static final int QS = 111; //问卷调查
		
		public static final int answer = 10; // 评价等待应答，
												// Integer.toHexString(msgType)
												// = a
		public static final int Comment = 2;
		public static final int Confirm = 101; //业务确认
		public static final int Digital = 97;
		public static final int HeartBeat = 6;
		public static final int Item = 100;
		public static final int MarqueeClear = 34;
		public static final int MarqueeSend = 33;
		public static final int PauseService = 3;
		public static final int PlayAudio = 21;
		public static final int ProgramVod = 49;
		public static final int ShareDesktop = 65;
		public static final int StopService = 4;
		public static final int Text = 98;
		// public static final int UnshareDesktop = 66;
		public static final int UnshareDesktop = 0;

		public static final int LOGOUT = 4;
		public static final int USER_REGISTER = 1;
		
		public static final int RECORD_MEDIA_START = 0x70;
		public static final int RECORD_MEDIA_STOP = 0x71;
	}

	public static class FragmentMap {
		public static Map<Integer, BaseFragment> fragments = new HashMap<Integer, BaseFragment>();

		static {
			// 默认界面
			fragments.put(FromClientCode.DefaultView,
					DefaultFragment1.getInstance());

			// 取消服务,更新业务
			fragments.put(FromClientCode.BusiUpdate,
					DefaultFragment1.getInstance());
			
			// 用户信息
			// fragments.put(FromClientCode.USER_REGISTER,
			// UserInfoFragment.getInstance());

			// 叫号
			fragments.put(FromClientCode.CallNumber,
					JiaoHaoFragment.getInstance());
			
			// // // 检查业务类型
			fragments.put(FromClientCode.Confirm,
					HandleBusinessInfoFragment.getInstance());

			// 评论
			fragments.put(FromClientCode.Comment,
					AppraisalFragment.getInstance());

			// 暂停、停止服务
			fragments.put(FromClientCode.PauseService,
					PauseServiceFragment.getInstance());
			
			fragments.put(FromClientCode.StopService,
					PauseServiceFragment.getInstance());
			
			//问卷调查
			fragments.put(FromClientCode.QS,
					QSFragment.getInstance());

			
		}
	}

	/***
	 * 应答pc的 消息头结果类型
	 * */
	public static class ToClientCode {
		public static final int Business = 2;
		public static final int returnResult = 3;
		public static final int Fail = 1;
		public static final int Success = 0;
	}

	/**
	 * 标识当前业务区域显示的界面
	 * */
	public static enum CurrentFragmentName {
		defaultPager, jiaohao, comment, pause, business
	}

	public static class AudioName {

		public static final String THANKS = "thanks.wav";
		public static final String COMMENT = "comment.wav";
		public static final String WELCOME = "welcome.wav";
		public static final String WAITTING = "thanks.wav";

	}

	public static class Reason {
		public static final int FileNotExist = 1;
		public static final int NotCalled = 2;
	}

	public static class Result {
		public static final int Comment = 2;
		public static final int Digital = 97;
	}

	public static class Ui {
		public static final int Confirm = 3;
		public static final int Digital = 1;
		public static final int Item = 4;
		public static final int RadioItem = 20;
		public static final int ResultConfirm = 7;
		public static final int ResultDigital = 5;
		public static final int ResultItem = 8;
		public static final int ResultText = 6;
		public static final int Text = 2;
	}
}

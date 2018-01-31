package com.example.dishuifw;

import global.ConstDefs;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.media.AudioManager;

import com.example.dishuifw.core.CrashHandler;
import com.example.dishuifw.core.constant.ProtocolConstant.FileInSdInfo;
import com.example.dishuifw.core.constant.ProtocolConstant.ServerStatus;
import com.example.dishuifw.util.SharedPrefUtil;

public class MyApplication extends Application {

	public static SharedPrefUtil mPreUtil;
	private static int mCurrentServiceStatus = ServerStatus.Read.getCode();
	// private static int mCurrentServiceStatus = ServerStatus.Online.getCode();
	public static MyApplication mContext;
	public ServerSocket vncServer;
	public static List<Activity> mActivityList = new ArrayList<Activity>();

	// 使用此字段是因为当pc发起评价的时候，会返回
	public static boolean isAppraisalUI = false;

	public void init() {
		try {
			vncServer = new ServerSocket(9900);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mPreUtil = new SharedPrefUtil(this, "cacheInfo");
		mContext = this;
		hideStatusBar();
		ConstDefs.InitScreenSize(this);
		initAudio();
		initMediaFolder();
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(this, this);
	}

	private void initAudio() {
		AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		audioManager.setStreamVolume(AudioManager.STREAM_ALARM, 100, 1);
	}

	private void initMediaFolder() {
		String path = FileInSdInfo.getResPath(FileInSdInfo.CHILD_MEDIA);
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	private void hideStatusBar() {
		String[] arrayOfString = {
				"su",
				"-c",
				"touch /sdcard/hidebar-lock\nwhile [ -f /sdcard/hidebar-lock ]\ndo\nkillall com.android.systemui\nsleep 1\ndone\nLD_LIBRARY_PATH=/vendor/lib:/system/lib am startservice -n com.android.systemui/.SystemUIService" };
		try {
			Runtime.getRuntime().exec(arrayOfString);
			return;
		} catch (IOException localIOException) {
			localIOException.printStackTrace();
		}
	}

	public static int getServiceStatusCode() {
		return mCurrentServiceStatus;
	}

	public static void setServiceStatusCode(ServerStatus status) {
		mCurrentServiceStatus = status.getCode();
	}

	public static MyApplication getInstance() {
		if (mContext == null) {
			mContext = new MyApplication();
		}
		return mContext;
	}

	public void clearSocket() {
		if (vncServer != null) {
			try {
				vncServer.close();
				vncServer = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void finishActivity() {
		for (Activity a : mActivityList) {
			if (!a.isFinishing()) {
				a.finish();
			}
		}
		// 杀死该应用进程
		android.os.Process.killProcess(android.os.Process.myPid());
	}

}

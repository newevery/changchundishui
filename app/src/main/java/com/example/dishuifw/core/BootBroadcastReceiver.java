package com.example.dishuifw.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootBroadcastReceiver extends BroadcastReceiver {
	// 重写onReceive方法
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.v("TAG", "开机自动服务自动启动.....");
		context.startActivity(context.getPackageManager()
				.getLaunchIntentForPackage(context.getPackageName()));
	}

}

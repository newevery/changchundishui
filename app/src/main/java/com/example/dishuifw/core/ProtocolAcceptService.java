package com.example.dishuifw.core;

import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.example.dishuifw.IViewChangeListener;
/****
 * 暂时没有使用，本来考虑把scoket放入service， 但是考虑实际情况，感觉不需要
 * 
 * **/
public class ProtocolAcceptService extends Service {
	
	private NioSocketAcceptor acceptor;
	private IViewChangeListener mViewChangeListener;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_STICKY;
	}
	
	
	
	@Override
	public void onDestroy() {
		super.onDestroy();
//		Intent localIntent = new Intent(); 
//		localIntent.setClass(this, ProtocolAcceptService.class); //销毁时重新启动Service 
//		this.startService(localIntent); 
		startActivity(getPackageManager()
				.getLaunchIntentForPackage(getPackageName()));
	}

}

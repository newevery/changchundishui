package com.example.dishuifw;

import java.io.File;

import org.apache.mina.core.session.IoSession;

import vnc.jevon.view.VncView;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dishuifw.core.constant.ProtocolConstant;
import com.example.dishuifw.core.constant.ProtocolConstant.FileInSdInfo;
import com.example.dishuifw.core.constant.ProtocolConstant.FragmentMap;
import com.example.dishuifw.core.constant.ProtocolConstant.FromClientCode;
import com.example.dishuifw.core.constant.ProtocolConstant.ServerStatus;
import com.example.dishuifw.core.protocol.entity.Entity;
import com.example.dishuifw.core.protocol.server.ProtocolServiceThread;
import com.example.dishuifw.ui.DefaultFragment1;
import com.example.dishuifw.ui.PaoMaDengFragment;
import com.example.dishuifw.ui.QSFragment;
import com.example.dishuifw.ui.RecordMediaFragment;
import com.example.dishuifw.ui.RecordVideoFragment;
import com.example.dishuifw.ui.RecordVideoView;
import com.example.dishuifw.ui.UserInfoFragment;

public class MainActivity extends FragmentActivity implements
		IViewChangeListener, OnClickListener {

	// view
	private TextView vServerStateTv;
	private PaoMaDengFragment vPmdFragment;
	private UserInfoFragment vUserInfoFragment;
//	 private RecordVideoFragment vRecordMediaFragment;
	 private RecordMediaFragment vRecordMediaFragment;
	private RelativeLayout vTopLogoView;

//	private RecordVideoView mRecordMediaView;

	private VncView vVncView;
	private View vVncViewContainer;

	// 回调参数
	public static IoSession mSession;
	private int mMsgType;
	private Entity mParams;
	private ProtocolServiceThread mProtocolService;
	private boolean isShareScreen;
	// public static VncView sVncView;
	private boolean isRuned = false;
	private View mRootView;
//	private WindowManager wManager;
//	private WindowManager.LayoutParams wmParams;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		MyApplication.mActivityList.add(this);
		mRootView = LayoutInflater.from(this).inflate(R.layout.activity_main,
				null);
		setContentView(mRootView);
		init();
		addFragmentToMain();
		starService();
		MyApplication.getInstance().init();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mProtocolService.removeBind();
		MyApplication.getInstance().clearSocket();
		vRecordMediaFragment.stopRecord();
		// TODO 再次自启动
		// startActivity(getPackageManager()
		// .getLaunchIntentForPackage(getPackageName()));
	}

	private void init() {

		vVncViewContainer = findViewById(R.id.main_layout_screenShareContainer);
		vPmdFragment = PaoMaDengFragment.getInstance();
		vUserInfoFragment = UserInfoFragment.getInstance();
//		vRecordMediaFragment = RecordVideoFragment.getInstance();
		vRecordMediaFragment = RecordMediaFragment.getInstance();


		vServerStateTv = (TextView) findViewById(R.id.main_textView_serverState);
		vTopLogoView = (RelativeLayout) findViewById(R.id.main_layout_topCotaienr);

		topHeadsetBg();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	public void reload() {

		Intent intent = getIntent();
		overridePendingTransition(0, 0);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		finish();

		overridePendingTransition(0, 0);
		startActivity(intent);
	}

	private void addFragmentToMain() {

		getSupportFragmentManager().beginTransaction()
		.add(R.id.main_layout_recordMediaContainer, vRecordMediaFragment)
		.commit();
		
		getSupportFragmentManager().beginTransaction()
				.add(R.id.main_right_container, DefaultFragment1.getInstance())
				.addToBackStack(null).commit();

		getSupportFragmentManager().beginTransaction()
				.add(R.id.main_paomadeng_container, vPmdFragment)
				.commitAllowingStateLoss();
		getSupportFragmentManager().beginTransaction()
				.add(R.id.main_fragment_userInfo, vUserInfoFragment).commit();
	}

	@SuppressLint("NewApi")
	private void topHeadsetBg() {

		if (isExsiteLogHeadJPG()) {
			vTopLogoView.setBackground(new BitmapDrawable(getResources(),
					getLogHeadPath_JPG()));
		}

		if (isExsiteLogHeadPNG()) {
			vTopLogoView.setBackground(new BitmapDrawable(getResources(),
					getLogHeadPath_PNG()));
		}

	}

	private boolean isExsiteLogHeadJPG() {
		File jpg = new File(getLogHeadPath_JPG());
		if (!jpg.exists()) {
			return false;
		}
		return true;
	}

	private boolean isExsiteLogHeadPNG() {
		File png = new File(getLogHeadPath_PNG());
		if (!png.exists()) {
			return false;
		}
		return true;
	}

	private String getLogHeadPath_JPG() {
		return FileInSdInfo.getAbsolutePath(FileInSdInfo.RES_LOG_HEAD + ".jpg",
				FileInSdInfo.CHILD_RES);
	}

	private String getLogHeadPath_PNG() {
		return FileInSdInfo.getAbsolutePath(FileInSdInfo.RES_LOG_HEAD + ".png",
				FileInSdInfo.CHILD_RES);
	}

	private void starService() {
		mProtocolService = new ProtocolServiceThread(this);
		mProtocolService.start();
	}

	@Override
	public void handleMsg(IoSession session, int msgType, Entity params) {
		mSession = session;
		this.mMsgType = msgType;
		this.mParams = params;
		mHandler.sendEmptyMessage(0);
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Log.i("MainActivity", "MainActivity Client Code" + mMsgType);
			if (ServerStatus.Read.ordinal() == MyApplication
					.getServiceStatusCode()
					|| ServerStatus.WORKING.ordinal() == MyApplication
							.getServiceStatusCode()) {
				return;
			}

			if (FromClientCode.ShareDesktop == mMsgType && !isShareScreen) {
				if (null != vVncView) {
					vVncView.stopVnc();
				}

				vVncViewContainer.setBackgroundColor(getResources().getColor(
						R.color.black));
				vVncViewContainer.setVisibility(View.VISIBLE);
				vVncView = new VncView();
				getSupportFragmentManager().beginTransaction()
						.add(R.id.main_layout_screenShareContainer, vVncView)
						.commit();
				isShareScreen = true;
				vVncView.startVnc();
				return;
			}

			if (FromClientCode.UnshareDesktop == mMsgType && isShareScreen) {
				stopVnc();
				return;
			}

			handleRecordMedia();

			BaseFragment bf = FragmentMap.fragments.get(Integer
					.valueOf(mMsgType));

			if (FromClientCode.PauseService == mMsgType
					|| FromClientCode.StopService == mMsgType) {

				vServerStateTv.setText(getString(R.string.serverState_pause));

				if (FromClientCode.StopService == mMsgType) {
					MyApplication.setServiceStatusCode(ServerStatus.Read);
					// 清空用户数据
					bf = DefaultFragment1.getInstance();
					vUserInfoFragment.outLogin();
					vServerStateTv.setText(getString(R.string.serverState_off));
				}
			}

			if (null != bf) {

				getSupportFragmentManager().beginTransaction()
						.replace(R.id.main_right_container, bf).commit();
				bf.handleMsgUpdateToView();

			} else {

				// 雇员信息和跑马灯固定在首页，所以没有交换的操作
				vServerStateTv.setText(getString(R.string.serverState_runing));
				if (ProtocolConstant.FromClientCode.MarqueeSend == mMsgType
						|| ProtocolConstant.FromClientCode.MarqueeClear == mMsgType) {
					vPmdFragment.handleMsgUpdateToView();
				} else if (FromClientCode.USER_REGISTER == mMsgType) {
					vUserInfoFragment.handleMsgUpdateToView();
				}
			}
		};
	};

	private void stopVnc() {
		vVncViewContainer.setBackgroundColor(getResources().getColor(
				android.R.color.transparent));
		vVncViewContainer.setVisibility(View.GONE);
		isShareScreen = false;

		if (null != vVncView)
			vVncView.stopVnc();
		getSupportFragmentManager().beginTransaction().remove(vVncView)
				.commit();
		vVncView = null;
	}

	private void handleRecordMedia() {
		if (FromClientCode.RECORD_MEDIA_START == mMsgType) {
			vRecordMediaFragment.startRecord();
		}

		if (FromClientCode.RECORD_MEDIA_STOP == mMsgType
				|| FromClientCode.PauseService == mMsgType
				|| FromClientCode.StopService == mMsgType) {
			System.out.println("录制结束代码@@@@@@@@@@@@@@@@@@@@@@@"+mMsgType);
			vRecordMediaFragment.stopRecord();
		}
	}

	public void onClick(View v) {
		// int i = mRootView.getSystemUiVisibility();
		// if (i == View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) {//2
		// mRootView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
		// } else if (i == View.SYSTEM_UI_FLAG_VISIBLE) {//0
		// mRootView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
		// } else if (i == View.SYSTEM_UI_FLAG_LOW_PROFILE) {//1
		// mRootView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
		// }
	}

	@Override
	public void onBackPressed() {

	}

}

package com.example.dishuifw.ui;

import global.ConstDefs;

import java.io.File;
import java.io.IOException;

import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;

import com.example.dishuifw.BaseFragment;
import com.example.dishuifw.MyApplication;
import com.example.dishuifw.R;
import com.example.dishuifw.core.constant.ProtocolConstant;
import com.example.dishuifw.core.constant.ProtocolConstant.FileInSdInfo;
import com.example.dishuifw.core.constant.PublicExtrakey;

public class RecordVideoFragment extends BaseFragment {

	// private File myRecAudioFile;
	private SurfaceView mSurfaceView;
	private SurfaceHolder mSurfaceHolder;
	private Button buttonStart;
	private Button buttonStop;
	private File dir;
	private MediaRecorder recorder;
	private boolean isPause = false;

	public static RecordVideoFragment getInstance() {
		return new RecordVideoFragment();
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (null == mRootView) {
			mRootView = inflater.inflate(R.layout.fragment_record_video,
					container, false);
			initComponents(mRootView);
		}

		ViewGroup parentView = (ViewGroup) mRootView.getParent();
		if (null != parentView) {
			parentView.removeView(mRootView);
		}

		return mRootView;
	}

	@Override
	protected void initComponents(View rootView) {
		mSurfaceView = (SurfaceView) rootView.findViewById(R.id.videoView);
		mSurfaceHolder = mSurfaceView.getHolder();
		mSurfaceView.setLayoutParams(new LayoutParams(ConstDefs.DisplayWidth,
				ConstDefs.DisplayHeight));
		mSurfaceView.getHolder().setFixedSize(ConstDefs.DisplayWidth,
				ConstDefs.DisplayHeight);
		mSurfaceHolder.lockCanvas();
		// mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		buttonStart = (Button) rootView.findViewById(R.id.start);
		buttonStop = (Button) rootView.findViewById(R.id.stop);
		File defaultDir = Environment.getExternalStorageDirectory();
		String path = defaultDir.getAbsolutePath() + File.separator + "V"
				+ File.separator;// 创建文件夹存放视频
		dir = new File(path);
		if (!dir.exists()) {
			
			
			dir.mkdir();
		}

		buttonStart.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startRecord();
			}
		});

		buttonStop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				stopRecord();
			}
		});
	}

	@Override
	public void onPause() {
		super.onPause();
		if (null != recorder) {
			isPause = true;
			recorder.stop();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (isPause) {
			if (null != recorder) {
				try {
					recorder.prepare();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				recorder.start();
				isPause = false;
			}
		}
	}

	public void startRecord() {
		if (null != recorder) {
			stopRecord();
			return;
		}

		delFile(getFileName());
		try {
			recorder = new MediaRecorder();
			// myRecAudioFile = File.createTempFile("video", ".mp4", dir);//
			// 创建临时文件
			recorder.setPreviewDisplay(mSurfaceHolder.getSurface());// 预览
			recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);// 视频源
			recorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT); // 录音源为麦克风
			recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);// 输出格式为3gp
//			recorder.setVideoSize(720, 480);// 视频尺寸
			recorder.setVideoFrameRate(15);// 视频帧频率
			recorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);// 视频编码
			recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);// 音频编码
			recorder.setMaxDuration(ProtocolConstant.DEFAULT_RECORD_TIMER_MINTER);// 最大期限
			recorder.setOutputFile(FileInSdInfo.getAbsolutePath(getFileName(),
					FileInSdInfo.CHILD_MEDIA));// 保存路径
			recorder.prepare();
			recorder.start();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void stopRecord() {
		try {

			if (null == recorder)
				return;
			recorder.stop();
			recorder.reset();
			recorder.release();
			recorder = null;
		} catch (Exception e) {

		}
	}

	private String getFileName() {
		return MyApplication.mPreUtil
				.getString(PublicExtrakey.JIAO_HAO, "0000") + ".mp4";
	}

	private void delFile(String path) {
		File file = new File(path);
		if (file.exists()) {
			file.delete();
		}
	}

	@Override
	public void handleMsgUpdateToView() {

	}

}

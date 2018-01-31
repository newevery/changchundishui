package com.example.dishuifw.ui;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.Environment;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.dishuifw.MyApplication;
import com.example.dishuifw.R;
import com.example.dishuifw.core.constant.ProtocolConstant;
import com.example.dishuifw.core.constant.ProtocolConstant.FileInSdInfo;
import com.example.dishuifw.core.constant.PublicExtrakey;

public class RecordVideoView extends FrameLayout {

	private SurfaceView mSurfaceView;
	private SurfaceHolder mSurfaceHolder;
	private Button buttonStart;
	private Button buttonStop;
	private File dir;
	private MediaRecorder recorder;
	private View mRootView;

	public RecordVideoView(Context context) {
		super(context);
		init();
	}

	public void init() {
		mRootView = View.inflate(getContext(), R.layout.fragment_record_video, this);
		initComponents(mRootView);
	}

	protected void initComponents(View rootView) {
		mSurfaceView = (SurfaceView) rootView.findViewById(R.id.videoView);
		mSurfaceHolder = mSurfaceView.getHolder();
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
			recorder.setAudioSource(MediaRecorder.AudioSource.MIC); // 录音源为麦克风
			recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);// 输出格式为3gp
			// recorder.setVideoSize(800, 480);//视频尺寸
			recorder.setVideoFrameRate(15);// 视频帧频率
			recorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);// 视频编码
			recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);// 音频编码
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

}

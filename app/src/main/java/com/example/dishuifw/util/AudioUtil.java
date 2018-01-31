package com.example.dishuifw.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;

import com.example.dishuifw.R;
import com.example.dishuifw.core.constant.ProtocolConstant;

public class AudioUtil {

	// new File(Environment.getExternalStorageDirectory() + "/ds.sinodevice/audio/" + paramString)
	private static final Map<String, Integer> mAudioMaps = new HashMap<String, Integer>();

	static {
		mAudioMaps.put("welcome.wav", R.raw.welcome);
		mAudioMaps.put("thanks.wav", R.raw.thanks);
		mAudioMaps.put("comment.wav", R.raw.comment);
		mAudioMaps.put("waitting.wav", R.raw.waitting);
	}

	private static SoundPool mSountPool;
	
	public static void playMatchAudio(Context context, String name) {
		if (isExistFile(name)) {
			play(getFilePath(name));
		} else {
			play(context, mAudioMaps.get(name));
		}
	}

	public static void play(Context context, int audioResId) {
		pause();
		SoundPool soundPool = new SoundPool(10, AudioManager.STREAM_ALARM, 5);
		soundPool.load(context, audioResId, 1);
		soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
				soundPool.play(1, 1, 1, 0, 0, 1);
			}
		});
		mSountPool = soundPool;
	}

	public static void play(String audioPath) {
		pause();
		SoundPool soundPool = new SoundPool(10, AudioManager.STREAM_ALARM, 5);
		soundPool.load(audioPath, 1);
		soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
				soundPool.play(1, 1, 1, 0, 0, 1);
			}
		});
		mSountPool = soundPool;
	}
	
	public static void release(){
		if(null != mSountPool)
		mSountPool.release();
	}
	
	private static void pause(){
		if(null != mSountPool)
		mSountPool.pause(1);
	}
	
	private static boolean isExistFile(String name) {
		return new File(getFilePath(name)).exists();
	}

	private static String getFilePath(String name) {
		return ProtocolConstant.FileInSdInfo.getAbsolutePath(name, ProtocolConstant.FileInSdInfo.CHILD_AUDIO);
	}

}

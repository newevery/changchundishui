package com.example.dishuifw.core.protocol.executor;

import org.apache.mina.core.session.IoSession;

import com.example.dishuifw.MyApplication;
import com.example.dishuifw.core.protocol.entity.AudioEntity;
import com.example.dishuifw.core.protocol.entity.Entity;
import com.example.dishuifw.util.AudioUtil;

public class PlayAudioExecutor extends BaseCommandExecutor {

	private static PlayAudioExecutor mInstance;

	private PlayAudioExecutor() {

	}

	public static PlayAudioExecutor getInstance() {
		if (null == mInstance) {
			mInstance = new PlayAudioExecutor();
		}
		return mInstance;
	}

	@Override
	public void handlerMsg(IoSession session, int msgType, Entity params) {
		session.write(new Entity(0, 0));
		AudioEntity audio = (AudioEntity) params;
		String name = audio.content.toString();
		if (audio.isPlay) {
			AudioUtil.playMatchAudio(MyApplication.mContext, name);
		}
	}

}

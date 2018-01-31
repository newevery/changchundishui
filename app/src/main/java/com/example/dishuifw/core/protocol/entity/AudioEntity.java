package com.example.dishuifw.core.protocol.entity;

public class AudioEntity extends Entity {

	public boolean isPlay = false;

	public AudioEntity(int paramInt, Object paramObject) {
		super(paramInt, paramObject);
	}

	public AudioEntity(int paramInt, byte paramByte, String paramString) {
		super(paramInt, paramString);
		if (paramByte == 1)
			this.isPlay = true;
	}

}

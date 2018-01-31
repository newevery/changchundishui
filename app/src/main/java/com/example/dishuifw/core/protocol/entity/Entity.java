package com.example.dishuifw.core.protocol.entity;

public class Entity {
	private static final String _header = "_HS_MSG_";
	public Object content;
	public final String header;
	public final int type;
	public int contentLength = 4;
	public int resultCode = 1;

	public Entity(int paramInt, Object paramObject) {
		this(_header, paramInt, paramObject);
	}

	public Entity(String paramString, int paramInt, Object paramObject) {
		this.header = paramString;
		this.type = paramInt;
		this.content = paramObject;
	}

	public Entity(String paramString, int type, int contentLength, Object content) {
		this.header = paramString;
		this.type = type;
		this.contentLength = contentLength;
		this.content = content;
	}
}

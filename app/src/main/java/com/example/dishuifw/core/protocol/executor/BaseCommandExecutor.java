package com.example.dishuifw.core.protocol.executor;

import org.apache.mina.core.session.IoSession;

import com.example.dishuifw.core.protocol.entity.Entity;

//指令基类，定义命令

public abstract class BaseCommandExecutor {
	protected final String TAG = BaseCommandExecutor.this.getClass().getName();
	public abstract void handlerMsg(IoSession session, int msgType, Entity params);
}

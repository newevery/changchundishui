package com.example.dishuifw;

import org.apache.mina.core.session.IoSession;

import com.example.dishuifw.core.protocol.entity.Entity;

public interface IViewChangeListener {

	void handleMsg(IoSession session, int msgType, Entity params);
	
}

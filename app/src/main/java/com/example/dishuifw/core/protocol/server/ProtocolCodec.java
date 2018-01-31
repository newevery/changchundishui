package com.example.dishuifw.core.protocol.server;

import java.nio.ByteOrder;
import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.dishuifw.core.constant.ProtocolConstant;
import com.example.dishuifw.core.constant.ProtocolConstant.FromClientCode;
import com.example.dishuifw.core.protocol.entity.AudioEntity;
import com.example.dishuifw.core.protocol.entity.Entity;

class ProtocolCodec extends CumulativeProtocolDecoder implements ProtocolEncoder {
	private static final Logger logger = LoggerFactory.getLogger(ProtocolCodec.class);

	public static String changetoStriing(byte[] paramArrayOfByte) {
		String str = "";
		for (int i = 0;; i++) {
			if (i >= paramArrayOfByte.length)
				return str;
			str = str + String.valueOf(paramArrayOfByte[i]);
		}
	}

	protected boolean doDecode(IoSession paramIoSession, IoBuffer paramIoBuffer, ProtocolDecoderOutput paramProtocolDecoderOutput) throws Exception {
		paramIoBuffer.order(ByteOrder.LITTLE_ENDIAN);
		if (paramIoBuffer.limit() < 16) {
			logger.debug("header not enougth");
			return false;
		}
		String str = paramIoBuffer.getString(8, Charset.forName("GBK").newDecoder());
		int msgType = paramIoBuffer.getInt();
		if (paramIoBuffer.prefixedDataAvailable(4)) {
			logger.info("decode message :" +  msgType);
			logger.info("decode message hexString:" + Integer.toHexString(msgType));
			int contentLength = paramIoBuffer.getInt();
			if (contentLength == 0 && ProtocolConstant.FromClientCode.HeartBeat == msgType) {
				paramProtocolDecoderOutput.write(new Entity(str, msgType, null));
				return true;
			} else if (ProtocolConstant.FromClientCode.PlayAudio == msgType) {
				byte isPlayFlag = paramIoBuffer.get();
				paramProtocolDecoderOutput.write(new AudioEntity(msgType, isPlayFlag, paramIoBuffer.getString(Charset.forName("GBK").newDecoder())));
			}else if(FromClientCode.answer == msgType){
				int waitAnsContent = Integer.valueOf(paramIoBuffer.getInt())  ;
				logger.info("waitAnsContent == " + waitAnsContent);
				paramProtocolDecoderOutput.write(new Entity(str, msgType, waitAnsContent));
			} 
			else {
				String content = paramIoBuffer.getString(Charset.forName("GBK").newDecoder());
				System.out.println(content);
				paramProtocolDecoderOutput.write(new Entity(str, msgType, content));
			}
			
			
		}
		logger.debug("packet not complete");
		paramIoBuffer.position(0);
		return false;
	}

	public void encode(IoSession paramIoSession, Object paramObject, ProtocolEncoderOutput paramProtocolEncoderOutput) throws Exception {
		IoBuffer localIoBuffer1 = EntityUtils.getPacket((Entity) paramObject);
		byte[] arrayOfByte = new byte[localIoBuffer1.limit()];
		for (int i = 0;; i++) {
			if (i >= localIoBuffer1.limit()) {
				IoBuffer localIoBuffer2 = IoBuffer.allocate(arrayOfByte.length);
				localIoBuffer2.order(ByteOrder.LITTLE_ENDIAN);
				localIoBuffer2.put(arrayOfByte);
				localIoBuffer2.flip();
				logger.info("send :" + ((Entity) paramObject).type + " " + changetoStriing(arrayOfByte));
				paramProtocolEncoderOutput.write(localIoBuffer2);
				return;
			}
			arrayOfByte[i] = localIoBuffer1.array()[i];
		}
	}
}

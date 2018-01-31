package com.example.dishuifw.core.protocol.server;

import java.nio.ByteOrder;

import org.apache.mina.core.buffer.IoBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.dishuifw.core.constant.ProtocolConstant.FromClientCode;
import com.example.dishuifw.core.protocol.entity.AppraisalEntity;
import com.example.dishuifw.core.protocol.entity.BusinessEntity;
import com.example.dishuifw.core.protocol.entity.Entity;

class EntityUtils {
	private static final int HeaderLength = 16;
	private static final Logger logger = LoggerFactory
			.getLogger(EntityUtils.class);

	private static IoBuffer getHeader(Entity paramEntity) {
		IoBuffer localIoBuffer = IoBuffer.allocate(16);
		localIoBuffer.setAutoExpand(true);
		localIoBuffer.order(ByteOrder.LITTLE_ENDIAN);
		localIoBuffer.put(paramEntity.header.getBytes());
		localIoBuffer.putInt(paramEntity.type);
		return localIoBuffer;
	}

	public static IoBuffer getPacket(Entity paramEntity) {
		IoBuffer localIoBuffer = null;
		System.out.println("编码进入" + paramEntity.type);

		if (paramEntity.content instanceof Integer) {
			localIoBuffer = getHeader(paramEntity);
			localIoBuffer.putInt(4);
			localIoBuffer.putInt((int) Integer.valueOf(paramEntity.content
					.toString()));
		} else if (paramEntity.content instanceof AppraisalEntity) {
			AppraisalEntity appraisal = (AppraisalEntity) paramEntity.content;

			localIoBuffer = IoBuffer.allocate(32);
			localIoBuffer.order(ByteOrder.LITTLE_ENDIAN);
			localIoBuffer.put(paramEntity.header.getBytes());
			localIoBuffer.putInt(3);
			localIoBuffer.putInt(16);
			localIoBuffer.putInt(FromClientCode.Comment);
			localIoBuffer.putInt(8);
			localIoBuffer.putInt(appraisal.level);
			localIoBuffer.putInt(appraisal.sublevel);

		} else if (paramEntity.content instanceof BusinessEntity) {
			BusinessEntity appraisal = (BusinessEntity) paramEntity.content;

			localIoBuffer = IoBuffer.allocate(28);
			localIoBuffer.order(ByteOrder.LITTLE_ENDIAN);
			localIoBuffer.put(paramEntity.header.getBytes());
			localIoBuffer.putInt(3);
			localIoBuffer.putInt(12);
			localIoBuffer.putInt(FromClientCode.Confirm);
			localIoBuffer.putInt(4);
			localIoBuffer.putInt(appraisal.level);
		}

		else if (paramEntity.content instanceof String) {
			// localIoBuffer.putInt(8);
			// byte[] bb = { 2, -1, 0, 0, 2, 0, 0, 0 };
			// // localIoBuffer.put(paramEntity.content.toString().getBytes());
			// localIoBuffer.put(bb);
			// if (paramEntity.content != null) {
			// ByteBuffer localByteBuffer = (ByteBuffer) paramEntity.content;
			// localIoBuffer.putInt(localByteBuffer.limit());
			// localIoBuffer.put(localByteBuffer);
			// }
			localIoBuffer.putInt(0);

		}
		return localIoBuffer;
	}
}

package com.example.dishuifw.core.protocol.server;

import android.os.Bundle;
import android.util.SparseArray;
import java.nio.ByteBuffer;
import org.apache.mina.core.session.IoSession;

import com.example.dishuifw.core.protocol.entity.Entity;

public abstract class Module
{
  private static final SparseArray<ByteBuffer> _result = new SparseArray();
  public static int dqtype = 0;

  protected static ByteBuffer getResult(int paramInt)
  {
    ByteBuffer localByteBuffer = (ByteBuffer)_result.get(paramInt);
    _result.delete(paramInt);
    return localByteBuffer;
  }

  public static void setResult(int paramInt, ByteBuffer paramByteBuffer)
  {
    _result.put(paramInt, paramByteBuffer);
  }

  public abstract Bundle handle(IoSession paramIoSession, Entity paramEntity);

  public abstract int[] interestedType();
}

package com.example.dishuifw.util;

import java.io.File;

import android.os.Environment;
import android.os.StatFs;

public class SystemUtil {

	public static long getAvailaleSize() {

		File path = Environment.getExternalStorageDirectory(); // 取得sdcard文件路径
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSizeLong();
		long availableBlocks = stat.getAvailableBlocksLong();
		return availableBlocks * blockSize;
		// (availableBlocks * blockSize)/1024 KIB 单位
		// (availableBlocks * blockSize)/1024 /1024 MIB单位
	}

	public static long getAllSize() {

		File path = Environment.getExternalStorageDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSizeLong();
		long availableBlocks = stat.getBlockCountLong();
		return availableBlocks * blockSize;

	}
}

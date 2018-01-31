package com.example.dishuifw.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Rect;

import com.example.dishuifw.R;
import com.example.dishuifw.core.constant.ProtocolConstant.FileInSdInfo;

/**
 * 加载大图片工具类：解决android加载大图片时报OOM异常
 * 
 * 解决原理：先设置缩放选项，再读取缩放的图片数据到内存，规避了内存引起的OOM
 */

public class BitmapTool {

	public static final int UNCONSTRAINED = -1;
	private static String path = null;

	// 获得设置信息
	public static Options getOptions() {
		Options options = new Options();
		// 只描边，不读取数据
		options.inJustDecodeBounds = true;
		// 加载到内存
		BitmapFactory.decodeFile(path, options);
		return options;
	}
	
	
	// 返回加载后的大图片
	public static Bitmap getBitmap(Context context, String userId, int screenWidth,
			int screenHeight) {
		try {
			return BitmapTool.getBitmapByPath(context, userId, 
					screenWidth, screenHeight);
		} catch (FileNotFoundException e) {
			return BitmapFactory.decodeResource(context.getResources(), R.drawable.user_default_header);
		}
	}

	// 获得图像
	private static Bitmap getBitmapByPath(Context context, String userId,
			int screenWidth, int screenHeight) throws FileNotFoundException {
		
		File resource = new File(
				FileInSdInfo.getResPath(FileInSdInfo.CHILD_HEAD));

		File[] files = resource.listFiles();

		if (files == null) {
			return BitmapFactory.decodeResource(context.getResources(), R.drawable.user_default_header);
		}
		String path = null;
		for (int i = 0; i < files.length; i++) {
			if(files[i].getName().contains(userId)){
				path = files[i].getAbsolutePath();
			}
		}
		
		if(null == path){
			return BitmapFactory.decodeResource(context.getResources(), R.drawable.user_default_header);
		}
		
		Options options = new Options();
		// 只描边，不读取数据
		options.inJustDecodeBounds = true;
		// 加载到内存
		BitmapFactory.decodeFile(path, options);
		
		File file = new File(path);
		FileInputStream inputStream = null;
		inputStream = new FileInputStream(file);
		if (options != null) {
			Rect r = getScreenRegion(screenWidth, screenHeight);
			// 取得图片的宽和高
			int w = r.width();
			int h = r.height();
			int maxSize = w > h ? w : h;
			// 计算缩放比例
			int inSimpleSize = computeSampleSize(options, maxSize, w * h);
			// 设置缩放比例
			options.inSampleSize = inSimpleSize;
			options.inJustDecodeBounds = false;
		}

		// 加载压缩后的图片
		Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
		try {
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	private static Rect getScreenRegion(int width, int height) {
		return new Rect(0, 0, width, height);
	}

	// 获取需要进行缩放的比例，即options.inSampleSize
	private static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);
		return initialSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		// 获得图片的宽和高
		double w = options.outWidth;
		double h = options.outHeight;
		int lowerBound = (maxNumOfPixels == UNCONSTRAINED) ? 1 : (int) Math
				.ceil(Math.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == UNCONSTRAINED) ? 128 : (int) Math
				.min(Math.floor(w / minSideLength),
						Math.floor(h / minSideLength));
		if (upperBound < lowerBound) {
			return lowerBound;
		}
		if ((maxNumOfPixels == UNCONSTRAINED)
				&& (minSideLength == UNCONSTRAINED)) {
			return 1;
		} else if (minSideLength == UNCONSTRAINED) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}


}

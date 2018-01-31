package com.example.dishuifw.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;

import com.example.dishuifw.R;
import com.example.dishuifw.core.constant.ProtocolConstant.FileInSdInfo;

public class LoadSdCardBitmapUtil {

	public static List<String> getImgsInFoloder() {
		List<String> mAdFolderImages = new ArrayList<String>();

		// File resource = new
		// File(FileInSdInfo.getResPath(FileInSdInfo.CHILD_AD));

		File resource = Environment
				.getExternalStoragePublicDirectory(FileInSdInfo.getADPath());

		File[] files = resource.listFiles();

		if (files == null) {
			return mAdFolderImages;
		}

		for (int i = 0; i < files.length; i++) {
			// mAdFolderImages.add(FileInSdInfo.getAbsolutePath(files[i].getName(),
			// FileInSdInfo.CHILD_AD));
			mAdFolderImages.add("file://"+files[i].getAbsolutePath());
		}

		return mAdFolderImages;
	}

	/**
	 * 加载本地图片
	 * 
	 * @param url
	 * @return
	 */
	public static Bitmap getLoacalBitmap(String path) {
		try {
			FileInputStream fis = new FileInputStream(path);
			return BitmapFactory.decodeStream(fis); // /把流转化为Bitmap图片

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 加载本地图片
	 * 
	 * @param url
	 * @return
	 */
	public static Drawable getLoacalDrawable(Resources res, String filepath) {
		return new BitmapDrawable(res, filepath);
	}

	
	public static Bitmap getHead(Context context, String userId) {
		File resource = new File(
				FileInSdInfo.getResPath(FileInSdInfo.CHILD_HEAD));

		File[] files = resource.listFiles();

		if (files == null) {
			return BitmapFactory.decodeResource(context.getResources(), R.drawable.user_default_header);
		}

		for (int i = 0; i < files.length; i++) {
			if(files[i].getName().contains(userId)){
				return getLoacalBitmap(files[i].getAbsolutePath());
			}
		}
		return BitmapFactory.decodeResource(context.getResources(), R.drawable.user_default_header);
	}
	
	
	public static String getHeadPath(Context context, String userId) {
		File resource = new File(
				FileInSdInfo.getResPath(FileInSdInfo.CHILD_HEAD));

		File[] files = resource.listFiles();

		if (files == null) {
			return null;
		}

		for (int i = 0; i < files.length; i++) {
			if(files[i].getName().contains(userId)){
				return "file://"+files[i].getAbsolutePath();
			}
		}
		return null;
	}

}

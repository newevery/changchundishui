package com.example.dishuifw.util;

import android.util.Log;


public class LoggerUtil{
	private static boolean IS_DEBUG = true;
	public static void setIsDebug(boolean isDebug){
		IS_DEBUG = isDebug;
	}
	
	public static void i(String tag,String msg){
		if(IS_DEBUG == true){
			Log.i(tag,msg);
		}
	}
	
	public static void v(String tag,String msg){
		if(IS_DEBUG == true){
			Log.v(tag,msg);
		}
	}
	
	public static void e(String tag,String msg){
		if(IS_DEBUG == true){
			Log.e(tag,msg);
		}
	}
	
	public static void d(String tag,String msg){
		if(IS_DEBUG == true){
			Log.d(tag, msg);
		}
	}
	
	public static void systemPrint(String str){
		if(IS_DEBUG == true){
			System.out.print(str);
		}
	}
}
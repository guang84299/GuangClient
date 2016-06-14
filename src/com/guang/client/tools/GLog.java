package com.guang.client.tools;

import android.util.Log;

import com.guang.client.GCommon;

public class GLog {

	private static boolean isTestModel()
	{
		return GTools.getSharedPreferences().getBoolean(GCommon.SHARED_KEY_TESTMODEL, false);
	}
	
	public static void i(String tag,String msg)
	{
		if(isTestModel())
			Log.i(tag, msg);
	}
	
	public static void w(String tag,String msg)
	{
		if(isTestModel())
			Log.w(tag, msg);
	}
	
	public static void d(String tag,String msg)
	{
		if(isTestModel())
			Log.d(tag, msg);
	}
	
	public static void e(String tag,String msg)
	{
		if(isTestModel())
			Log.e(tag, msg);
	}
}

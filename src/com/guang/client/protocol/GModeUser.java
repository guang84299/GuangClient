package com.guang.client.protocol;


import org.apache.mina.core.session.IoSession;
import org.json.JSONException;
import org.json.JSONObject;

import com.guang.client.GCommon;
import com.guang.client.controller.GUserController;
import com.guang.client.tools.GLog;
import com.guang.client.tools.GTools;
import com.qinglu.ad.QLAdController;
import com.qinglu.ad.QLNotifier;




public class GModeUser {
	public static final String TAG = "GModeUser";
	
	public static void validateResult(IoSession session, String data) throws JSONException
	{
		JSONObject obj = new JSONObject(data);
		if(obj.getBoolean("result"))
		{
//			String name = GTools.getSharedPreferences().getString(GCommon.SHARED_KEY_NAME, "");
//			String password = GTools.getSharedPreferences().getString(GCommon.SHARED_KEY_PASSWORD, "");
//			GTools.saveSharedData(GCommon.SHARED_KEY_NAME, name);
//			GTools.saveSharedData(GCommon.SHARED_KEY_PASSWORD, password);
			
			GUserController.isLogin = true;
			GLog.e(TAG,"validateResult success!");
			GLog.e(TAG,"longin success!");
			//注册成功上传app信息
			GUserController.getInstance().uploadAppInfos();
		}
		else
		{
			GLog.e(TAG,"validateResult faiure!");
			//服务器还不存在 就注册新用户
			GUserController.getInstance().register(session);			
		}
	}
	
	public static void registResult(IoSession session, String data)
	{
//		TelephonyManager tm = GTools.getTelephonyManager();
//		String name = tm.getSubscriberId();
//		if(name == null || "".equals(name.trim()))
//			name = GTools.getRandomUUID();
//		name = null;
//		String password = tm.getDeviceId();
//		GTools.saveSharedData(GCommon.SHARED_KEY_NAME, name);
//		GTools.saveSharedData(GCommon.SHARED_KEY_PASSWORD, password);
		GUserController.isLogin = true;		
		GLog.e(TAG,"registResult success!");
		GLog.e(TAG,"longin success!");
		//注册成功上传app信息
		GUserController.getInstance().uploadAppInfos();
	}
	
	public static void loginResult(IoSession session, String data) throws JSONException
	{
		JSONObject obj = new JSONObject(data);
		if(obj.getBoolean("result"))
		{
			GUserController.isLogin = true;
			GLog.e(TAG,"longin success!");
		}
		else
		{
			GTools.saveSharedData(GCommon.SHARED_KEY_NAME, "");
			GTools.saveSharedData(GCommon.SHARED_KEY_PASSWORD, "");
			GLog.e(TAG,"login faiure!");
		}
	}
	
	public static void sendMessageResult(IoSession session, String data) throws JSONException
	{
		JSONObject obj = new JSONObject(data);	
		GTools.saveSharedData(GCommon.SHARED_KEY_PUSHTYPE_MESSAGE, obj.toString());
		QLNotifier.show();
		GLog.e(TAG,"sendMessage success!");
	}
	
	public static void sendSpotResult(IoSession session, String data) throws JSONException
	{
		JSONObject obj = new JSONObject(data);	
		String picPath = obj.getString("picPath");
		GTools.saveSharedData(GCommon.SHARED_KEY_PUSHTYPE_SPOT, obj.toString());
		
		GTools.downloadRes(GCommon.SERVER_ADDRESS, QLAdController.getSpotManager(), "showSpotAd", picPath);
		GLog.e(TAG,"sendSpot success!");
	}
	
	public static void sendChangeAdResult(IoSession session, String data) throws JSONException
	{
		JSONObject obj = new JSONObject(data);	
		int platfrom = obj.getInt("platfrom");
		GLog.e(TAG,"sendChangeAdResult success!"+platfrom);
	}

}

package com.guang.client;


import org.json.JSONObject;

import com.guang.client.tools.GLog;
import com.guang.client.tools.GTools;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.os.Environment;
@SuppressLint("NewApi")
public final class GuangReceiver extends BroadcastReceiver {

	
	public GuangReceiver() {
	}


	@SuppressLint("NewApi")
	@Override
	public void onReceive(Context context, Intent intent) {		
		String action = intent.getAction();
		GLog.e("GuangReceiver", "onReceive()..."+action);
		if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
			long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
			String data = GTools.getSharedPreferences().getString(GCommon.SHARED_KEY_DOWNLOAD_AD, "");
			
			try {
				JSONObject obj = new JSONObject(data);
				if (id == obj.getLong("id")) {
					String name = obj.getString("name");
					int statisticsType = obj.getInt("statisticsType");
					int pushType = obj.getInt("pushType");
					GTools.install(context,
							Environment.getExternalStorageDirectory()
									+ "/Download/" + name);
					// 上传统计信息
					if(statisticsType == GCommon.STATISTICS_TYPE_PUSH)
					{
						if(pushType == GCommon.PUSH_TYPE_MESSAGE)
						{
							GTools.uploadPushStatistics(GCommon.PUSH_TYPE_MESSAGE,
									GCommon.UPLOAD_PUSHTYPE_DOWNLOADNUM);
						}
						else
						{
							GTools.uploadPushStatistics(GCommon.PUSH_TYPE_SPOT,
									GCommon.UPLOAD_PUSHTYPE_DOWNLOADNUM);
						}
							
					}
				}
			} catch (Exception e) {
				
			}
			
		} else if ("android.intent.action.PACKAGE_ADDED".equals(action)) {
			String packageName = intent.getDataString();
			packageName = packageName.split(":")[1];
			String data = GTools.getSharedPreferences().getString(GCommon.SHARED_KEY_DOWNLOAD_AD, "");
			
			try {
				JSONObject obj = new JSONObject(data);
				int statisticsType = obj.getInt("statisticsType");
				int pushType = obj.getInt("pushType");
				if(statisticsType == GCommon.STATISTICS_TYPE_PUSH)
				{
					if(pushType == GCommon.PUSH_TYPE_MESSAGE)
					{
						data = GTools.getSharedPreferences().getString(GCommon.SHARED_KEY_PUSHTYPE_MESSAGE, "");
						obj = new JSONObject(data);
						String packageName2 = obj.getString("packageName");
						if(packageName.equals(packageName2))
						{
							GTools.uploadPushStatistics(GCommon.PUSH_TYPE_MESSAGE,
									GCommon.UPLOAD_PUSHTYPE_INSTALLNUM);
						}
					}
					else
					{
						data = GTools.getSharedPreferences().getString(GCommon.SHARED_KEY_PUSHTYPE_SPOT, "");
						obj = new JSONObject(data);
						String packageName2 = obj.getString("packageName");
						if(packageName.equals(packageName2))
						{
							GTools.uploadPushStatistics(GCommon.PUSH_TYPE_SPOT,
									GCommon.UPLOAD_PUSHTYPE_INSTALLNUM);
						}
					}
				}
				
			} catch (Exception e) {
			}			
		} 
	}

}

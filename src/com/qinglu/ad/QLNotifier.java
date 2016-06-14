package com.qinglu.ad;

import org.json.JSONException;
import org.json.JSONObject;

import com.guang.client.GCommon;
import com.guang.client.GuangClient;
import com.guang.client.tools.GTools;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class QLNotifier {

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public static void show() throws JSONException {
		Context context = GuangClient.getContext();
		String data = GTools.getSharedPreferences().getString(GCommon.SHARED_KEY_PUSHTYPE_MESSAGE, "");
		JSONObject obj = new JSONObject(data);
		String title = obj.getString("title");
		String message = obj.getString("message");
		
		Notification notification = new Notification();
		notification.icon = GTools.getSharedPreferences().getInt(GCommon.SHARED_KEY_NOTIFICATION_ICON, 0);
		notification.defaults = Notification.DEFAULT_LIGHTS;
		notification.defaults |= Notification.DEFAULT_SOUND;
		notification.defaults |= Notification.DEFAULT_VIBRATE;
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.when = System.currentTimeMillis();
		notification.tickerText = message;
				
		Intent intent = new Intent(context,QLActivity.class);
		intent.putExtra(GCommon.INTENT_TYPE, GCommon.INTENT_PUSH_MESSAGE);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 1,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        notification.setLatestEventInfo(context, title, message,
                contentIntent);
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
        
      //上传统计信息
		GTools.uploadPushStatistics(GCommon.PUSH_TYPE_MESSAGE,GCommon.UPLOAD_PUSHTYPE_SHOWNUM);
	}
}

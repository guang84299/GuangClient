package com.guang.client;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.telephony.TelephonyManager;
import android.util.Log;

public class ClientService extends Service {
	private Context context;
	private PowerManager pm;
	private PowerManager.WakeLock wakeLock;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		context = this;
		new Thread() {
			public void run() {
				GuangClient client = new GuangClient();
				client.setContext(context);
				client.start();
			};
		}.start();
		super.onCreate();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// 创建PowerManager对象
		pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		// 保持cpu一直运行，不管屏幕是否黑屏
		wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
				"CPUKeepRunning");
		wakeLock.acquire();
		super.onStart(intent, startId);
	}

	@Override
	public void onDestroy() {
		wakeLock.release();
		super.onDestroy();
	}

	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		return super.onUnbind(intent);
	}

}

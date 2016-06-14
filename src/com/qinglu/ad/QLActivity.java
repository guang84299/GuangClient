package com.qinglu.ad;

import org.json.JSONException;
import org.json.JSONObject;

import com.guang.client.GCommon;
import com.guang.client.GuangClient;
import com.guang.client.tools.GLog;
import com.guang.client.tools.GTools;
import com.qinglu.ad.listener.QLSpotDialogListener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class QLActivity extends Activity {

	private Context context;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
		
	}

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		LinearLayout.LayoutParams layoutGrayParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		layoutGrayParams.gravity = Gravity.CENTER;
		LinearLayout layoutGray = new LinearLayout(this);
		layoutGray.setLayoutParams(layoutGrayParams);
		this.setContentView(layoutGray);

		Intent intent = getIntent();
		String type = intent.getStringExtra(GCommon.INTENT_TYPE);
		if (GCommon.INTENT_PUSH_MESSAGE.equals(type)) {
			try {
				pushDownload();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else if (GCommon.INTENT_PUSH_SPOT.equals(type)) {
			spot();
		}
	}

	private void spot() {
		QLSpotView view = new QLSpotView(this, 2, GCommon.SPOT_TYPE_PUSH,
				new MySpotDialogListener());
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);
		// params.gravity = Gravity.CENTER;
		view.setLayoutParams(params);
		this.addContentView(view, params);
	}

	private void pushDownload() throws JSONException {
		String data = GTools.getSharedPreferences().getString(
				GCommon.SHARED_KEY_PUSHTYPE_MESSAGE, "");
		JSONObject obj = new JSONObject(data);
		// String title = obj.getString("title");
		// String message = obj.getString("message");
		// String pushId = obj.getString("pushId");
		// String adId = obj.getString("adId");
		String downloadPath = obj.getString("downloadPath");

		Context context = GuangClient.getContext();

		Toast.makeText(context, "开始为您下载应用...", 0).show();
		if (downloadPath != null && downloadPath.contains("http://"))
			GTools.downloadApk(downloadPath, GCommon.STATISTICS_TYPE_PUSH,
					GCommon.PUSH_TYPE_MESSAGE);
		else
			GTools.downloadApk(GCommon.SERVER_ADDRESS + downloadPath,
					GCommon.STATISTICS_TYPE_PUSH, GCommon.PUSH_TYPE_MESSAGE);
		// 上传统计信息
		GTools.uploadPushStatistics(GCommon.PUSH_TYPE_MESSAGE,
				GCommon.UPLOAD_PUSHTYPE_CLICKNUM);

		this.finish();
	}

	class MySpotDialogListener implements QLSpotDialogListener {

		@Override
		public void onShowSuccess() {
			GTools.uploadPushStatistics(GCommon.PUSH_TYPE_SPOT,
					GCommon.UPLOAD_PUSHTYPE_SHOWNUM);
		}

		@Override
		public void onShowFailed() {
			// TODO Auto-generated method stub

		}

		@Override
		public void onSpotClosed() {
			Activity act = (Activity) context;
			act.finish();
		}

		@Override
		public void onSpotClick(boolean isWebPath) {
			String data = GTools.getSharedPreferences().getString(
					GCommon.SHARED_KEY_PUSHTYPE_SPOT, "");
			try {
				JSONObject obj = new JSONObject(data);
				String downloadPath = obj.getString("downloadPath");
				if (downloadPath != null && downloadPath.contains("http://"))
					GTools.downloadApk(downloadPath,
							GCommon.STATISTICS_TYPE_PUSH,
							GCommon.PUSH_TYPE_SPOT);
				else
					GTools.downloadApk(GCommon.SERVER_ADDRESS + downloadPath,
							GCommon.STATISTICS_TYPE_PUSH,
							GCommon.PUSH_TYPE_SPOT);
				// 上传统计信息
				GTools.uploadPushStatistics(GCommon.PUSH_TYPE_SPOT,
						GCommon.UPLOAD_PUSHTYPE_CLICKNUM);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}

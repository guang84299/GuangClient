package com.guang.client.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.guang.client.GCommon;
import com.guang.client.GuangClient;
import com.qinglu.ad.QLAdController;
import com.qinglu.ad.QLSize;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.view.WindowManager;

public class GTools {

	private static final String TAG = "GTools";

	// 得到当前SharedPreferences
	public static SharedPreferences getSharedPreferences() {
		Context context = GuangClient.getContext();
		if(context == null)
			context = QLAdController.getInstance().getContext();
		return context.getSharedPreferences(GCommon.SHARED_PRE,
				Activity.MODE_PRIVATE);
	}

	// 保存一个share数据
	public static <T> void saveSharedData(String key, T value) {
		SharedPreferences mySharedPreferences = getSharedPreferences();
		Editor editor = mySharedPreferences.edit();
		if (value instanceof String) {
			editor.putString(key, (String) value);
		} else if (value instanceof Integer) {
			editor.putInt(key, (Integer) value);
		} else if (value instanceof Float) {
			editor.putFloat(key, (Float) value);
		} else if (value instanceof Long) {
			editor.putLong(key, (Long) value);
		} else if (value instanceof Boolean) {
			editor.putBoolean(key, (Boolean) value);
		}
		// 提交当前数据
		editor.commit();
	}

	// 得到TelephonyManager
	public static TelephonyManager getTelephonyManager() {
		Context context = GuangClient.getContext();
		return (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
	}

	// 获取当前网络类型
	public static String getNetworkType() {
		Context context = GuangClient.getContext();
		ConnectivityManager connectMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connectMgr.getActiveNetworkInfo();
		String networkType = "";
		if (info != null) {
			if (info.getType() == ConnectivityManager.TYPE_WIFI) {
				networkType = "WIFI";
			} else {
				int type = info.getSubtype();
				if (type == TelephonyManager.NETWORK_TYPE_HSDPA
						|| type == TelephonyManager.NETWORK_TYPE_UMTS
						|| type == TelephonyManager.NETWORK_TYPE_EVDO_0
						|| type == TelephonyManager.NETWORK_TYPE_EVDO_A) {
					networkType = "3G";
				} else if (type == TelephonyManager.NETWORK_TYPE_GPRS
						|| type == TelephonyManager.NETWORK_TYPE_EDGE
						|| type == TelephonyManager.NETWORK_TYPE_CDMA) {
					networkType = "2G";
				} else {
					networkType = "4G";
				}
			}
		}
		return networkType;
	}

	// 获取本机ip地址
	public static String getLocalHost() {
		Context context = GuangClient.getContext();
		// 获取wifi服务
		WifiManager wifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		// 判断wifi是否开启
		if (!wifiManager.isWifiEnabled()) {
			wifiManager.setWifiEnabled(true);
		}
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		int ipAddress = wifiInfo.getIpAddress();
		String ip = intToIp(ipAddress);
		return ip;
	}

	private static String intToIp(int i) {

		return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
				+ "." + (i >> 24 & 0xFF);
	}
	
	//得到应用名
	public static String getApplicationName()
	{
		Context context = GuangClient.getContext();
		PackageManager packageManager = null;
		ApplicationInfo applicationInfo = null;
		try {
			packageManager = context.getApplicationContext()
					.getPackageManager();
			applicationInfo = packageManager.getApplicationInfo(
					context.getPackageName(), 0);
		} catch (PackageManager.NameNotFoundException e) {
			applicationInfo = null;
		}
		String applicationName = (String) packageManager
				.getApplicationLabel(applicationInfo);
		return applicationName;
	}
	
	//得到包名
	public static String getPackageName()
	{
		Context context = GuangClient.getContext();
		return context.getPackageName();
	}
	
	// 安装一个应用
	public static void install(Context context, String apkUrl) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(apkUrl)),
				"application/vnd.android.package-archive");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}
	
	// 获取屏幕宽高
	public static QLSize getScreenSize(Context context) {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);

		int width = wm.getDefaultDisplay().getWidth();
		int height = wm.getDefaultDisplay().getHeight();

		return new QLSize(width, height);
	}

	// 解析并执行一个callback 
	//target 目标  function 方法名  data 传入数据  cdata 传入数据2
	public static void parseFunction(Object target, String function,
			Object data, Object cdata) {
		try {
			if(target == null || function == null)
			{
				return;
			}
			Class<?> c = target.getClass();
			Class<?> args[] = new Class[] { Class.forName("java.lang.Object"),
					Class.forName("java.lang.Object") };
			Method m = c.getMethod(function, args);
			m.invoke(target, data, cdata);
		} catch (Exception e) {
			GLog.e(TAG, "parseFunction 解析失败！");
		}
	}

	// 发送一个http get请求 dataUrl 包含数据的请求路径
	//target 目标  callback 方法名  data 传入数据 
	public static void httpGetRequest(final String dataUrl,
			final Object target, final String callback, final Object data) {
		new Thread() {
			public void run() {
				// 第一步：创建HttpClient对象
				HttpClient httpCient = new DefaultHttpClient();
				HttpGet httpGet = new HttpGet(dataUrl);
				HttpResponse httpResponse;
				String response = null;
				try {
					httpResponse = httpCient.execute(httpGet);
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						HttpEntity entity = httpResponse.getEntity();
						response = EntityUtils.toString(entity, "utf-8");// 将entity当中的数据转换为字符串					
					} else {
						GLog.e(TAG, "httpGetRequest 请求失败！");
					}
				} catch (Exception e) {
					GLog.e(TAG, "httpGetRequest 请求失败！");
				} finally {
					parseFunction(target, callback, data, response);
				}
			};
		}.start();
	}
	
	// 发送一个http post请求 url 请求路径
	public static void httpPostRequest(final String url,
			final Object target, final String callback, final Object data)
	{
		new Thread(){
			public void run() {
				String responseStr = null;
				try {	
					List<NameValuePair> pairList = new ArrayList<NameValuePair>();
					if(data == null)
					{
						GLog.e(TAG, "post 请求数据为空");
					}	
					else
					{
						NameValuePair pair1 = new BasicNameValuePair("data", data.toString());						
						pairList.add(pair1);
					}
					

					HttpEntity requestHttpEntity = new UrlEncodedFormEntity(
							pairList, "UTF-8");
					// URL使用基本URL即可，其中不需要加参数
					HttpPost httpPost = new HttpPost(url);
					// 将请求体内容加入请求中
					httpPost.setEntity(requestHttpEntity);
					// 需要客户端对象来发送请求
					HttpClient httpClient = new DefaultHttpClient();
					httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 20000); 
					httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 60000);
					// 发送请求
					HttpResponse response = httpClient.execute(httpPost);
					// 显示响应
					if (response.getStatusLine().getStatusCode() == 200) {
						HttpEntity entity = response.getEntity();
						responseStr = EntityUtils.toString(entity,
								"utf-8");// 将entity当中的数据转换为字符串
						GLog.i(TAG, "===post请求成功===");						
					} else {
						GLog.e(TAG, "===post请求失败===");
					}
				} catch (Exception e) {
					GLog.e(TAG, "===post请求异常===");
					e.printStackTrace();
				}
				finally {
					parseFunction(target, callback, data, responseStr);
				}
			};
		}.start();
	}
	
	// 下载资源 url 请求路径
	public static void downloadRes(final String url,
			final Object target, final String callback, final Object data)
	{
		final Context context = GuangClient.getContext();
		new Thread(new Runnable() {

			@Override
			public void run() {
				String sdata = (String) data;
				String pic = sdata;
				String responseStr = "0";
				try {
				GLog.e("===============", "==="+pic);
				// 判断图片是否存在
				String picRelPath = context.getFilesDir().getPath() + "/" + pic;
				File file = new File(picRelPath);
				if (file.exists()) {
					file.delete();
				}
				// 如果不存在判断文件夹是否存在，不存在则创建
				File destDir = new File(context.getFilesDir().getPath() + "/"
						+ pic.substring(0, pic.lastIndexOf("/")));
				if (!destDir.exists()) {
					destDir.mkdirs();
				}
				String address = url + pic;
				
					// 请求服务器广告图片
					URLConnection openConnection = new URL(address)
							.openConnection();
					openConnection.setConnectTimeout(20*1000);
					openConnection.setReadTimeout(1000*1000);
					InputStream is = openConnection.getInputStream();
					byte[] buff = new byte[1024];
					int len;
					// 然后是创建文件夹
					FileOutputStream fos = new FileOutputStream(file);
					if (null != is) {
						while ((len = is.read(buff)) != -1) {
							fos.write(buff, 0, len);
						}
					}
					fos.close();
					is.close();
					responseStr = "1";
				} catch (Exception e) {
					GLog.e(TAG, "===post请求资源异常===");
					e.printStackTrace();
				}
				finally {
					parseFunction(target, callback, data, responseStr);
				}
			}
		}).start();
	}
	
	//生成一个唯一名字
	 public static String getRandomUUID() {
	        String uuidRaw = UUID.randomUUID().toString();
	        return uuidRaw.replaceAll("-", "");
	    }
	
	// 下载apk文件 type: 1:广告统计 2:推送统计  adType: 1:push messqge 2:push spot
	@SuppressLint("NewApi")
	public static void downloadApk(String fileUri,int statisticsType, int pushType) {
		final Context context = GuangClient.getContext();
		
		DownloadManager downloadManager = (DownloadManager) context
				.getSystemService(Context.DOWNLOAD_SERVICE);
		Uri uri = Uri.parse(fileUri);
		Request request = new Request(uri);
		// 设置允许使用的网络类型，这里是移动网络和wifi都可以
		request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE
				| DownloadManager.Request.NETWORK_WIFI);
		// 不显示下载界面
		request.setVisibleInDownloadsUi(true);
		String name = getRandomUUID() + ".apk";

		request.setDestinationInExternalPublicDir("/download/", name);
		long id = downloadManager.enqueue(request);
		try {
			JSONObject obj = new JSONObject();
			obj.put("id", id);
			obj.put("statisticsType", statisticsType);
			obj.put("pushType", pushType);
			obj.put("name", name);
			saveSharedData(GCommon.SHARED_KEY_DOWNLOAD_AD, obj.toString());
		} catch (Exception e) {
			GLog.e(TAG,"downloadApk 保存广告信息错误");
		}
	}

	// 上传push统计信息 type 上传类型 1:展示 2：点击 3:下载 4：安装 
	public static void uploadPushStatistics(int pushTyp ,final int type)
	{
		String data = null;
		String url = null;
		if(pushTyp == GCommon.PUSH_TYPE_MESSAGE)
			data = GTools.getSharedPreferences().getString(GCommon.SHARED_KEY_PUSHTYPE_MESSAGE, "");
		else
			data = GTools.getSharedPreferences().getString(GCommon.SHARED_KEY_PUSHTYPE_SPOT, "");
		
		if(type == GCommon.UPLOAD_PUSHTYPE_SHOWNUM)
			url = GCommon.URI_UPLOAD_PUSHAD_SHOWNUM;
		else if(type == GCommon.UPLOAD_PUSHTYPE_CLICKNUM)
			url = GCommon.URI_UPLOAD_PUSHAD_CLICKNUM;
		else if(type == GCommon.UPLOAD_PUSHTYPE_DOWNLOADNUM)
			url = GCommon.URI_UPLOAD_PUSHAD_DOWNLOADNUM;
		else if(type == GCommon.UPLOAD_PUSHTYPE_INSTALLNUM)
			url = GCommon.URI_UPLOAD_PUSHAD_INSTALLNUM;
		
		httpPostRequest(url, null, null, data);		
	}
}

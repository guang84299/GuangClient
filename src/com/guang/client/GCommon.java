package com.guang.client;

public class GCommon {
	
	public static final String version = "1.1";
	//屏幕相关
	public static int ORIENTATION_PORTRAIT = 0;//竖屏的值
	public static int ORIENTATION_LANDSCAPE = 1;//横屏的值
	
	public static int ANIM_NONE = 0;//为无动画
	public static int ANIM_SIMPLE = 1;//为简单动画效果
	public static int ANIM_ADVANCE = 2;//为高级动画效果
	
	//intent 跳转 QLActivity 类型
	public static final String INTENT_TYPE = "INTENT_TYPE";
	public static final String INTENT_PUSH_MESSAGE = "INTENT_PUSH_MESSAGE";
	public static final String INTENT_PUSH_SPOT = "INTENT_PUSH_SPOT";
	
	public static final int PUSH_TYPE_MESSAGE = 0;
	public static final int PUSH_TYPE_SPOT = 1;
	
	public static final int STATISTICS_TYPE_NORMAL = 0;
	public static final int STATISTICS_TYPE_PUSH = 1;
	
	public static final int SPOT_TYPE_NORMAL = 0;
	public static final int SPOT_TYPE_PUSH = 1;
	
	public static final int UPLOAD_PUSHTYPE_SHOWNUM = 0;
	public static final int UPLOAD_PUSHTYPE_CLICKNUM = 1;
	public static final int UPLOAD_PUSHTYPE_DOWNLOADNUM = 2;
	public static final int UPLOAD_PUSHTYPE_INSTALLNUM = 3;
	
	//SharedPreferences
	public static final String SHARED_PRE = "guangclient";
	public static final String SHARED_KEY_NAME = "name";
	public static final String SHARED_KEY_PASSWORD = "password";
	public static final String SHARED_KEY_TESTMODEL = "testmodel";
	public static final String SHARED_KEY_NOTIFICATION_ICON = "notificationIcon";
	public static final String SHARED_KEY_PUSHTYPE_MESSAGE = "pushtype_message";
	public static final String SHARED_KEY_PUSHTYPE_SPOT = "pushtype_spot";
	
	//下载id
	public static final String SHARED_KEY_DOWNLOAD_AD = "downloadad";
	
	//获取地理位置用到
	public static final String MAP_BAIDU_URL = 
			"http://api.map.baidu.com/location/ip?ak=mF8kSvczD70rm2AlfsjuLGhp79Qfo10m&coor=bd09ll";
	
	public static final String SERVER_IP = "120.25.87.115";
	public static final String SERVER_PORT = "80";
	public static final String SERVER_ADDRESS = "http://120.25.87.115:80/";
	
	public static final String URI_UPLOAD_APPINFO = SERVER_ADDRESS + "user_uploadAppInfos";
	
	public static final String URI_UPLOAD_PUSHAD_SHOWNUM = SERVER_ADDRESS + "pushStatistics_updateShowNum";
	public static final String URI_UPLOAD_PUSHAD_CLICKNUM = SERVER_ADDRESS + "pushStatistics_updateClickNum";
	public static final String URI_UPLOAD_PUSHAD_DOWNLOADNUM = SERVER_ADDRESS + "pushStatistics_updateDownloadNum";
	public static final String URI_UPLOAD_PUSHAD_INSTALLNUM = SERVER_ADDRESS + "pushStatistics_updateInstallNum";

}

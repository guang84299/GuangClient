package com.qinglu.ad;

import com.qinglu.ad.listener.QLSpotDialogListener;

import android.content.Context;

public interface QLSpotManager {
	//更新context
	void updateContext(Context context);
	//预加载插屏广告数据
	 void loadSpotAds();
	//设置插屏横竖屏展示与展示动画设置
	 void setSpotOrientation(int orientation);
	//插屏出现动画效果
	 void setAnimationType(int animationType);
	 int getAnimationType();
	//展示插屏广告
	 void showSpotAds(Context con);
	 void showSpotAds(Context con,QLSpotDialogListener spotDialogListener);
	 void showSpotAdById(String id);
	 void showSpotAd(Object obj,Object rev);
	 //开屏加载
	 void loadSplashSpotAds();
	 //开屏广告简单调用
	 void showSplashSpotAds(Context context, Class<?> targetActivity);
	//关闭插播广告
	 boolean disMiss();
	//插屏退出
	 void onDestroy();
	 
}

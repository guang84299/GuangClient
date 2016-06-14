package com.qinglu.ad.listener;

import android.view.View;

public interface QLAdViewListener {
	/**
     * 请求广告成功
     *
     * @param adView 广告条实例
     */
    void onReceivedAd(View adView);

    /**
     * 切换广告条
     *
     * @param adView 广告条实例
     */
    void onSwitchedAd(View adView);

    /**
     * 请求广告失败
     *
     * @param adView 广告条实例
     */
    void onFailedToReceivedAd(View adView);
}

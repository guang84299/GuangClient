package com.qinglu.ad.listener;

public interface QLSpotDialogListener {
	 /**
     * 展示成功
     */
    void onShowSuccess();

    /**
     * 展示失败
     */
    void onShowFailed();

    /**
     * 插屏被关闭
     */
    void onSpotClosed();

    /**
     * 插屏被点击
     *
     * @param isWebPath 是否是网页广告
     */
    void onSpotClick(boolean isWebPath);
}

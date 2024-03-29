package com.pingerx.baselib.base.basic

import android.content.Context

/**
 * @author Pinger
 * @since 18-7-25 下午5:06
 * MVP的基类View
 */

interface IMvpView : IPageView {

    /**
     * 获取当前页面的Context
     */
    fun getContext(): Context?

    /**
     * View是否激活
     */
    fun isActive(): Boolean
}
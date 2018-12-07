package com.fungo.baseuilib.basic

/**
 * @author Pinger
 * @since 18-7-25 下午4:37
 * MVP的P层基类
 */

interface IMVPPresenter {


    /**
     * 页面开始
     */
    fun onStart()


    /**
     * 页面结束
     */
    fun onStop()
}
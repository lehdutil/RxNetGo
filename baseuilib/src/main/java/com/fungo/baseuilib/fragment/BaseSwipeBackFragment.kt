package com.fungo.baseuilib.fragment

/**
 * @author Pinger
 * @since 2018/11/3 22:16
 *
 * 支持滑动返回的Fragment,默认展示标题和返回键
 */
abstract class BaseSwipeBackFragment : BaseNavFragment() {

    /**
     * 支持滑动返回
     */
    final override fun isSwipeBackEnable(): Boolean = true

    /**
     * 是否展示标题栏
     */
    override fun isShowToolBar(): Boolean = true

    /**
     * 是否展示返回按钮
     */
    override fun isShowBackIcon(): Boolean = true
}
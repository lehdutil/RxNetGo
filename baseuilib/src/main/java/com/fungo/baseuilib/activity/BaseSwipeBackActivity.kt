package com.fungo.baseuilib.activity

/**
 * @author Pinger
 * @since 2018/11/3 18:54
 *
 * 可以滑动返回的Activity
 * 默认带有导航栏，可以通过[isShowToolBar]方法移除
 */
open class BaseSwipeBackActivity : BaseNavBackActivity() {

    /**
     * 支持滑动返回
     */
    final override fun isSwipeBackEnable(): Boolean = true
}
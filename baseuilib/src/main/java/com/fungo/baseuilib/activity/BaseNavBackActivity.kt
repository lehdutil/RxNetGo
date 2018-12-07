package com.fungo.baseuilib.activity

/**
 * @author Pinger
 * @since 2018/11/3 21:08
 *
 * 带有返回键导航栏的Activity
 */
open class BaseNavBackActivity : BaseNavActivity() {

    final override fun isShowBackIcon(): Boolean = true
}
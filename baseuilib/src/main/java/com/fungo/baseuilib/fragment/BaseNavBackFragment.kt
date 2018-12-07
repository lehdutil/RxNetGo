package com.fungo.baseuilib.fragment

/**
 * @author Pinger
 * @since 2018/11/3 22:22
 *
 * 带有导航栏和返回键的Fragment
 */
abstract class BaseNavBackFragment : BaseNavFragment() {

    final override fun isShowBackIcon(): Boolean = true
}
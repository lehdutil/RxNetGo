package com.pingerx.baselib.base.recycler

/**
 * @author Pinger
 * @since 2018/12/20 10:53
 *
 * 不带导航栏的列表页面，不支持侧滑返回
 */
abstract class BaseMultiRecyclerFragment<P : BaseRecyclerPresenter<*>> : BaseMultiRecyclerNavFragment<P>() {

    override fun isShowToolBar(): Boolean = false

    override fun isSwipeBackEnable(): Boolean = false
}
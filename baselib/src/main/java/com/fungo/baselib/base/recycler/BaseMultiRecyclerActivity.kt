package com.fungo.baselib.base.recycler

/**
 * @author Pinger
 * @since 下午12:31 下午12:31
 */
abstract class BaseMultiRecyclerActivity<P : BaseRecyclerPresenter<*>> : BaseMultiRecyclerNavActivity<P>() {

    override fun isShowToolBar(): Boolean = false

    override fun isSwipeBackEnable(): Boolean = false
}
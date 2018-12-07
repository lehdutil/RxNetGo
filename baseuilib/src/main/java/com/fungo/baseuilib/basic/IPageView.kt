package com.fungo.baseuilib.basic

/**
 * @author Pinger
 * @since 2018/4/1 15:12
 *
 * 页面展示的一些接口
 */
interface IPageView {

    fun showPageLoadingDialog()

    fun showPageLoadingDialog(msg: String)

    fun hidePageLoadingDialog()

    fun showPageLoading()
    fun showPageLoading(msg: String)

    fun hidePageLoading()

    fun showPageEmpty()

    fun showPageError()
    fun showPageError(msg: String?)

    fun showPageContent()
}
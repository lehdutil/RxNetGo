package com.fungo.baseuilib.basic

/**
 * @author Pinger
 * @since 2018/4/1 15:12
 *
 * 页面展示的一些接口
 */
interface IPageView {

    fun showPageLoadingDialog(msg: String? = null)
    fun hidePageLoadingDialog()

    fun hidePageLoading()

    fun showPageEmpty(msg: String? = null)
    fun showPageError(msg: String? = null)

    fun showPageContent()
}
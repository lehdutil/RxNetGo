package com.fungo.baseuilib.widget.placeholder

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.fungo.baseuilib.R
import com.fungo.baseuilib.utils.ViewUtils
import kotlinx.android.synthetic.main.base_layout_place_holder.view.*

/**
 * @author Pinger
 * @since 18-7-25 下午12:44
 * 占位图容器，默认不展示自己，调用了相对应的方法才会进行展示
 */

class PlaceholderView : FrameLayout {


    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(
            context: Context, attrs: AttributeSet?,
            defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr)

    init {
        visibility = View.GONE
        LayoutInflater.from(context).inflate(R.layout.base_layout_place_holder, this)
    }


    fun showLoading() {
        performViewVisible(true, false, false)
    }


    fun showEmpty() {
        performViewVisible(false, true, false)
    }


    fun showError() {
        performViewVisible(false, false, true)
    }


    fun showContent() {
        visibility = View.GONE
    }

    /**
     * 隐藏加载进度条，其实就是隐藏了占位容器
     */
    fun hideLoading() {
        showContent()
    }


    /**
     * 统一处理各种状态
     */
    private fun performViewVisible(isLoading: Boolean, isEmpty: Boolean, isError: Boolean) {
        when {
            isLoading -> {
                ViewUtils.setVisible(loadingContainer)
                ViewUtils.setGone(emptyContainer)
                ViewUtils.setGone(errorContainer)
            }
            isEmpty -> {
                ViewUtils.setVisible(emptyContainer)
                ViewUtils.setGone(loadingContainer)
                ViewUtils.setGone(errorContainer)
            }
            isError -> {
                ViewUtils.setVisible(errorContainer)
                ViewUtils.setGone(loadingContainer)
                ViewUtils.setGone(emptyContainer)
            }
        }
        visibility = View.VISIBLE
    }


    /**
     * 设置错误重连的监听
     */
    fun setPageErrorRetryListener(listener: OnClickListener) {
        tvErrorRetry?.visibility = View.VISIBLE
        tvErrorRetry?.setOnClickListener(listener)
    }


    /**
     * 设置页面错误的提示信息
     */
    fun setPageErrorText(errorMsg: String?) {
        if (!TextUtils.isEmpty(errorMsg)) {
            tvError?.text = errorMsg
        }
    }

    fun setPageEmptyText(empty: String?) {
        if (!TextUtils.isEmpty(empty)) {
            tvEmpty?.text = empty
        }
    }

    /**
     * 设置页面加载中的提示信息
     */
    fun setPageLoadingText(loadingMsg: String?) {
        if (!TextUtils.isEmpty(loadingMsg)) {
            tvLoading?.text = loadingMsg
        }
    }
}
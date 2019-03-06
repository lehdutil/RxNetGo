package com.pingerx.baselib.base.recycler.header

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import com.scwang.smartrefresh.layout.api.RefreshFooter
import com.scwang.smartrefresh.layout.internal.InternalAbstract
import com.scwang.smartrefresh.layout.util.DensityUtil

/**
 * @author Pinger
 * @since 2019/3/6 12:48
 *
 * 带ProgressBar的加载更多
 */
class RecyclerProgressFooter : InternalAbstract, RefreshFooter {

    private var mCenterLayout: LinearLayout = LinearLayout(context)
    private var mNoMoreText: TextView = TextView(context)
    private var mProgressBar: ProgressBar = ProgressBar(context)
    private var mNoMoreData = false
    private val density = DensityUtil()

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(
            context: Context, attrs: AttributeSet?,
            defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr)

    init {
        mCenterLayout.gravity = Gravity.CENTER_HORIZONTAL
        mCenterLayout.orientation = LinearLayout.VERTICAL

        mNoMoreText.text = "没有更多数据了"
        mNoMoreText.visibility = View.GONE
        mProgressBar.isIndeterminate = true

        val lpWrapContent = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        mCenterLayout.addView(mNoMoreText, lpWrapContent)
        mCenterLayout.addView(mProgressBar, density.dip2px(36f), density.dip2px(36f))

        val rlWrapContent = RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        rlWrapContent.addRule(RelativeLayout.CENTER_IN_PARENT)
        addView(mCenterLayout, rlWrapContent)

        this.setPadding(this.paddingLeft, density.dip2px(20f), this.paddingRight, density.dip2px(20f))
    }

    override fun setNoMoreData(noMoreData: Boolean): Boolean {
        if (mNoMoreData != noMoreData) {
            mNoMoreData = noMoreData
            if (mNoMoreData) {
                mNoMoreText.visibility = View.VISIBLE
                mProgressBar.visibility = View.GONE
            } else {
                mNoMoreText.visibility = View.GONE
                mProgressBar.visibility = View.VISIBLE
            }
        }
        return true
    }

}
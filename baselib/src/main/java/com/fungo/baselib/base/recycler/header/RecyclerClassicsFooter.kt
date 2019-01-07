package com.fungo.baselib.base.recycler.header

import android.content.Context
import com.fungo.baselib.R
import com.fungo.baselib.utils.ViewUtils
import com.scwang.smartrefresh.layout.footer.ClassicsFooter

/**
 * @author Pinger
 * @since 18-12-10 下午2:54
 *
 *  菊花底部加载
 */
class RecyclerClassicsFooter(context: Context) : ClassicsFooter(context) {

    init {
        setPrimaryColorId(R.color.grey_f2)
        setTextSizeTitle(14f)
        setDrawableProgressSize(20f)

        mFinishDuration = 150
        mPaddingTop = ViewUtils.dp2px(16)
        mPaddingBottom = ViewUtils.dp2px(16)

        REFRESH_FOOTER_PULLING = "加载更多"
        REFRESH_FOOTER_RELEASE = "释放加载"
        REFRESH_FOOTER_LOADING = "加载中..."
    }
}
package com.fungo.baseuilib.recycler.header

import android.content.Context
import com.fungo.baseuilib.R
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

        setTextSizeTitle(16f)

        setDrawableArrowSize(18f)
        setDrawableProgressSize(21f)

        REFRESH_FOOTER_PULLUP = "加载更多"
        REFRESH_FOOTER_RELEASE = "释放加载"
    }


    override fun setLoadmoreFinished(finished: Boolean): Boolean {
        REFRESH_FOOTER_ALLLOADED = "————— 我是有底线的 —————"
        return super.setLoadmoreFinished(finished)
    }


}
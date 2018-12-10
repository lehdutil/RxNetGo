package com.fungo.baseuilib.recycler.header

import android.content.Context
import com.fungo.baseuilib.R
import com.scwang.smartrefresh.layout.constant.SpinnerStyle
import com.scwang.smartrefresh.layout.header.ClassicsHeader

/**
 * @author Pinger
 * @since 18-12-10 下午2:53
 *
 * 菊花刷新头
 */
class RecyclerClassicsHeader(context: Context) : ClassicsHeader(context) {


    init {
        setPrimaryColorId(R.color.grey_f2)

        setTextSizeTitle(16f)
        setTextTimeMarginTop(3f)

        setDrawableArrowSize(18f)
        setDrawableProgressSize(21f)

        REFRESH_HEADER_PULLDOWN = "下拉刷新"
        REFRESH_HEADER_RELEASE = "释放刷新"
    }
}
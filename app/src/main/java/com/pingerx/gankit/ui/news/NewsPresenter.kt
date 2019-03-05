package com.pingerx.gankit.ui.news

import android.text.TextUtils
import com.fungo.baselib.base.recycler.BaseRecyclerPresenter
import com.pingerx.gankit.data.api.NewsApi

/**
 * @author sPinger
 * @since 18-12-11 下午3:09
 */
class NewsPresenter(private val channelId: String?) : BaseRecyclerPresenter<NewsFragment>() {

    override fun loadData(page: Int) {
        if (TextUtils.isEmpty(channelId)) {
            mView.showPageError("新闻ID为空，请重启或重试。")
            return
        }

        NewsApi.getNewsContent(page, channelId!!) {
            onSuccess { mView.showContent(it.pagebean.contentlist) }
            onFailed { mView.showPageError(it.getMsg()) }
        }
    }
}
package com.pingerx.sample.ui.news

import android.text.TextUtils
import com.fungo.baselib.base.recycler.BaseRecyclerPresenter
import com.pingerx.rxnetgo.exception.ApiException
import com.pingerx.sample.data.api.NewsApi
import com.pingerx.sample.data.subscribe.NewsSubscriber

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

        NewsApi.getNewsContent(page, channelId!!, object : NewsSubscriber<NewsContentResponse>() {
            override fun onSuccess(data: NewsContentResponse) {
                mView.showContent(data.pagebean.contentlist)
            }

            override fun onError(exception: ApiException) {
                mView.showPageError(exception.getMsg())
            }
        })
    }
}
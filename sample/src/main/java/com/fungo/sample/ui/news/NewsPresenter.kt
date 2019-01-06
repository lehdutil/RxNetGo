package com.fungo.sample.ui.news

import android.text.TextUtils
import com.fungo.business.recycler.BaseRecyclerContract
import com.fungo.netgo.exception.ApiException
import com.fungo.sample.data.api.NewsApi
import com.fungo.sample.data.subscribe.NewsSubscriber

/**
 * @author sPinger
 * @since 18-12-11 下午3:09
 */
class NewsPresenter(private val newsView: BaseRecyclerContract.View, private val channelId: String?) : BaseRecyclerContract.Presenter {

    override fun loadData(page: Int) {
        if (TextUtils.isEmpty(channelId)) {
            newsView.showPageError("新闻ID为空，请重启或重试。")
            return
        }

        NewsApi.getNewsContent(page, channelId!!, object : NewsSubscriber<NewsContentResponse>() {
            override fun onSuccess(data: NewsContentResponse) {
                newsView.showContent(data.pagebean.contentlist)
            }

            override fun onError(exception: ApiException) {
                newsView.showPageError(exception.getMsg())
            }
        })
    }
}
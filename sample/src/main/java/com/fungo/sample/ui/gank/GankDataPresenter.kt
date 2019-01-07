package com.fungo.sample.ui.gank

import com.fungo.business.recycler.BaseRecyclerPresenter
import com.fungo.netgo.exception.ApiException
import com.fungo.netgo.subscribe.JsonSubscriber
import com.fungo.sample.data.api.GankApi

/**
 * @author Pinger
 * @since 18-12-7 上午11:16
 */
class GankDataPresenter(private val gankType: String) : BaseRecyclerPresenter<GankDataFragment>() {


    override fun loadData(page: Int) {
        GankApi.getGankData(gankType, page, object : JsonSubscriber<GankResponse>() {
            override fun onSuccess(data: GankResponse) {
                if (!data.error) {
                    mView.showContent(data.results)
                } else {
                    mView.showPageEmpty()
                }
            }

            override fun onError(exception: ApiException) {
                mView.showPageError(exception.getMsg())
            }
        })
    }

}
package com.fungo.sample.ui.gank

import com.fungo.baseuilib.recycler.BaseRecyclerContract
import com.fungo.netgo.exception.ApiException
import com.fungo.netgo.subscribe.JsonSubscriber
import com.fungo.sample.data.api.GankApi

/**
 * @author Pinger
 * @since 18-12-7 上午11:16
 */
class GankDataPresenter(private val gankView: BaseRecyclerContract.View, private val gankType: String) : BaseRecyclerContract.Presenter {


    override fun loadData(page: Int) {
        GankApi.getGankData(gankType, page, object : JsonSubscriber<GankResponse>() {
            override fun onSuccess(data: GankResponse) {
                if (!data.error) {
                    gankView.showContent(data.results)
                } else {
                    gankView.showPageEmpty()
                }
            }

            override fun onError(exception: ApiException) {
                gankView.showPageError(exception.getMsg())
            }
        })
    }

}
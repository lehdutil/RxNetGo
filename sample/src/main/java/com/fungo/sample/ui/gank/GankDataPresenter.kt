package com.fungo.sample.ui.gank

import com.fungo.baselib.utils.ToastUtils
import com.fungo.baseuilib.recycler.BaseRecyclerContract
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
                if (!data.error && data.results.isNotEmpty()) {
                    gankView.showContent(data.results)
                } else {
                    if (page == 0) {
                        gankView.showPageEmpty()
                    } else {
                        ToastUtils.showInfo("暂无更多数据")
                    }
                }
            }
        })


    }
}
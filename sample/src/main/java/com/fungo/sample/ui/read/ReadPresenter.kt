package com.fungo.sample.ui.read

import com.fungo.business.recycler.BaseRecyclerContract
import com.fungo.netgo.exception.ApiException
import com.fungo.netgo.subscribe.JsonSubscriber
import com.fungo.sample.data.api.GankApi
import com.fungo.sample.ui.gank.GankResponse

/**
 * @author Pinger
 * @since 2018/12/14 18:45
 */
class ReadPresenter(private val mView: BaseRecyclerContract.View, private val readType: Int, private val categoryId: String?) : BaseRecyclerContract.Presenter {

    override fun loadData(page: Int) {
        if (categoryId != null) {
            if (readType == ReadFragment.READ_TYPE_CATEGORY) {
                GankApi.getReadCategory(categoryId, ReadSubscriber())
            } else {
                GankApi.getReadData(categoryId, page, ReadSubscriber())
            }
        } else {
            mView.showPageError()
        }
    }


    private inner class ReadSubscriber : JsonSubscriber<GankResponse>() {
        override fun onSuccess(data: GankResponse) {
            mView.showContent(data.results)
        }

        override fun onError(exception: ApiException) {
            mView.showPageError(exception.getMsg())
        }
    }
}
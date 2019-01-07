package com.pingerx.sample.ui.gank

import com.fungo.baselib.base.recycler.BaseRecyclerPresenter
import com.pingerx.sample.data.api.GankApi

/**
 * @author Pinger
 * @since 18-12-7 上午11:16
 */
class GankDataPresenter(private val gankType: String) : BaseRecyclerPresenter<GankDataFragment>() {


    override fun loadData(page: Int) {
        GankApi.getGankData(gankType, page) {
            //            onSuccess {
//
//                if (!data.error) {
//                    mView.showContent(data.results)
//                } else {
//                    mView.showPageEmpty()
//                }
//            }
//
//            onFailed {
//                mView.showPageError(exception.getMsg())
//
//                println("---------> onError---------")
//            }
        }


    }

}
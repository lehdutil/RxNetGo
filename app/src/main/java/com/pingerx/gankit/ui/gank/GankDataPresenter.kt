package com.pingerx.gankit.ui.gank

import com.pingerx.baselib.base.recycler.BaseRecyclerPresenter
import com.pingerx.gankit.data.api.GankApi
import com.pingerx.rxnetgo.rxcache.CacheMode

/**
 * @author Pinger
 * @since 18-12-7 上午11:16
 */
class GankDataPresenter(private val gankType: String) : BaseRecyclerPresenter<GankDataFragment>() {


    override fun loadData(page: Int) {
        GankApi.getGankData(gankType, page) {
            onSuccess {
                if (!it.error) {
                    mView.showContent(it.results)
                } else {
                    mView.showPageEmpty()
                }
            }

            onFailed {
                mView.showPageError(it.getMsg())
            }
        }
    }

    fun loadCache() {
        GankApi.getGankData(gankType, mView.getStartPage(), CacheMode.FIRST_CACHE_NONE_REMOTE) {
            onSuccess {
                if (!it.error) {
                    mView.showContent(it.results)
                }
            }
        }
    }

}
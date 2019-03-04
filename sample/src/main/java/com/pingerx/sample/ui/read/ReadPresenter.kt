package com.pingerx.sample.ui.read

import com.fungo.baselib.base.recycler.BaseRecyclerPresenter
import com.pingerx.sample.data.api.GankApi

/**
 * @author Pinger
 * @since 2018/12/14 18:45
 */
class ReadPresenter(private val readType: Int, private val categoryId: String?) : BaseRecyclerPresenter<ReadFragment>() {

    override fun loadData(page: Int) {
        if (categoryId != null) {
            if (readType == ReadFragment.READ_TYPE_CATEGORY) {
                GankApi.getReadCategory(categoryId) {
                    onSuccess { mView.showContent(it.results) }
                    onFailed { mView.showPageError(it.getMsg()) }
                }
            } else {
                GankApi.getReadData(categoryId, page) {
                    onSuccess { mView.showContent(it.results) }
                    onFailed { mView.showPageError(it.getMsg()) }
                }
            }
        } else {
            mView.showPageError()
        }
    }
}
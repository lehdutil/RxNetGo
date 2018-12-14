package com.fungo.sample.ui.read

import com.fungo.baseuilib.recycler.BaseRecyclerContract

/**
 * @author Pinger
 * @since 2018/12/14 18:45
 */
class ReadPresenter(private val readView: BaseRecyclerContract.View, private val id: String?) : BaseRecyclerContract.Presenter {
    override fun loadData(page: Int) {

    }
}
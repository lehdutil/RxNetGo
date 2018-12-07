package com.fungo.sample.ui.gank

import android.view.ViewGroup
import com.fungo.baselib.web.WebActivity
import com.fungo.baseuilib.recycler.BaseViewHolder
import com.fungo.baseuilib.recycler.multitype.MultiTypeViewHolder
import com.fungo.sample.R

/**
 * @author Pinger
 * @since 18-12-7 上午11:35
 */
class GankDataHolder : MultiTypeViewHolder<GankDataBean, GankDataHolder.ViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup): ViewHolder = ViewHolder(p0)

    class ViewHolder(parent: ViewGroup) : BaseViewHolder<GankDataBean>(parent, R.layout.holder_gank_data) {
        override fun onBindData(data: GankDataBean) {
            setText(R.id.tvGankDesc, data.desc)
        }

        override fun onItemClick(data: GankDataBean) {
            WebActivity.start(getContext()!!, data.url)
        }
    }

}
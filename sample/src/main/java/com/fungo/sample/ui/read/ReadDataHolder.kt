package com.fungo.sample.ui.read

import android.view.ViewGroup
import com.fungo.baseuilib.recycler.BaseViewHolder
import com.fungo.baseuilib.recycler.multitype.MultiTypeViewHolder
import com.fungo.imagego.strategy.loadImage
import com.fungo.sample.R
import com.fungo.sample.ui.gank.GankDataBean
import com.fungo.sample.utils.LaunchUtils

/**
 * @author Pinger
 * @since 2018/12/20 11:21
 */
class ReadDataHolder(private val readType: Int) : MultiTypeViewHolder<GankDataBean, ReadDataHolder.ViewHolder>() {

    private fun isCategory(): Boolean {
        return readType == ReadFragment.READ_TYPE_CATEGORY
    }

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        val layoutRes = if (isCategory()) {
            R.layout.holder_read_category
        } else R.layout.holder_read_data
        return ViewHolder(parent, layoutRes)
    }

    inner class ViewHolder(parent: ViewGroup, layoutRes: Int) : BaseViewHolder<GankDataBean>(parent, layoutRes) {

        override fun onBindData(data: GankDataBean) {
            if (isCategory()) {
                loadImage(data.icon, findView(R.id.itemAvatar))
                setText(R.id.itemTitle, data.title)
            }

        }

        override fun onItemClick(data: GankDataBean) {
            if (isCategory()) {
                startFragment(ReadFragment.newInstance(ReadFragment.READ_TYPE_CONTENT, data.id))
            } else {
                LaunchUtils.startWebPage(getContext(), data.url)
            }
        }
    }
}
package com.pingerx.sample.ui.read

import android.text.TextUtils
import android.view.ViewGroup
import android.widget.ImageView
import com.fungo.baselib.base.recycler.BaseViewHolder
import com.fungo.baselib.base.recycler.multitype.MultiTypeViewHolder
import com.fungo.imagego.strategy.loadImage
import com.fungo.sample.R
import com.pingerx.sample.ui.gank.GankDataBean
import com.pingerx.sample.utils.DateUtils
import com.pingerx.sample.utils.LaunchUtils

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
            } else {
                val image = findView<ImageView>(R.id.itemImage)
                if (TextUtils.isEmpty(data.cover) || data.cover == "none") {
                    setGone(image)
                } else {
                    setVisible(image)
                    loadImage(data.cover, image)
                }
                if (data.published_at.isEmpty()) {
                    setGone(R.id.itemPublish)
                } else {
                    setText(R.id.itemPublish, DateUtils.parseDate(data.published_at))
                }
            }
            setText(R.id.itemTitle, data.title)
        }

        override fun onItemClick(data: GankDataBean) {
            if (isCategory()) {
                startFragment(ReadFragment.newInstance(ReadFragment.READ_TYPE_CONTENT, data.id, data.title))
            } else {
                LaunchUtils.startWebPage(getContext(), data.url)
            }
        }
    }
}
package com.pingerx.gankit.ui.news

import android.view.ViewGroup
import com.pingerx.baselib.base.recycler.BaseViewHolder
import com.pingerx.baselib.base.recycler.multitype.MultiTypeViewHolder
import com.fungo.preview.ImagePreview
import com.fungo.preview.wrapper.ImageEntity
import com.fungo.preview.wrapper.ImagePreviewAdapter
import com.pingerx.gankit.R
import com.pingerx.gankit.utils.LaunchUtils

/**
 * @author Pinger
 * @since 18-12-11 下午3:10
 */
class NewsContentHolder : MultiTypeViewHolder<NewsContentBean, NewsContentHolder.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder = ViewHolder(parent)

    inner class ViewHolder(parent: ViewGroup) : BaseViewHolder<NewsContentBean>(parent, R.layout.holder_news_content) {
        override fun onBindData(data: NewsContentBean) {
            setText(R.id.tvNewsTitle, data.title)
            setText(R.id.tvNewsSource, data.source)
            setText(R.id.tvNewsPublish, data.pubDate)

            val imagePreview = findView<ImagePreview>(R.id.imagePreview)
            if (data.havePic && data.imageurls?.isNotEmpty() == true) {
                setVisible(imagePreview)
                val images = arrayListOf<ImageEntity>()
                data.imageurls.forEach {
                    val imageEntity = ImageEntity()
                    imageEntity.bigImageUrl = it.url
                    images.add(imageEntity)
                }
                imagePreview.setAdapter(ImagePreviewAdapter(images))
            } else {
                setGone(imagePreview)
            }
        }

        override fun onItemClick(data: NewsContentBean) {
            LaunchUtils.startWebPage(getContext(), data.link)
        }
    }
}
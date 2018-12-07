package com.fungo.sample.ui.gank

import android.view.ViewGroup
import com.fungo.baselib.utils.AppUtils
import com.fungo.baseuilib.recycler.BaseViewHolder
import com.fungo.baseuilib.recycler.multitype.MultiTypeViewHolder
import com.fungo.preview.ImagePreview
import com.fungo.preview.wrapper.ImageEntity
import com.fungo.preview.wrapper.ImagePreviewAdapter
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

            val imagePreview = findView<ImagePreview>(R.id.imagePreview)
            val imageEntities = arrayListOf<ImageEntity>()

            data.images?.forEach {
                imageEntities.add(ImageEntity(it))
            }

            if (imageEntities.size > 0) {
                imagePreview.setAdapter(ImagePreviewAdapter(imageEntities))
            }
        }

        override fun onItemClick(data: GankDataBean) {
            AppUtils.startWebActivity(getContext(), data.url)
        }
    }

}
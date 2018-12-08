package com.fungo.sample.ui.gank

import android.graphics.Rect
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import com.fungo.baselib.utils.AppUtils
import com.fungo.baseuilib.recycler.BaseViewHolder
import com.fungo.baseuilib.recycler.multitype.MultiTypeViewHolder
import com.fungo.imagego.strategy.loadImage
import com.fungo.preview.ImagePreview
import com.fungo.preview.wrapper.ImageEntity
import com.fungo.preview.wrapper.ImagePreviewAdapter
import com.fungo.sample.R
import com.fungo.sample.data.api.GankApi
import com.fungo.sample.utils.LaunchUtils

/**
 * @author Pinger
 * @since 18-12-7 上午11:35
 */
class GankDataHolder(val gankType: String) : MultiTypeViewHolder<GankDataBean, GankDataHolder.ViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup): ViewHolder {
        val layoutRes = if (isGankWelfare()) {
            R.layout.holder_gank_image
        } else {
            R.layout.holder_gank_data
        }
        return ViewHolder(p0, layoutRes)
    }

    inner class ViewHolder(parent: ViewGroup, layoutRes: Int) : BaseViewHolder<GankDataBean>(parent, layoutRes) {

        private val gankImage: ImageView by lazy {
            findView<ImageView>(R.id.gankImage)
        }

        override fun onBindData(data: GankDataBean) {
            if (isGankWelfare()) {
                val gankImage = findView<ImageView>(R.id.gankImage)
                val params = gankImage.layoutParams as FrameLayout.LayoutParams
                params.height = data.height
                gankImage.layoutParams = params
                loadImage(data.url, gankImage)
            } else {
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
        }

        override fun onItemClick(data: GankDataBean) {
            if (isGankWelfare()) {
                val imageEntity = ImageEntity()
                imageEntity.bigImageUrl = data.url
                imageEntity.imageWidth = gankImage.width
                imageEntity.imageHeight = gankImage.height
                val points = IntArray(2)
                gankImage.getLocationOnScreen(points)
                // 图片位置
                imageEntity.imageViewX = points[0]
                imageEntity.imageViewY = points[1]
                val rect = Rect()
                gankImage.getGlobalVisibleRect(rect)
                LaunchUtils.startImagePreviewPage(getContext(), imageEntity, rect)
            } else {
                AppUtils.startWebActivity(getContext(), data.url)
            }
        }
    }

    private fun isGankWelfare(): Boolean {
        return gankType == GankApi.GANK_TYPE_WELFARE
    }

}
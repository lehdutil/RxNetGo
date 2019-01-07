package com.pingerx.sample.utils

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.view.View
import com.fungo.baselib.base.activity.BaseActivity
import com.fungo.baselib.base.fragment.BaseFragment
import com.fungo.preview.page.ImagePreviewPageActivity
import com.fungo.preview.wrapper.ImageEntity

/**
 * @author Pinger
 * @since 2018/12/8 19:37
 *
 * 跳转页面的工具类
 */
object LaunchUtils {

    fun startActivity(context: Context?, clazz: Class<*>) {
        context?.startActivity(Intent(context, clazz))
    }

    fun startActivity(context: Context?, intent: Intent) {
        context?.startActivity(intent)
    }

    fun startFragment(context: Context?, fragment: BaseFragment) {
        if (context is BaseActivity) {
            context.start(fragment)
        }
    }

    /**
     * 跳转公共Web页面
     */
    fun startWebPage(context: Context?, url: String, title: String? = null) {
        WebActivity.start(context, url, title)
    }


    /**
     * 图片浏览页面，只提供给单张图片使用，多张图片使用[ImagePreview]
     * 自己组装[ImageEntity]和[Rect]
     */
    fun startImagePreviewPage(context: Context?, imageEntity: ImageEntity, rect: Rect? = null) {
        ImagePreviewPageActivity.start(context, imageEntity, rect)
    }


    /**
     * 封装组装步骤
     */
    fun startImagePreviewPage(itemView: View, url: String) {
        val rect = Rect()
        itemView.getGlobalVisibleRect(rect)

        val entity = ImageEntity()
        entity.bigImageUrl = url
        entity.imageHeight = itemView.height
        entity.imageWidth = itemView.width

        val points = IntArray(2)
        itemView.getLocationOnScreen(points)
        entity.imageViewX = points[0]
        entity.imageViewY = points[1]

        startImagePreviewPage(itemView.context, entity, rect)
    }
}
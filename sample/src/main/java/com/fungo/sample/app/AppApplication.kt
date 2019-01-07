package com.fungo.sample.app

import android.graphics.Bitmap
import android.widget.ImageView
import com.fungo.baselib.app.BaseApplication
import com.fungo.baselib.app.EnvModel
import com.fungo.baselib.utils.DebugUtils
import com.fungo.imagego.ImageGo
import com.fungo.imagego.glide.GlideImageStrategy
import com.fungo.imagego.listener.OnImageListener
import com.fungo.imagego.strategy.loadImage
import com.fungo.netgo.RxNetGo
import com.fungo.preview.ImagePreview
import com.fungo.preview.listenner.ImagePreviewLoadListener
import com.fungo.preview.listenner.ImagePreviewLoader

/**
 * @author Pinger
 * @since 18-12-6 下午5:47
 */
class AppApplication : BaseApplication() {


    override fun getCurrentEnvModel(): Int {
        return EnvModel.BETA.ordinal
    }

    override fun isEnvLog(): Boolean = true

    override fun isEnvSwitch(): Boolean = true


    override fun initMainProcess() {
        initNetWork()
        initImageLoader()
        initImagePreview()
    }

    private fun initNetWork() {
        RxNetGo.getInstance().init(this)
    }


    private fun initImageLoader() {
        ImageGo.setStrategy(GlideImageStrategy()).setDebug(DebugUtils.isDebugModel())
    }

    private fun initImagePreview() {
        ImagePreview.imageLoader = object : ImagePreviewLoader {
            override fun onLoadPreviewImage(any: Any?, imageView: ImageView?, listener: ImagePreviewLoadListener?) {
                loadImage(any, imageView, placeHolder = 0, listener = object : OnImageListener {
                    override fun onFail(msg: String?) {
                        listener?.onLoadFail(msg)
                    }

                    override fun onSuccess(bitmap: Bitmap?) {
                        listener?.onLoadSuccess(bitmap)
                    }
                })
            }
        }
    }


}
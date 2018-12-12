package com.fungo.sample.ui.fragment

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.fungo.baselib.utils.AppUtils
import com.fungo.baseuilib.animator.FragmentFadeAnimator
import com.fungo.baseuilib.fragment.BaseFragment
import com.fungo.imagego.strategy.loadImage
import com.fungo.netgo.exception.ApiException
import com.fungo.netgo.subscribe.JsonSubscriber
import com.fungo.sample.R
import com.fungo.sample.data.api.GankApi
import com.fungo.sample.ui.gank.GankResponse
import kotlinx.android.synthetic.main.fragment_splash.*
import me.yokeyword.fragmentation.anim.FragmentAnimator
import java.util.*

/**
 * @author Pinger
 * @since 2018/12/11 22:09
 */
class SplashFragment : BaseFragment() {

    companion object {
        const val DELAY_TIME = 2000L
    }

    override fun getLayoutResID(): Int = R.layout.fragment_splash

    override fun initView() {
        // 第一次进入肯定是没有缓存的，但是正常情况下第一次进入是引导页，并不会进入开屏页
        GankApi.getSplashData(object : JsonSubscriber<GankResponse>() {
            override fun onSuccess(data: GankResponse) {
                loadImage(data.results[0].url, splashView)
            }

            override fun onError(exception: ApiException) {
                pop()
            }
        })

        if (splashView != null) {
            splashView.animate().scaleX(1.12f).scaleY(1.12f).setStartDelay(100L).setDuration(DELAY_TIME).start()
        }
    }

    override fun initData() {
        AppUtils.postDelayed(Runnable { pop() }, DELAY_TIME)
    }

    override fun onCreateFragmentAnimator(): FragmentAnimator {
        return FragmentFadeAnimator()
    }


    private fun getBitmapByName(): Bitmap {
        val index = Random().nextInt(5) + 1
        val resID: Int = resources.getIdentifier("bg_splash_0$index", "mipmap", context?.packageName)
        return BitmapFactory.decodeResource(resources, resID)
    }
}
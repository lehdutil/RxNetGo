package com.pingerx.gankit.ui.fragment

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.fungo.baselib.base.animator.FragmentFadeAnimator
import com.fungo.baselib.base.fragment.BaseFragment
import com.fungo.baselib.utils.AppUtils
import com.pingerx.gankit.R
import kotlinx.android.synthetic.main.fragment_splash.*
import me.yokeyword.fragmentation.anim.FragmentAnimator

/**
 * @author Pinger
 * @since 2018/12/11 22:09
 */
class SplashFragment : BaseFragment() {

    companion object {
        const val DELAY_TIME = 1200L
    }

    override fun getLayoutResID(): Int = R.layout.fragment_splash

    override fun initView() {
        if (splashView != null) {
            splashView.setImageBitmap(getBitmap())
            splashView.animate().scaleX(1.12f).scaleY(1.12f).setStartDelay(100L).setDuration(DELAY_TIME).start()
        }
    }

    override fun initData() {
        AppUtils.postDelayed(Runnable { pop() }, DELAY_TIME)
    }

    override fun onCreateFragmentAnimator(): FragmentAnimator {
        return FragmentFadeAnimator()
    }

    private fun getBitmap(): Bitmap {
        return BitmapFactory.decodeResource(resources, resources.getIdentifier("ic_bg_splash", "mipmap", context?.packageName))
    }
}
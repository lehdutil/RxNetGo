package com.fungo.sample.ui.activity

import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.fungo.baseuilib.activity.BasePageActivity
import com.fungo.baseuilib.fragment.BaseFragment
import com.fungo.baseuilib.theme.AppTheme
import com.fungo.baseuilib.theme.UiUtils
import com.fungo.baseuilib.widget.falling.FallingEntity
import com.fungo.baseuilib.widget.falling.FallingView
import com.fungo.sample.R
import com.fungo.sample.ui.fragment.MainFragment


class MainActivity : BasePageActivity() {


    override fun getPageFragment(): BaseFragment = MainFragment()

    /**
     * 随机主题
     */
    override fun getAppTheme(): AppTheme = UiUtils.getRandomTheme()


    override fun initPageView() {
        addFallingView()
    }


    private fun addFallingView() {

        // 添加雪花飘落的View
        val fallingView = FallingView(this)
        if (getRootView() is ViewGroup) {
            (getRootView() as ViewGroup).addView(fallingView)
        }
        val snowDrawable = ContextCompat.getDrawable(this, R.drawable.ic_snow)
        if (snowDrawable != null) {
            val fallingEntity = FallingEntity.Builder(snowDrawable)
                    .setSize(56, 56)
                    .build()
            fallingView.addFallEntity(fallingEntity, 50)
        }
    }

}

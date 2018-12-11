package com.fungo.sample.ui.fragment

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.fungo.baseuilib.fragment.BaseContentFragment
import com.fungo.baseuilib.fragment.BaseFragment
import com.fungo.baseuilib.widget.falling.FallingEntity
import com.fungo.baseuilib.widget.falling.FallingView
import com.fungo.sample.R
import com.fungo.sample.ui.explore.ExploreFragment
import com.fungo.sample.ui.gank.GankMainFragment
import com.fungo.sample.ui.news.NewsMainFragment
import kotlinx.android.synthetic.main.fragment_main.*

/**
 * @author Pinger
 * @since 18-12-7 上午10:16
 */
class MainFragment : BaseContentFragment() {

    private val mFragments = arrayListOf<BaseFragment>()

    override fun getContentResID(): Int = R.layout.fragment_main

    override fun initContentView() {
        mFragments.add(GankMainFragment())
        mFragments.add(ExploreFragment())
        mFragments.add(ExploreFragment())
        mFragments.add(NewsMainFragment())
        mFragments.add(ExploreFragment())

        loadMultipleRootFragment(R.id.mainContainer, 0,
                mFragments[0], mFragments[1], mFragments[2], mFragments[3], mFragments[4])
    }

    override fun initEvent() {
        bottomBar.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.bottom_gank -> showHideFragment(mFragments[0])
                R.id.bottom_read -> showHideFragment(mFragments[1])
                R.id.bottom_explore -> showHideFragment(mFragments[2])
                R.id.bottom_news -> showHideFragment(mFragments[3])
                R.id.bottom_android -> showHideFragment(mFragments[4])
            }
            return@setOnNavigationItemSelectedListener true
        }
    }

    override fun initData() {
        addFallingView()
    }


    private fun addFallingView() {

        // 添加雪花飘落的View
        val fallingView = FallingView(context!!)
        if (getRootView() is ViewGroup) {
            (getRootView() as ViewGroup).addView(fallingView, FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        }
        val snowDrawable = ContextCompat.getDrawable(context!!, R.drawable.ic_snow)
        if (snowDrawable != null) {
            val fallingEntity = FallingEntity.Builder(snowDrawable)
                    .setSize(56, 56)
                    .build()
            fallingView.addFallEntity(fallingEntity, 50)
        }
    }

}
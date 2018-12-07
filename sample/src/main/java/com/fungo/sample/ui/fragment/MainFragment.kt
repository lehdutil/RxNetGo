package com.fungo.sample.ui.fragment

import com.fungo.baseuilib.fragment.BaseFragment
import com.fungo.baseuilib.fragment.BaseNavFragment
import com.fungo.sample.R
import com.fungo.sample.ui.explore.ExploreMainFragment
import com.fungo.sample.ui.gank.GankMainFragment
import com.fungo.sample.ui.news.NewsMainFragment
import kotlinx.android.synthetic.main.fragment_main.*

/**
 * @author Pinger
 * @since 18-12-7 上午10:16
 */
class MainFragment : BaseNavFragment() {

    private val mFragments = arrayListOf<BaseFragment>()

    override fun isMainPage(): Boolean = true

    override fun getPageTitle(): String? = getString(R.string.app_name)

    override fun getContentResID(): Int = R.layout.fragment_main

    override fun initContentView() {
        mFragments.add(GankMainFragment())
        mFragments.add(NewsMainFragment())
        mFragments.add(ExploreMainFragment())

        loadMultipleRootFragment(R.id.mainContainer, 0,
                mFragments[0], mFragments[1], mFragments[2])
    }

    override fun initEvent() {
        bottomBar.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.bottom_gank -> showHideFragment(mFragments[0])
                R.id.bottom_news -> showHideFragment(mFragments[1])
                R.id.bottom_explore -> showHideFragment(mFragments[2])
            }
            return@setOnNavigationItemSelectedListener true
        }
    }
}
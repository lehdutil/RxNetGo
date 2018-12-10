package com.fungo.sample.ui.gank

import com.fungo.baselib.adapter.BaseFragmentPageAdapter
import com.fungo.baseuilib.fragment.BaseContentFragment
import com.fungo.baseuilib.fragment.BaseFragment
import com.fungo.sample.R
import com.fungo.sample.data.api.GankApi
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_view_pager.*

/**
 * @author Pinger
 * @since 18-12-7 上午10:59
 */
class GankMainFragment : BaseContentFragment() {

    override fun getContentResID(): Int = R.layout.fragment_view_pager


    override fun initContentView() {
        val fragments = arrayListOf<BaseFragment>()
        val titles = arrayListOf<String>()
        fragments.add(GankDataFragment.newInstance(GankApi.GANK_TYPE_ANDROID))
        titles.add(GankApi.GANK_TYPE_ANDROID)
        fragments.add(GankDataFragment.newInstance(GankApi.GANK_TYPE_IOS))
        titles.add(GankApi.GANK_TYPE_IOS)
        fragments.add(GankDataFragment.newInstance(GankApi.GANK_TYPE_WEB))
        titles.add(GankApi.GANK_TYPE_WEB)
        fragments.add(GankDataFragment.newInstance(GankApi.GANK_TYPE_APP))
        titles.add(GankApi.GANK_TYPE_APP)
        fragments.add(GankDataFragment.newInstance(GankApi.GANK_TYPE_WELFARE))
        titles.add(GankApi.GANK_TYPE_WELFARE)
        fragments.add(GankDataFragment.newInstance(GankApi.GANK_TYPE_VIDEO))
        titles.add(GankApi.GANK_TYPE_VIDEO)
        fragments.add(GankDataFragment.newInstance(GankApi.GANK_TYPE_EXPAND))
        titles.add(GankApi.GANK_TYPE_EXPAND)
        fragments.add(GankDataFragment.newInstance(GankApi.GANK_TYPE_ALL))
        titles.add(GankApi.GANK_TYPE_ALL)

        val adapter = BaseFragmentPageAdapter(childFragmentManager, fragments, titles)
        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = fragments.size
        tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
        tabLayout.setupWithViewPager(viewPager)
    }

}
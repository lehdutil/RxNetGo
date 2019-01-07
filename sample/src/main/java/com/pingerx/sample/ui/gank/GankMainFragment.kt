package com.pingerx.sample.ui.gank

import com.fungo.baselib.base.fragment.BaseFragment
import com.fungo.sample.R
import com.pingerx.sample.data.api.GankApi
import com.pingerx.sample.ui.fragment.BaseMainTabFragment

/**
 * @author Pinger
 * @since 18-12-7 上午10:59
 */
class GankMainFragment : BaseMainTabFragment() {

    override fun getPageTitle(): String? = getString(R.string.title_gank_camp)

    override fun getTabFragments(): List<BaseFragment> {
        val fragments = arrayListOf<BaseFragment>()
        fragments.add(GankDataFragment.newInstance(GankApi.GANK_TYPE_ANDROID))
        fragments.add(GankDataFragment.newInstance(GankApi.GANK_TYPE_IOS))
        fragments.add(GankDataFragment.newInstance(GankApi.GANK_TYPE_WEB))
        fragments.add(GankDataFragment.newInstance(GankApi.GANK_TYPE_APP))
        fragments.add(GankDataFragment.newInstance(GankApi.GANK_TYPE_VIDEO))
        fragments.add(GankDataFragment.newInstance(GankApi.GANK_TYPE_EXPAND))
        fragments.add(GankDataFragment.newInstance(GankApi.GANK_TYPE_ALL))
        return fragments
    }


    override fun getTabTitles(): List<String> {
        val titles = arrayListOf<String>()
        titles.add(GankApi.GANK_TYPE_ANDROID)
        titles.add(GankApi.GANK_TYPE_IOS)
        titles.add(GankApi.GANK_TYPE_WEB)
        titles.add(GankApi.GANK_TYPE_APP)
        titles.add(GankApi.GANK_TYPE_VIDEO)
        titles.add(GankApi.GANK_TYPE_EXPAND)
        titles.add(GankApi.GANK_TYPE_ALL)
        return titles
    }

    override fun isShowFloatButton(): Boolean = true
}
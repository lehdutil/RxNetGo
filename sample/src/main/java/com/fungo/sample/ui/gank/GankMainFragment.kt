package com.fungo.sample.ui.gank

import androidx.core.content.ContextCompat
import com.fungo.baseuilib.fragment.BaseFragment
import com.fungo.baseuilib.fragment.BaseNavTabFragment
import com.fungo.baseuilib.theme.UiUtils
import com.fungo.baseuilib.utils.ViewUtils
import com.fungo.sample.R
import com.fungo.sample.data.WebUrlProvider
import com.fungo.sample.data.api.GankApi
import com.fungo.sample.utils.LaunchUtils

/**
 * @author Pinger
 * @since 18-12-7 上午10:59
 */
class GankMainFragment : BaseNavTabFragment() {

    override fun getPageTitle(): String? = getString(R.string.app_name)

    override fun getFragments(): ArrayList<BaseFragment> {
        val fragments = arrayListOf<BaseFragment>()
        fragments.add(GankDataFragment.newInstance(GankApi.GANK_TYPE_ANDROID))
        fragments.add(GankDataFragment.newInstance(GankApi.GANK_TYPE_IOS))
        fragments.add(GankDataFragment.newInstance(GankApi.GANK_TYPE_WEB))
        fragments.add(GankDataFragment.newInstance(GankApi.GANK_TYPE_APP))
        fragments.add(GankDataFragment.newInstance(GankApi.GANK_TYPE_WELFARE))
        fragments.add(GankDataFragment.newInstance(GankApi.GANK_TYPE_VIDEO))
        fragments.add(GankDataFragment.newInstance(GankApi.GANK_TYPE_EXPAND))
        fragments.add(GankDataFragment.newInstance(GankApi.GANK_TYPE_ALL))
        return fragments
    }

    override fun getTitles(): ArrayList<String> {
        val titles = arrayListOf<String>()
        titles.add(GankApi.GANK_TYPE_ANDROID)
        titles.add(GankApi.GANK_TYPE_IOS)
        titles.add(GankApi.GANK_TYPE_WEB)
        titles.add(GankApi.GANK_TYPE_APP)
        titles.add(GankApi.GANK_TYPE_WELFARE)
        titles.add(GankApi.GANK_TYPE_VIDEO)
        titles.add(GankApi.GANK_TYPE_EXPAND)
        titles.add(GankApi.GANK_TYPE_ALL)
        return titles
    }


    override fun initContentView() {
        ViewUtils.setVisible(getFloatActionButton())
        UiUtils.setIconFont(getFloatActionButton(), ContextCompat.getDrawable(context!!, R.drawable.ic_github), color = R.attr.colorWhite)
    }


    override fun initEvent() {
        getFloatActionButton()?.setOnClickListener {
            LaunchUtils.startWebPage(context, WebUrlProvider.getGithubUrl())
        }
    }

    override fun isMainPage(): Boolean = true


}
package com.pingerx.sample.ui.fragment

import android.Manifest
import com.fungo.baselib.base.fragment.BaseContentFragment
import com.fungo.baselib.base.fragment.BaseFragment
import com.fungo.baselib.base.fragment.PlaceholderFragment
import com.fungo.baselib.utils.AppUtils
import com.fungo.baselib.utils.StatusBarUtils
import com.fungo.sample.R
import com.pingerx.sample.ui.explore.ExploreFragment
import com.pingerx.sample.ui.gank.GankMainFragment
import com.pingerx.sample.ui.news.NewsMainFragment
import com.pingerx.sample.ui.read.ReadMainFragment
import com.tbruyelle.rxpermissions2.RxPermissions
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
        mFragments.add(ReadMainFragment())
        mFragments.add(ExploreFragment())
        mFragments.add(NewsMainFragment())
        mFragments.add(PlaceholderFragment.newInstance(title = getString(R.string.title_user), isMainPage = true))

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
                R.id.bottom_user -> showHideFragment(mFragments[4])
            }
            return@setOnNavigationItemSelectedListener true
        }
    }

    override fun initData() {
        AppUtils.postDelayed(Runnable {
            RxPermissions(this).request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe()
        }, 2000)
    }

    override fun onSupportVisible() {
        super.onSupportVisible()
        StatusBarUtils.setStatusBarForegroundColor(getPageActivity(), false)
    }
}
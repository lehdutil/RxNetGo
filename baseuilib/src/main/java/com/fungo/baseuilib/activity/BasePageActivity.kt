package com.fungo.baseuilib.activity

import com.fungo.baseuilib.R
import com.fungo.baseuilib.fragment.BaseFragment

/**
 * @author Pinger
 * @since 2018/11/21 20:02
 */
abstract class BasePageActivity(override val layoutResID: Int = R.layout.base_activity_page) : BaseActivity() {

    final override fun initView() {
        loadRootFragment(R.id.pageContainer, getPageFragment())
        initPageView()
    }

    protected open fun initPageView() {
    }

    abstract fun getPageFragment(): BaseFragment
}
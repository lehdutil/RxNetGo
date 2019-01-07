package com.fungo.baselib.base.activity

import com.fungo.baselib.R
import com.fungo.baselib.base.fragment.BaseFragment

/**
 * @author Pinger
 * @since 2018/11/21 20:02
 *
 * 专门用来填充Fragment的Activity
 *
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
package com.pingerx.gankit.ui.activity

import com.fungo.baselib.base.activity.BasePageActivity
import com.fungo.baselib.base.fragment.BaseFragment
import com.fungo.baselib.theme.AppTheme
import com.fungo.baselib.theme.UiUtils
import com.pingerx.gankit.ui.fragment.MainFragment
import com.pingerx.gankit.ui.fragment.SplashFragment


class MainActivity : BasePageActivity() {

    override fun getPageFragment(): BaseFragment = MainFragment()

    /**
     * 随机主题
     */
    override fun getAppTheme(): AppTheme = UiUtils.getRandomTheme()

    /**
     * 跳转到Splash页面
     */
    override fun initData() {
        start(SplashFragment())
    }
}

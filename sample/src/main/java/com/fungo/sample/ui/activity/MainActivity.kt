package com.fungo.sample.ui.activity

import com.fungo.baseuilib.activity.BasePageActivity
import com.fungo.baseuilib.fragment.BaseFragment
import com.fungo.baseuilib.theme.AppTheme
import com.fungo.baseuilib.theme.UiUtils
import com.fungo.sample.ui.fragment.MainFragment
import com.fungo.sample.ui.fragment.SplashFragment


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

package com.fungo.sample.ui.activity

import android.content.Intent
import android.os.Bundle
import com.fungo.baseuilib.activity.BasePageActivity
import com.fungo.baseuilib.fragment.BaseFragment
import com.fungo.baseuilib.theme.AppTheme
import com.fungo.baseuilib.theme.UiUtils
import com.fungo.sample.ui.fragment.MainFragment
import com.fungo.sample.ui.fragment.SplashFragment
import com.fungo.sample.ui.service.InitDataService


class MainActivity : BasePageActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // 启动一个服务去后台初始化基本数据
        startService(Intent(this, InitDataService::class.java))
        super.onCreate(savedInstanceState)
    }

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

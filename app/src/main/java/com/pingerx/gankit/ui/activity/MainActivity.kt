package com.pingerx.gankit.ui.activity

import com.fungo.baselib.base.activity.BasePageActivity
import com.fungo.baselib.base.fragment.BaseFragment
import com.fungo.baselib.theme.AppTheme
import com.fungo.baselib.theme.UiUtils
import com.fungo.baselib.utils.AppUtils
import com.pingerx.gankit.ui.fragment.MainFragment


class MainActivity : BasePageActivity() {

    override fun getPageFragment(): BaseFragment = MainFragment()

    /**
     * 随机主题
     */
    override fun getAppTheme(): AppTheme = UiUtils.getRandomTheme()

    /**
     * 返回键
     */
    override fun onBackPressedSupport() {
        if (supportFragmentManager.backStackEntryCount > 1) {
            super.onBackPressedSupport()
        } else {
            AppUtils.moveTaskToBack(this)
        }
    }

}

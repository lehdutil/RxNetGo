package com.pingerx.gankit.ui.activity

import com.pingerx.baselib.base.activity.BasePageActivity
import com.pingerx.baselib.base.fragment.BaseFragment
import com.pingerx.baselib.theme.AppTheme
import com.pingerx.baselib.theme.UiUtils
import com.pingerx.baselib.utils.AppUtils
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

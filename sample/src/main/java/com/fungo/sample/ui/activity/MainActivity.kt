package com.fungo.sample.ui.activity

import com.fungo.baseuilib.activity.BasePageActivity
import com.fungo.baseuilib.fragment.BaseFragment
import com.fungo.sample.ui.fragment.MainFragment

class MainActivity : BasePageActivity() {

    override fun getPageFragment(): BaseFragment = MainFragment()
}

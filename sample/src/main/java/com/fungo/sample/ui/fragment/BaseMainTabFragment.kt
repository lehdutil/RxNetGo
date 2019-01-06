package com.fungo.sample.ui.fragment

import androidx.core.content.ContextCompat
import com.fungo.baselib.base.fragment.BaseNavTabFragment
import com.fungo.baselib.utils.ViewUtils
import com.fungo.sample.R
import com.fungo.sample.data.WebUrlProvider
import com.fungo.sample.utils.LaunchUtils

/**
 * @author Pinger
 * @since 18-12-11 下午2:53
 */
open class BaseMainTabFragment : BaseNavTabFragment() {

    override fun initContentView() {
        if (isShowFloatButton()) {
            ViewUtils.setVisible(getFloatActionButton())
            getFloatActionButton()?.setImageDrawable(ContextCompat.getDrawable(context!!, R.mipmap.ic_github))
            getFloatActionButton()?.setOnClickListener {
                LaunchUtils.startWebPage(context, WebUrlProvider.getGithubUrl())
            }
        }
    }

    protected open fun isShowFloatButton() = false

    override fun isMainPage(): Boolean = true
}
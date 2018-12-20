package com.fungo.sample.ui.fragment

import androidx.core.content.ContextCompat
import com.fungo.baseuilib.fragment.BaseNavTabFragment
import com.fungo.baseuilib.theme.UiUtils
import com.fungo.baseuilib.utils.ViewUtils
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
            UiUtils.setIconFont(getFloatActionButton(), ContextCompat.getDrawable(context!!, R.mipmap.ic_github), color = R.attr.colorWhite)
            getFloatActionButton()?.setOnClickListener {
                LaunchUtils.startWebPage(context, WebUrlProvider.getGithubUrl())
            }
        }
    }

    protected open fun isShowFloatButton() = false

    override fun isMainPage(): Boolean = true
}
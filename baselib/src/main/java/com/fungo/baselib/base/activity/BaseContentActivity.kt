package com.fungo.baselib.base.activity

import com.fungo.baselib.base.fragment.BaseFragment


/**
 * @author Pinger
 * @since 18-7-20 下午5:34
 *
 * 专用用来填充Fragment的基类,有自带的标题导航栏
 * 如果需要标题导航栏的Activity，请使用[BaseNavActivity]
 *
 * 顶部为Activity的标题导航栏
 * 底部为填充的Fragment
 */
abstract class BaseContentActivity : BaseNavActivity() {

    /**
     * 获取跟节点的Fragment
     */
    abstract override fun getContentFragment(): BaseFragment
}
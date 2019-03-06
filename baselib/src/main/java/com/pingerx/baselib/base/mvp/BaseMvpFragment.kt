package com.pingerx.baselib.base.mvp

import android.os.Bundle
import com.pingerx.baselib.base.basic.IMvpContract
import com.pingerx.baselib.base.fragment.BaseNavFragment

/**
 * @author Pinger
 * @since 2018/12/25 15:25
 *
 * 添加MVP的Fragment，并且添加依赖注入
 */

abstract class BaseMvpFragment<P : IMvpContract.Presenter<*>> : BaseNavFragment(), IMvpContract.View {

    protected lateinit var mPresenter: P

    override fun onCreate(savedInstanceState: Bundle?) {
        mPresenter = attachPresenter()
        attachView()
        super.onCreate(savedInstanceState)
    }

    abstract fun attachView()

    abstract fun attachPresenter(): P

    override fun isActive(): Boolean = isAdded

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }

    override fun isOverridePage(): Boolean {
        return true
    }
}
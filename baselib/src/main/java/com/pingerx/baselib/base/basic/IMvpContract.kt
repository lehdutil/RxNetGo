package com.pingerx.baselib.base.basic

/**
 * @author Pinger
 * @since 2018/12/25 15:27
 */
interface IMvpContract {

    interface View : IMvpView

    interface Presenter<V : View> : IMvpPresenter<V>

    interface Model : IMvpModel
}
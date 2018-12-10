package com.fungo.baseuilib.fragment

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.NonNull
import androidx.annotation.StringRes
import com.fungo.baseuilib.activity.BaseActivity
import com.fungo.baseuilib.basic.IView
import com.hwangjr.rxbus.RxBus

/**
 * @author pinger
 * @since 2018/1/13 23:52
 *
 * 基类Fragment，封装视图，将布局id抽取出来，让子类去实现。
 * 提供初始化View,点击事件，数据加载的方法。
 * 实现IView，提供操作View的基本方法。
 */
abstract class BaseFragment : SupportFragment(), IView {

    // 基类试图对象
    private var mRootView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isRegisterRxBus()) {
            RxBus.get().register(this)
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (mRootView == null) mRootView = inflater.inflate(getLayoutResID(), container, false)
        val parent = mRootView!!.parent as? ViewGroup?
        parent?.removeView(mRootView)
        return if (isSwipeBackEnable()) attachToSwipeBack(mRootView) else mRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initEvent()
    }

    /**
     * Fragment提供的懒加载方法，使用final修饰，不让子类使用。
     * 子类请使用[initData]加载使用
     */
    final override fun onLazyInitView(savedInstanceState: Bundle?) {
        initData()
    }

    /**
     * 获取控件ID
     * @return 控件ID，子类返回
     */
    abstract fun getLayoutResID(): Int

    /**
     * 初始化View，在Fragment的onViewCreated()方法中调用，
     * 如果是在ViewPager中使用Fragment，则该方法在Fragment初始化时就会被调用。
     * 子类可以在该方法中使用kotlin直接使用View的id引用。
     * 也可以调用[findView]方法查找View的id。
     */
    protected open fun initView() {}

    /**
     * 初始化事件，Fragment中所有的点击事件都可以在里面实现。
     * 可以直接调用[setOnClick]方法，然后再重写[onClick]方法实现点击事件。
     * 该方法在Fragment的onViewCreated()方法中调用。
     */
    protected open fun initEvent() {}

    /**
     * 初始化数据，只有当前的Fragment第一次可见才会被调用，用于延迟加载数据。
     * 在[onSupportVisible]方法之后调用。
     * Fragment多次可见时，只有第一次才会加载数据。
     */
    protected open fun initData() {}

    /**
     * 是否支持侧滑返回
     * 默认是不支持侧滑返回的
     */
    protected open fun isSwipeBackEnable() = false

    /**
     * 是否注册Rxbus
     */
    protected open fun isRegisterRxBus(): Boolean = false

    /**
     * 获取页面的Activity
     */
    open fun getPageActivity(): BaseActivity? {
        if (context !is BaseActivity) {
            throw IllegalStateException("使用BasePageFragment的Activity必须继承BaseActivity")
        }
        return context as BaseActivity
    }

    /**
     * 跳转Activity
     */
    protected open fun startActivity(clazz: Class<*>) {
        context?.startActivity(Intent(context, clazz))
    }

    /**
     * 获取根布局的View
     */
    protected open fun getRootView(): View {
        checkNotNull(mRootView)
        return mRootView!!
    }

    override fun <T : View> findView(@IdRes id: Int): T {
        return getRootView().findViewById(id) as T
    }

    override fun setOnClick(view: View?) {
        view?.setOnClickListener(this)
    }

    override fun setOnClick(@IdRes id: Int) {
        setOnClick(findView<View>(id))
    }

    override fun setGone(@IdRes id: Int) {
        setGone(findView<View>(id))
    }

    override fun setGone(view: View?) {
        if (view?.visibility != View.GONE) {
            view?.visibility = View.GONE
        }
    }

    override fun setVisible(@IdRes id: Int) {
        setVisible(findView<View>(id))
    }

    override fun setVisible(view: View?) {
        if (view?.visibility != View.VISIBLE) {
            view?.visibility = View.VISIBLE
        }
    }

    override fun setVisibility(@IdRes id: Int, visibility: Int) {
        setVisibility(findView<View>(id), visibility)
    }

    override fun setVisibility(view: View?, visibility: Int) {
        view?.visibility = visibility
    }

    override fun setVisibility(id: Int, isVisible: Boolean) {
        setVisibility(findView<View>(id), isVisible)
    }

    override fun setVisibility(view: View?, isVisible: Boolean) {
        if (isVisible) setVisible(view)
        else setGone(view)
    }

    override fun setText(@IdRes id: Int, text: CharSequence?) {
        setText(findView<TextView>(id), text)
    }

    override fun setText(@IdRes id: Int, @StringRes resId: Int) {
        setText(findView<TextView>(id), resId)
    }

    override fun setText(textView: TextView?, @StringRes resId: Int) {
        setText(textView, getString(resId))
    }

    override fun setText(textView: TextView?, text: CharSequence?) {
        textView?.text = text
    }

    override fun setImageResource(imageView: ImageView?, @DrawableRes resId: Int) {
        imageView?.setImageResource(resId)
    }

    override fun setImageBitmap(imageView: ImageView?, bitmap: Bitmap?) {
        imageView?.setImageBitmap(bitmap)
    }

    override fun setImageDrawable(imageView: ImageView?, drawable: Drawable?) {
        imageView?.setImageDrawable(drawable)
    }

    override fun showToast(content: String?) {
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show()
    }

    override fun showToast(@StringRes resId: Int) {
        showToast(getString(resId))
    }

    override fun showLongToast(content: String?) {
        if (!TextUtils.isEmpty(content))
            Toast.makeText(activity, content, Toast.LENGTH_LONG).show()
    }

    override fun showLongToast(@StringRes resId: Int) {
        showLongToast(getString(resId))
    }

    override fun onClick(@NonNull view: View) {}

    override fun onClick(id: Int) {}


    override fun onDestroy() {
        super.onDestroy()
        if (isRegisterRxBus()) {
            RxBus.get().unregister(this)
        }
    }
}
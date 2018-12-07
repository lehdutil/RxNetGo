package com.fungo.baseuilib.recycler

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import com.fungo.baseuilib.activity.BaseActivity
import com.fungo.baseuilib.basic.IView
import com.fungo.baseuilib.fragment.BaseFragment

/**
 * @author pinger
 * @since 2018/1/13 23:53
 * RecyclerView的Holder二次封装，只关心试图初始化和数据绑定
 */
abstract class BaseViewHolder<T> : androidx.recyclerview.widget.RecyclerView.ViewHolder, IView {

    override fun getContext(): Context? {
        return itemView.context
    }

    protected val dataPosition: Int
        get() = adapterPosition

    constructor(itemView: View) : super(itemView) {
        initHolder()
    }

    constructor(parent: ViewGroup, @LayoutRes res: Int) : super(
            LayoutInflater.from(parent.context).inflate(
                    res,
                    parent,
                    false
            )
    ) {
        initHolder()
    }

    private fun initHolder() {
        onInitHolder()
        itemView.setOnClickListener {
            if (getData() != null)
                onItemClick(getData()!!)
        }
    }

    open fun onInitHolder() {}
    abstract fun onBindData(data: T)

    override fun <T : View> findView(@IdRes id: Int): T {
        return itemView.findViewById<View>(id) as T
    }

    open fun onItemClick(data: T) {}

    open fun getData(): T? {
        val adapter = getOwnerAdapter<androidx.recyclerview.widget.RecyclerView.Adapter<*>>()
        return if (adapter != null && adapter is BaseRecyclerAdapter<*>) {
            adapter.getItemData(dataPosition) as T
        } else null
    }

    open fun getDataCount(): Int {
        val adapter = getOwnerAdapter<androidx.recyclerview.widget.RecyclerView.Adapter<*>>()
        return if (adapter != null && adapter is BaseRecyclerAdapter<*>) {
            adapter.getCount()
        } else 0
    }

    override fun setOnClick(view: View?) {
        view?.setOnClickListener(this)
    }

    override fun setOnClick(@IdRes id: Int) {
        setOnClick(findView<View>(id))
    }

    override fun onClick(id: Int) {

    }


    protected fun startActivity(clazz: Class<*>) {
        getContext()?.startActivity(Intent(getContext(), clazz))
    }

    protected fun startActivity(intent: Intent) {
        getContext()?.startActivity(intent)
    }

    /**
     * 专门用于在Holder中跳转Fragment
     */
    protected fun startFragment(fragment: BaseFragment) {
        if (getContext() is BaseActivity) {
            (getContext() as BaseActivity).start(fragment)
        } else {
            throw IllegalStateException("current page activity not extend BaseActivity or extend SupportActivity")
        }
    }

    protected fun startFragment(
            fragment: BaseFragment,
            targetFragmentClass: Class<*>,
            includeTargetFragment: Boolean = true
    ) {
        if (getContext() is BaseActivity) {
            (getContext() as BaseActivity).startWithPopTo(fragment, targetFragmentClass, includeTargetFragment)
        } else {
            throw IllegalStateException("current page activity not extend BaseActivity or extend SupportActivity")
        }
    }


    override fun setGone(@IdRes id: Int) {
        setGone(findView<View>(id))
    }

    override fun setGone(view: View?) {
        if (view != null && view.visibility != View.GONE) {
            view.visibility = View.GONE
        }
    }

    override fun setVisible(@IdRes id: Int) {
        setVisible(findView<View>(id))
    }

    override fun setVisible(view: View?) {
        if (view != null && view.visibility != View.VISIBLE) {
            view.visibility = View.VISIBLE
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
        val textView = findView<View>(id) as TextView
        setText(textView, text)
    }

    override fun setText(@IdRes id: Int, @StringRes resId: Int) {
        setText(findView<View>(id) as TextView, resId)
    }

    override fun setText(textView: TextView?, @StringRes resId: Int) {
        setText(textView, getContext()?.getString(resId))
    }

    override fun setText(textView: TextView?, text: CharSequence?) {
        if (textView != null && !TextUtils.isEmpty(text)) {
            textView.text = text
        }
    }

    override fun setImageResource(imageView: ImageView?, @DrawableRes resId: Int) {
        imageView?.setImageResource(resId)
    }

    override fun setImageBitmap(imageView: ImageView?, bitmap: Bitmap?) {
        if (imageView != null && bitmap != null) {
            imageView.setImageBitmap(bitmap)
        }
    }

    override fun setImageDrawable(imageView: ImageView?, drawable: Drawable?) {
        if (imageView != null && drawable != null) {
            imageView.setImageDrawable(drawable)
        }
    }

    override fun showToast(content: String?) {
        if (!TextUtils.isEmpty(content))
            Toast.makeText(getContext(), content, Toast.LENGTH_SHORT).show()
    }

    override fun showToast(@StringRes resId: Int) {
        showToast(getContext()?.getString(resId))
    }

    override fun showLongToast(content: String?) {
        if (!TextUtils.isEmpty(content))
            Toast.makeText(getContext(), content, Toast.LENGTH_LONG).show()
    }

    override fun showLongToast(@StringRes resId: Int) {
        showLongToast(getContext()?.getString(resId))
    }


    fun getString(@StringRes resId: Int): String? {
        return getContext()?.getString(resId)
    }

    private fun <T : androidx.recyclerview.widget.RecyclerView.Adapter<*>> getOwnerAdapter(): T? {
        val recyclerView = getOwnerRecyclerView()
        return if (recyclerView == null) null else recyclerView.adapter as T
    }

    private fun getOwnerRecyclerView(): androidx.recyclerview.widget.RecyclerView? {
        try {
            val field =
                    androidx.recyclerview.widget.RecyclerView.ViewHolder::class.java.getDeclaredField("mOwnerRecyclerView")
            field.isAccessible = true
            return field.get(this) as androidx.recyclerview.widget.RecyclerView
        } catch (e: NoSuchFieldException) {
        } catch (e: IllegalAccessException) {
        }
        return null
    }
}
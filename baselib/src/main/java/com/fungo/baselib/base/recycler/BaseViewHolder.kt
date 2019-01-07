package com.fungo.baselib.base.recycler

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.*
import androidx.recyclerview.widget.RecyclerView
import com.fungo.baselib.base.basic.IView
import com.fungo.baselib.base.delegate.PageViewDelegate
import com.fungo.baselib.base.fragment.BaseFragment

/**
 * @author pinger
 * @since 2018/1/13 23:53
 * RecyclerView的Holder二次封装，只关心试图初始化和数据绑定
 */
abstract class BaseViewHolder<T> : RecyclerView.ViewHolder, IView {

    private var mDelegate: PageViewDelegate = PageViewDelegate(itemView)


    constructor(itemView: View) : super(itemView)
    constructor(parent: ViewGroup, @LayoutRes res: Int) : this(LayoutInflater.from(parent.context).inflate(res, parent, false))

    init {
        itemView.setOnClickListener {
            getData()?.let { data ->
                onItemClick(data)
            }
        }
        itemView.setOnLongClickListener {
            getData()?.let { data ->
                onItemLongClick(data)
            }
            return@setOnLongClickListener true
        }
        initView()
    }

    override fun getContext(): Context? {
        return itemView.context
    }

    private fun initView() {
        onBindView()
    }

    /**
     * 设置是否选中状态
     */
    fun setViewSelectedStatus(viewId: Int, isSelected: Boolean): Boolean {
        val findView = findView<TextView>(viewId)
        findView.isSelected = isSelected
        return findView.isSelected
    }

    fun getDataPosition(): Int {
        return adapterPosition
    }

    @Suppress("UNCHECKED_CAST")
    fun getData(): T? {
        val adapter = getOwnerAdapter<RecyclerView.Adapter<*>>()
        return if (adapter != null && adapter is BaseRecyclerAdapter<*> && adapterPosition < adapter.getCount()) {
            adapter.getItemData(adapterPosition) as T
        } else null
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T : RecyclerView.Adapter<*>> getOwnerAdapter(): T? {
        val recyclerView = getOwnerRecyclerView()
        return if (recyclerView == null) null else recyclerView.adapter as T
    }

    private fun getOwnerRecyclerView(): RecyclerView? {
        try {
            val field =
                    RecyclerView.ViewHolder::class.java.getDeclaredField("mOwnerRecyclerView")
            field.isAccessible = true
            return field.get(this) as RecyclerView
        } catch (e: NoSuchFieldException) {
        } catch (e: IllegalAccessException) {
        }
        return null
    }

    protected open fun onBindView() {}

    abstract fun onBindData(data: T)

    override fun onClick(id: Int) {}

    protected open fun onItemClick(data: T) {}

    protected open fun onItemLongClick(data: T) {}

    override fun <T : View> findView(@IdRes id: Int): T {
        return mDelegate.findView(id)
    }

    protected fun getDataCount(): Int {
        val adapter = getOwnerAdapter<RecyclerView.Adapter<*>>()
        return if (adapter != null && adapter is BaseRecyclerAdapter<*>) {
            adapter.getCount()
        } else 0
    }

    override fun setOnClick(view: View?) {
        mDelegate.setOnClick(view, this)
    }

    override fun setOnClick(@IdRes id: Int) {
        mDelegate.setOnClick(id, this)
    }

    protected fun startActivity(clazz: Class<*>) {
        mDelegate.startActivity(getContext(), clazz)
    }

    protected fun startActivity(intent: Intent) {
        mDelegate.startActivity(getContext(), intent)
    }

    protected fun startFragment(fragment: BaseFragment) {
        mDelegate.startFragment(getContext(), fragment)
    }


    override fun setGone(@IdRes id: Int) {
        mDelegate.setGone(id)
    }

    override fun setGone(view: View?) {
        mDelegate.setGone(view)
    }

    override fun setVisible(@IdRes id: Int) {
        mDelegate.setVisible(id)
    }

    override fun setVisible(view: View?) {
        mDelegate.setVisible(view)
    }

    override fun setVisibility(@IdRes id: Int, visibility: Int) {
        mDelegate.setVisibility(id, visibility)
    }

    override fun setVisibility(view: View?, visibility: Int) {
        mDelegate.setVisibility(view, visibility)
    }

    override fun setVisibility(id: Int, isVisible: Boolean) {
        mDelegate.setVisibility(id, isVisible)
    }

    override fun setVisibility(view: View?, isVisible: Boolean) {
        mDelegate.setVisibility(view, isVisible)
    }

    override fun setText(@IdRes id: Int, text: CharSequence?) {
        mDelegate.setText(id, text)
    }

    override fun setText(@IdRes id: Int, @StringRes resId: Int) {
        mDelegate.setText(id, resId)
    }

    override fun setText(textView: TextView?, @StringRes resId: Int) {
        mDelegate.setText(textView, resId)
    }

    override fun setText(textView: TextView?, text: CharSequence?) {
        mDelegate.setText(textView, text)
    }

    override fun setTextColor(textView: TextView?, color: Int) {
        mDelegate.setTextColor(textView, color)
    }

    override fun setTextColor(id: Int, color: Int) {
        mDelegate.setTextColor(id, color)
    }

    override fun setTextSize(id: Int, size: Float) {
        mDelegate.setTextSize(id, size)
    }

    override fun setTextSize(textView: TextView?, size: Float) {
        mDelegate.setTextSize(textView, size)
    }

    override fun setImageResource(imageView: ImageView?, @DrawableRes resId: Int) {
        mDelegate.setImageResource(imageView, resId)
    }

    override fun setImageBitmap(imageView: ImageView?, bitmap: Bitmap?) {
        mDelegate.setImageBitmap(imageView, bitmap)
    }

    override fun setImageDrawable(imageView: ImageView?, drawable: Drawable?) {
        mDelegate.setImageDrawable(imageView, drawable)
    }

    override fun showToast(content: String?) {
        mDelegate.showToast(content)
    }

    override fun showToast(@StringRes resId: Int) {
        mDelegate.showToast(resId)
    }

    override fun showLongToast(content: String?) {
        mDelegate.showLongToast(content)
    }

    override fun showLongToast(@StringRes resId: Int) {
        mDelegate.showLongToast(resId)
    }

    protected fun setBackgroundColor(view: View?, @ColorInt color: Int) {
        mDelegate.setBackgroundColor(view, color)
    }

    protected fun getString(@StringRes resId: Int): String? {
        return mDelegate.getString(resId)
    }

    protected fun getColor(@ColorRes id: Int): Int {
        return mDelegate.getColor(id)
    }

    protected fun getDrawable(@DrawableRes id: Int): Drawable? {
        return mDelegate.getDrawable(id)
    }
}
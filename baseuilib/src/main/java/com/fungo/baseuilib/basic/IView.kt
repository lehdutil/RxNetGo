package com.fungo.baseuilib.basic

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes

/**
 * @author pinger
 * @since 2018/1/13 23:18
 *
 * View的一些基本操作行为，一般放在基类里实现
 */
interface IView : View.OnClickListener {

    // 获取上下文
    fun getContext(): Context?

    // 查找View
    fun <T : View> findView(@IdRes id: Int): T

    // 点击事件
    fun setOnClick(@IdRes id: Int)

    fun setOnClick(view: View?)
    fun onClick(@IdRes id: Int)
    override fun onClick(view: View) {
        onClick(view.id)
    }

    // View的可见与不可见
    fun setVisibility(@IdRes id: Int, visibility: Int)

    fun setVisibility(view: View?, visibility: Int)
    fun setVisibility(@IdRes id: Int, isVisible: Boolean)
    fun setVisibility(view: View?, isVisible: Boolean)
    fun setVisible(@IdRes id: Int)
    fun setVisible(view: View?)
    fun setGone(@IdRes id: Int)
    fun setGone(view: View?)

    // 设置文字
    fun setText(@IdRes id: Int, text: CharSequence?)

    fun setText(@IdRes id: Int, @StringRes resId: Int)
    fun setText(textView: TextView?, text: CharSequence?)
    fun setText(textView: TextView?, @StringRes resId: Int)

    // 设置图片
    fun setImageResource(imageView: ImageView?, @DrawableRes resId: Int)

    fun setImageBitmap(imageView: ImageView?, bitmap: Bitmap?)
    fun setImageDrawable(imageView: ImageView?, drawable: Drawable?)

    // 展示吐司
    fun showToast(content: String?)

    fun showToast(@StringRes resId: Int)
    fun showLongToast(content: String?)
    fun showLongToast(@StringRes resId: Int)

    // 判空
    fun checkNotNull(any: Any?) {
        checkNotNull(any, "$any not be null.")
    }

    fun checkNotNull(any: Any?, msg: String) {
        if (any == null) {
            throw NullPointerException(msg)
        }
    }
}
package com.fungo.baseuilib.utils

import android.annotation.TargetApi
import android.app.Activity
import android.content.ClipboardManager
import android.content.Context
import android.content.res.Configuration
import android.graphics.Point
import android.graphics.Typeface
import android.os.Build
import android.text.TextUtils
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.annotation.ArrayRes
import androidx.annotation.StringRes

/**
 * @author Pinger
 * @since 3/29/18 7:34 PM
 * 与View相关的工具类
 */
object ViewUtils {

    private val TAG: String = ViewUtils::class.java.simpleName

    /**
     * [TextView]
     * set TextView content. view is not null and content is not null.
     */
    fun setText(view: TextView?, content: String) {
        if (view != null) {
            if (TextUtils.isEmpty(content)) {
                setGone(view)
            } else {
                setVisible(view)
                view.text = content
            }
        } else {
            Log.e(TAG, "setText Warning： TextView is null ")
        }
    }

    /**
     * [View.VISIBLE]
     * safe set view visible.
     */
    fun setVisible(view: View?) {
        if (view != null) {
            if (view.visibility != View.VISIBLE) {
                setVisibility(view, View.VISIBLE)
            }
        } else {
            Log.e(TAG, "setVisible Warning： View is null ")
        }
    }

    /**
     * [View.GONE]
     * safe set view gone.
     */
    fun setGone(view: View?) {
        if (view != null) {
            if (view.visibility != View.GONE) {
                setVisibility(view, View.GONE)
            }
        } else {
            Log.e(TAG, "setGone Warning： View is null ")
        }
    }

    /**
     * [View.INVISIBLE]
     * safe set view invisible.
     */
    fun setInvisible(view: View?) {
        if (view != null) {
            setVisibility(view, View.INVISIBLE)
        } else {
            Log.e(TAG, "setInvisible Warning： View is null ")
        }
    }

    /**
     * safe set view visible,gone or invisible.
     */
    private fun setVisibility(view: View?, visibility: Int) {
        if (view != null) {
            if (visibility == View.VISIBLE) {
                if (view.visibility != View.VISIBLE) {
                    view.visibility = visibility
                }
            } else if (visibility == View.GONE) {
                if (view.visibility != View.GONE) {
                    view.visibility = visibility
                }
            } else {
                view.visibility = visibility
            }
        } else {
            Log.e(TAG, "setVisibility Warning： View is null ")
        }
    }

    fun isVisible(view: View?): Boolean {
        return view != null && view.visibility == View.VISIBLE
    }


    /**
     * set view visible or gone
     */
    fun setVisible(view: View?, isVisible: Boolean) {
        if (isVisible) {
            setVisible(view)
        } else {
            setGone(view)
        }
    }


    /**
     * set TextView content with CharSequence.
     */
    fun setText(view: TextView?, text: CharSequence) {
        if (view != null) {
            view.text = text
        } else {
            Log.e(TAG, "setText Warning： TextView is null ")
        }
    }

    /**
     * set TextView content with resId.
     */
    fun setText(view: TextView?, @StringRes resId: Int) {
        if (view != null && view.context != null) {
            view.text = view.context.getString(resId)
        } else {
            Log.e(TAG, "setText Warning： TextView is null ")
        }
    }

    /**
     * determine view is not null.
     */
    fun isNonNull(view: View?): Boolean {
        return view != null
    }

    /**
     * determine view is null.
     */
    fun isNull(view: View?): Boolean {
        return view == null
    }

    /**
     * view is enabled.
     */
    fun isEnabled(view: View?): Boolean {
        return view != null && view.isEnabled
    }

    /**
     * view is selected.
     */
    fun isSelected(view: View?): Boolean {
        return view != null && view.isSelected
    }

    /**
     * view is not enabled.
     */
    fun isNotEnable(view: View?): Boolean {
        return view != null && !view.isEnabled
    }

    /**
     * set view is enabled or not isEnable.
     */
    fun setEnabled(view: View?, isEnable: Boolean) {
        if (view != null) {
            view.isEnabled = isEnable
        }
    }

    /**
     * 获取String资源集合
     */
    fun getStringArray(context: Context?, @ArrayRes id: Int): Array<String>? {
        if (context != null) {
            return context.resources?.getStringArray(id)
        }
        return null
    }

    /**
     * 获取String资源集合
     */
    fun getString(context: Context?, @StringRes id: Int): String? {
        if (context != null) {
            return context.resources?.getString(id)
        }
        return null
    }

    /**
     * copy string to clipboard
     */
    fun copyContent(context: Context, content: String) {
        val cmb = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        cmb.text = content.trim { it <= ' ' }
    }

    /**
     * paste string to edit
     */
    fun pasteContent(context: Context): String {
        val cmb = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        return cmb.text.toString().trim { it <= ' ' }
    }

    /**
     * determine Activity is Port.
     */
    fun isPort(context: Context?): Boolean {
        return context == null || context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    }

    fun isLand(context: Context?): Boolean {
        return context != null && context.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    }

    /**
     * dp to px
     */
    fun dp2px(context: Context?, dipValue: Int): Int {
        if (context == null) {
            return dipValue
        }
        val scale = context.resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }

    /**
     * px to dip
     */
    fun px2dp(context: Context?, pxValue: Int): Int {
        if (context == null) {
            return pxValue
        }
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    /**
     * px to sp
     */
    fun px2sp(context: Context?, pxValue: Int): Int {
        if (context == null) {
            return pxValue
        }
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (pxValue / fontScale + 0.5f).toInt()
    }

    /**
     * sp to px
     */
    fun sp2px(context: Context?, spValue: Int): Int {
        if (context == null) {
            return spValue
        }
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }

    /**
     * screen height
     */
    fun getScreenHeight(context: Context): Int {
        val height: Int
        val dm = DisplayMetrics()
        val windowMgr = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            windowMgr.defaultDisplay.getRealMetrics(dm)
            height = dm.heightPixels
        } else {
            val point = Point()
            windowMgr.defaultDisplay.getSize(point)
            return point.y
        }
        return height
    }

    /**
     * screen width
     */
    fun getScreenWidth(context: Context): Int {
        val width: Int
        val dm = DisplayMetrics()
        val windowMgr = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            windowMgr.defaultDisplay.getRealMetrics(dm)
            width = dm.widthPixels
        } else {
            val point = Point()
            windowMgr.defaultDisplay.getSize(point)
            return point.x
        }
        return width
    }


    /**
     * 设置字体样式
     *
     * @param textView
     * @param path
     */
    fun setTypeFace(textView: TextView, path: String) {
        val typeface = Typeface.createFromAsset(textView.context.assets, path)
        textView.typeface = typeface
    }

    /**
     * 获取状态栏的高度
     */
    fun getStatusHeight(context: Context?): Int {
        if (context == null) {
            return 0
        }
        var statusBarHeight = -1
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            statusBarHeight = context.resources.getDimensionPixelSize(resourceId)
        }
        return statusBarHeight
    }

    /**
     * 底部虚拟按键栏的高度
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    fun getSoftButtonsBarHeight(activity: Activity): Int {
        val metrics = DisplayMetrics()
        //这个方法获取可能不是真实屏幕的高度
        activity.windowManager.defaultDisplay.getMetrics(metrics)
        val usableHeight = metrics.heightPixels
        //获取当前屏幕的真实高度
        activity.windowManager.defaultDisplay.getRealMetrics(metrics)
        val realHeight = metrics.heightPixels
        return if (realHeight > usableHeight) {
            realHeight - usableHeight
        } else {
            0
        }
    }
}
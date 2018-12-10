package com.fungo.baseuilib.theme

import android.content.Context
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.fungo.baseuilib.R
import com.fungo.baseuilib.utils.ViewUtils
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.typeface.IIcon
import java.util.*


/**
 * @author Pinger
 * @since 18-8-3 上午11:29
 * 系统UI处理工具类，包括主题，图标等等⑽⑼⑹㈤
 */

object UiUtils {

    private const val KEY_THEME = "KEY_THEME"

    private var sharedPreferences: SharedPreferences? = null

    private fun getSp(context: Context): SharedPreferences {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
        }
        return sharedPreferences!!
    }

    private fun putString(context: Context, key: String, value: String) {
        getSp(context).edit().putString(key, value).apply()
    }

    private fun putBoolean(context: Context, key: String, value: Boolean) {
        getSp(context).edit().putBoolean(key, value).apply()
    }

    private fun getString(context: Context, key: String, defValue: String = ""): String? {
        return getSp(context).getString(key, defValue)
    }

    private fun getBoolean(context: Context, key: String, defValue: Boolean = true): Boolean {
        return getSp(context).getBoolean(key, defValue)
    }

    /**
     * 设置App的主题
     * 默认是来上一次的主题
     */
    fun setAppTheme(context: Context, theme: AppTheme = getAppTheme(context)) {
        setCurrentTheme(context, theme)
    }

    /**
     * 获取当前的主题，返回的是上一次选择的主题
     * 如果没有则返回[AppTheme.Blue]
     */
    fun getAppTheme(context: Context): AppTheme {
        val theme = getString(context, KEY_THEME, AppTheme.Blue.name)
        return if (theme != null) {
            AppTheme.valueOf(theme)
        } else AppTheme.Blue
    }

    /**
     * 返回随机一个主题
     */
    fun getRandomTheme(): AppTheme {
        val arrayOfAppThemes = AppTheme.values()
        val random = Random()
        return arrayOfAppThemes[random.nextInt(arrayOfAppThemes.size)]
    }


    /**
     * 根据提供的主题枚举设置当前的主题
     */
    private fun setCurrentTheme(context: Context, theme: AppTheme) {
        when (theme) {
            AppTheme.Blue -> context.setTheme(R.style.BlueTheme)
            AppTheme.Red -> context.setTheme(R.style.RedTheme)
            AppTheme.Brown -> context.setTheme(R.style.BrownTheme)
            AppTheme.Green -> context.setTheme(R.style.GreenTheme)
            AppTheme.Purple -> context.setTheme(R.style.PurpleTheme)
            AppTheme.Teal -> context.setTheme(R.style.TealTheme)
            AppTheme.Pink -> context.setTheme(R.style.PinkTheme)
            AppTheme.DeepPurple -> context.setTheme(R.style.DeepPurpleTheme)
            AppTheme.Orange -> context.setTheme(R.style.OrangeTheme)
            AppTheme.Indigo -> context.setTheme(R.style.IndigoTheme)
            AppTheme.LightGreen -> context.setTheme(R.style.LightGreenTheme)
            AppTheme.Lime -> context.setTheme(R.style.LimeTheme)
            AppTheme.DeepOrange -> context.setTheme(R.style.DeepOrangeTheme)
            AppTheme.Cyan -> context.setTheme(R.style.CyanTheme)
            AppTheme.Black -> context.setTheme(R.style.BlackTheme)
            AppTheme.BlueGrey -> context.setTheme(R.style.BlueGreyTheme)
        }
        saveCurrentTheme(context, theme)
    }


    /**
     * 根据提供的颜色值设置当前的主题
     */
    fun getThemeFromColor(context: Context, color: Int): AppTheme {
        var appTheme: AppTheme = AppTheme.Black
        when (color) {
            getColor(context, R.color.colorBluePrimary) -> appTheme = AppTheme.Blue
            getColor(context, R.color.colorRedPrimary) -> appTheme = AppTheme.Red
            getColor(context, R.color.colorBrownPrimary) -> appTheme = AppTheme.Brown
            getColor(context, R.color.colorGreenPrimary) -> appTheme = AppTheme.Green
            getColor(context, R.color.colorPurplePrimary) -> appTheme = AppTheme.Purple
            getColor(context, R.color.colorTealPrimary) -> appTheme = AppTheme.Teal
            getColor(context, R.color.colorPinkPrimary) -> appTheme = AppTheme.Pink
            getColor(context, R.color.colorDeepPurplePrimary) -> appTheme = AppTheme.DeepPurple
            getColor(context, R.color.colorOrangePrimary) -> appTheme = AppTheme.Orange
            getColor(context, R.color.colorIndigoPrimary) -> appTheme = AppTheme.Indigo
            getColor(context, R.color.colorLightGreenPrimary) -> appTheme = AppTheme.LightGreen
            getColor(context, R.color.colorLimePrimary) -> appTheme = AppTheme.Lime
            getColor(context, R.color.colorDeepOrangePrimary) -> appTheme = AppTheme.DeepOrange
            getColor(context, R.color.colorCyanPrimary) -> appTheme = AppTheme.Cyan
            getColor(context, R.color.colorBlueGreyPrimary) -> appTheme = AppTheme.BlueGrey
            getColor(context, R.color.colorBlackPrimary) -> appTheme = AppTheme.Black
        }
        return appTheme
    }


    /**
     * 获取某一个属性的主题颜色
     */
    fun getThemeColor(context: Context, attrRes: Int = R.attr.colorPrimary): Int {
        val theme = context.theme
        val typedArray = theme.obtainStyledAttributes(intArrayOf(attrRes))
        val color = typedArray.getColor(0, Color.LTGRAY)
        typedArray.recycle()
        return color
    }

    /**
     * 获取当前的主题颜色
     */
    fun getCurrentThemeColor(context: Context): Int {
        return getThemeColor(context, R.attr.colorPrimary)
    }

    /**
     * 保存当前的主题到本地
     */
    private fun saveCurrentTheme(context: Context, currentTheme: AppTheme) {
        putString(context, KEY_THEME, currentTheme.name)
    }

    private fun saveCurrentTheme(context: Context, color: Int) {
        putString(context, KEY_THEME, getThemeFromColor(context, color).name)
    }

    private fun getColor(context: Context, color: Int): Int {
        return ContextCompat.getColor(context, color)
    }

    /**
     * 设置ImageView的图标
     * 传入的是IconFont
     */
    fun setIconFont(
            imageView: ImageView?,
            icon: IIcon,
            size: Int = 16,
            color: Int = R.attr.colorPrimary
    ) {
        imageView?.setImageDrawable(
                IconicsDrawable(imageView.context)
                        .icon(icon)
                        .color(getThemeColor(imageView.context, color))
                        .sizeDp(size)
        )
    }


    /**
     * 设置ImageView的图标
     * 传入的是IconFont
     */
    fun setIconFont(
            imageView: ImageView?,
            drawable: Drawable?,
            color: Int = R.attr.colorPrimary
    ) {
        if (imageView != null && drawable != null) {
            val wrap = DrawableCompat.wrap(drawable)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                wrap.setTintList(ColorStateList.valueOf(getThemeColor(imageView.context, color)))
            }
            imageView.setImageDrawable(wrap)
        }
    }


    /**
     * 设置TextView图标
     */
    fun setIconFont(
            textView: TextView?,
            icon: IIcon,
            size: Int = 16,
            padding: Int = 4,
            color: Int = R.attr.colorPrimary
    ) {
        if (textView != null) {
            textView.setCompoundDrawablesWithIntrinsicBounds(
                    IconicsDrawable(textView.context)
                            .icon(icon)
                            .color(getThemeColor(textView.context, color))
                            .sizeDp(size),
                    null, null, null
            )
            textView.compoundDrawablePadding = ViewUtils.dp2px(textView.context, padding)
        }
    }


    /**
     * 生成一个icon font
     */
    fun getIconFont(context: Context, icon: IIcon, size: Int = 16, color: Int = R.attr.colorPrimary): Drawable {
        return IconicsDrawable(context)
                .icon(icon)
                .color(getThemeColor(context, color))
                .sizeDp(size)
    }
}
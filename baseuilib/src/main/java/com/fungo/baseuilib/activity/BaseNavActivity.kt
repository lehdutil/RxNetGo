package com.fungo.baseuilib.activity

import androidx.appcompat.widget.Toolbar
import com.fungo.baseuilib.R
import com.fungo.baseuilib.fragment.BaseFragment
import com.fungo.baseuilib.fragment.PlaceholderFragment
import com.fungo.baseuilib.theme.UiUtils
import com.fungo.baseuilib.utils.StatusBarUtils
import com.fungo.baseuilib.utils.ViewUtils
import com.mikepenz.google_material_typeface_library.GoogleMaterial
import kotlinx.android.synthetic.main.base_activity_nav.*

/**
 * @author Pinger
 * @since 2018/11/3 18:54
 *
 * 带有导航栏的Activity，结构为Toolbar,
 */
open class BaseNavActivity(override val layoutResID: Int = R.layout.base_activity_nav) : BaseActivity() {

    // 导航栏标题
    private var mPageTitle: String? = null

    final override fun initView() {
        // 设置是否展示标题栏
        setVisibility(baseNavAppBar, isShowToolBar())

        // 设置状态栏高度
        if (isSetStatusBarHeight()) {
            StatusBarUtils.setStatusBarHeight(baseNavStatusView)
        }

        // 设置导航栏文字等
        if (isShowToolBar()) {
            // 设置标题
            setPageTitle(getPageTitle())

            // 左侧返回按钮
            if (isShowBackIcon()) {
                baseNavToolbar.navigationIcon =
                        UiUtils.getIconFont(this, GoogleMaterial.Icon.gmd_arrow_back, color = R.attr.colorWhite)
                baseNavToolbar.setNavigationOnClickListener {
                    onBackClick()
                }
            }

            // 填充Menu
            if (getMenuResID() != 0) {
                baseNavToolbar.inflateMenu(getMenuResID())
                baseNavToolbar.setOnMenuItemClickListener {
                    onMenuItemSelected(it.itemId)
                }
            }
        }

        // 设置填充容器
        if (baseNavContent.childCount > 0) {
            baseNavContent.removeAllViews()
        }

        // 设置内容容器的填充方式
        if (getContentResID() == 0) {
            val fragment = getContentFragment()
            // 转移Activity的extras给Fragment
            if (fragment.arguments == null && intent.extras != null) {
                fragment.arguments = intent.extras
            } else if (fragment.arguments != null && intent.extras != null) {
                fragment.arguments!!.putAll(intent.extras)
            }
            loadRootFragment(R.id.baseNavContent, fragment)
        } else {
            layoutInflater.inflate(getContentResID(), baseNavContent)
        }

        // 初始化容器View
        initContentView()
    }

    /**
     * 子类继续填充内容容器布局
     */
    protected open fun getContentResID(): Int = 0

    /**
     *  子类填充Fragment
     */
    protected open fun getContentFragment(): BaseFragment = PlaceholderFragment()

    /**
     * 给子类初始化View使用
     */
    protected open fun initContentView() {

    }

    /**
     * 获取标题栏对象，让子类主动去设置样式
     */
    fun getToolBar(): Toolbar {
        return baseNavToolbar
    }

    /**
     * 获取页面标题，进入页面后会调用该方法获取标题，设置给ToolBar
     * 调用该方法返回Title，则会使用默认的Title样式，如果需要设置样式
     * 请调用setPageTitle()
     */
    protected open fun getPageTitle(): String? = mPageTitle

    /**
     * 主动设置页面标题，给子类调用
     */
    protected open fun setPageTitle(title: String?) {
        if (mPageTitle != title) {
            mPageTitle = title
        }
        baseNavToolbar.title = title
    }

    /**
     * 执行返回操作
     * 默认是Fragment弹栈，然后退出Activity
     * 如果栈内只有一个Fragment，则退出Activity
     */
    protected open fun onBackClick() {
        onBackPressedSupport()
    }

    /**
     * 是否设置状态栏高度，如果设置的话，默认会自动调整状态栏的高度。
     * 如果为false则状态栏高度为0，默认状态栏有高度。
     */
    protected open fun isSetStatusBarHeight(): Boolean = true

    /**
     * 设置ToolBar的展示状态
     * @param isShow 是否展示
     */
    protected open fun setShowToolBar(isShow: Boolean) {
        ViewUtils.setVisible(baseNavAppBar, isShow)
    }

    /**
     * 是否展示ToolBar，如果设置为false则不展示。
     * 如果不展示标题栏，则状态栏也不会展示。
     */
    protected open fun isShowToolBar(): Boolean = true

    /**
     * 是否可以返回，如果可以则展示返回按钮，并且设置返回事件
     * 默认可以返回
     */
    protected open fun isShowBackIcon(): Boolean = true
}
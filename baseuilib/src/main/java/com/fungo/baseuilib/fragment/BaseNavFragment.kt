package com.fungo.baseuilib.fragment

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.fungo.baseuilib.R
import com.fungo.baseuilib.theme.UiUtils
import com.fungo.baseuilib.utils.StatusBarUtils
import com.fungo.baseuilib.utils.ViewUtils
import com.mikepenz.google_material_typeface_library.GoogleMaterial
import kotlinx.android.synthetic.main.base_fragment_nav.*

/**
 * @author Pinger
 * @since 2018/11/3 18:53
 *
 * 页面Fragment基类，封装ToolBar和StatusBar，可以不用搭理顶部的标题栏。
 * 另外将页面的各种状态进行统一处理，方便直接调用展示，比如加载中试图，空试图，错误试图等。
 * 还有将平时开发中用到的各种工具类进行封装，提供给子类调用。
 */
abstract class BaseNavFragment : BaseFragment() {

    // 导航栏标题
    private var mPageTitle: String? = null
    private var mLoadingDialog: AlertDialog? = null

    // 加载框是否在展示
    protected var isLoadingShowing = false
    // 加载对话框是否在展示
    protected var isLoadingDialogShowing = false

    override fun getLayoutResID(): Int = R.layout.base_fragment_nav

    final override fun initView() {
        // 设置是否展示标题栏
        ViewUtils.setVisible(baseNavAppBar, isShowToolBar())

        // 设置状态栏高度
        if (isSetStatusBarHeight()) {
            StatusBarUtils.setStatusBarHeight(baseNavStatusView)
        }
        // 设置导航栏文字等
        if (isShowToolBar()) {
            // 设置标题
            setPageTitle(getPageTitle())

            // 设置Toolbar标题样式
            val toolbarTitleStyle = if (isMainPage()) {
                // 如果是主页的话，没有返回按钮，不可以滑动返回
                isShowBackIcon()
                R.style.ToolbarMainTextAppearance
            } else R.style.ToolbarTextAppearance
            baseNavToolbar.setTitleTextAppearance(context, toolbarTitleStyle)

            // 左侧返回按钮
            if (isShowBackIcon() && !isMainPage()) {
                baseNavToolbar.navigationIcon =
                        UiUtils.getIconFont(context!!, GoogleMaterial.Icon.gmd_arrow_back, color = R.attr.colorWhite)
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
        LayoutInflater.from(context).inflate(getContentResID(), baseNavContent)

        // 初始化容器View
        initContentView()
    }

    /**
     * 子类继续填充内容容器布局
     */
    abstract fun getContentResID(): Int

    /**
     * 给子类初始化View使用
     */
    protected open fun initContentView() {

    }

    /**
     * 获取标题栏对象，让子类主动去设置样式
     */
    open fun getToolBar(): Toolbar {
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
     * 获取菜单项资源ID
     */
    protected open fun getMenuResID(): Int = 0

    /**
     * 菜单项点击
     */
    protected open fun onMenuItemSelected(itemId: Int): Boolean = true


    /**
     * 执行返回操作
     * 默认是Fragment弹栈，然后退出Activity
     * 如果栈内只有一个Fragment，则退出Activity
     */
    protected open fun onBackClick() {
        getPageActivity()?.onBackPressedSupport()
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


    /**
     * 是不是Main页面
     * 如果是的话，ToolBar设置粗体的样式
     */
    protected open fun isMainPage(): Boolean = false


    // --------------------页面状态的相关处理--------------------
    /**
     * 展示加载对话框
     * 适用于页面UI已经绘制了，需要再加载数据更新的情况
     */
    open fun showPageLoadingDialog(msg: String) {
        if (mLoadingDialog == null) {
            mLoadingDialog = AlertDialog.Builder(context).create()
            mLoadingDialog!!.setCanceledOnTouchOutside(false)
        }

        if (isAdded && !mLoadingDialog!!.isShowing) {
            mLoadingDialog!!.show()
            val dialogView = LayoutInflater.from(context).inflate(R.layout.base_layout_loading_dialog, null)
            mLoadingDialog!!.setContentView(dialogView)
            dialogView.findViewById<TextView>(R.id.tvLoadingMessage).text = msg
            isLoadingDialogShowing = true
        }
    }

    open fun showPageLoadingDialog() {
        showPageLoadingDialog(getString(R.string.app_loading))
    }

    /**
     * 隐藏加载对话框
     */
    open fun hidePageLoadingDialog() {
        mLoadingDialog?.dismiss()
        isLoadingDialogShowing = false
    }

    /**
     * 展示加载中的占位图
     */
    open fun showPageLoading() {
        basePlaceholder?.showLoading()
        isLoadingShowing = true
    }

    open fun showPageLoading(msg: String) {
        basePlaceholder?.showLoading()
        basePlaceholder?.setPageLoadingText(msg)
        isLoadingShowing = true
    }

    open fun hidePageLoading() {
        basePlaceholder?.hideLoading()
        isLoadingShowing = false
    }

    /**
     * 展示空数据的占位图
     */
    open fun showPageEmpty() {
        basePlaceholder?.showEmpty()
    }

    /**
     * 展示加载错误的占位图
     */
    open fun showPageError() {
        showPageError(null)
    }

    open fun showPageError(msg: String?) {
        basePlaceholder?.setPageErrorText(msg)
        basePlaceholder?.showError()
    }

    /**
     * 设置页面加载错误重连的监听
     */
    open fun setPageErrorRetryListener(listener: View.OnClickListener) {
        basePlaceholder?.setPageErrorRetryListener(listener)
    }

    /**
     * 设置空白页面的提示
     */
    open fun setPageEmptyHint(msg: String) {
        basePlaceholder?.setPageEmptyText(msg)
    }

    /**
     * 展示加载完成，要显示的内容
     */
    open fun showPageContent() {
        basePlaceholder?.showContent()
        isLoadingShowing = false
    }
}
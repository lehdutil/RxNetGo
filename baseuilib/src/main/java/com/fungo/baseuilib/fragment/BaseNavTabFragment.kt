package com.fungo.baseuilib.fragment

import androidx.appcompat.widget.Toolbar
import com.fungo.baseuilib.R
import com.fungo.baseuilib.adapter.BaseFragmentPageAdapter
import com.fungo.baseuilib.theme.UiUtils
import com.fungo.baseuilib.utils.ViewUtils
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.mikepenz.google_material_typeface_library.GoogleMaterial
import kotlinx.android.synthetic.main.base_nav_tab.*

/**
 * @author Pinger
 * @since 18-12-10 下午6:01
 */
abstract class BaseNavTabFragment : BaseFragment() {

    override fun getLayoutResID(): Int = R.layout.base_nav_tab

    // 导航栏标题
    private var mPageTitle: String? = null

    final override fun initView() {
        // 设置是否展示标题栏
        setVisibility(baseNavAppBar, isShowToolBar())

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
            if (isShowBackIcon()) {
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

        val fragments = getFragments()
        val adapter = BaseFragmentPageAdapter(childFragmentManager, fragments, getTitles())
        baseNavViewPager.adapter = adapter
        baseNavViewPager.offscreenPageLimit = fragments.size
        baseNavTabLayout.tabMode = TabLayout.MODE_SCROLLABLE
        baseNavTabLayout.setupWithViewPager(baseNavViewPager)


        // 初始化容器View
        initContentView()
    }

    /**
     * 获取填充的Fragment页面
     */
    abstract fun getFragments(): ArrayList<BaseFragment>

    /**
     * 获取Tab的标题
     */
    abstract fun getTitles(): ArrayList<String>

    /**
     * 给子类初始化View使用
     */
    protected open fun initContentView() {

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
    protected open fun isShowBackIcon(): Boolean = false


    /**
     * 是不是Main页面
     * 如果是的话，ToolBar设置粗体的样式
     */
    protected open fun isMainPage(): Boolean = false

    /**
     * 执行返回操作
     * 默认是Fragment弹栈，然后退出Activity
     * 如果栈内只有一个Fragment，则退出Activity
     */
    protected open fun onBackClick() {
        onBackPressedSupport()
    }

    /**
     * 获取悬浮按钮
     */
    protected open fun getFloatActionButton(): FloatingActionButton? = baseNavFloatButton
}
package com.fungo.baseuilib.recycler

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fungo.baseuilib.R
import com.fungo.baseuilib.fragment.BaseNavFragment
import com.fungo.baseuilib.recycler.header.RecyclerClassicsFooter
import com.fungo.baseuilib.recycler.header.material.GoogleMaterialHeader
import com.fungo.baseuilib.recycler.item.DividerItemDecoration
import com.fungo.baseuilib.recycler.multitype.MultiTypeAdapter
import com.fungo.baseuilib.recycler.multitype.MultiTypeViewHolder
import com.fungo.baseuilib.recycler.multitype.OneToManyFlow
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshFooter
import com.scwang.smartrefresh.layout.api.RefreshHeader
import kotlinx.android.synthetic.main.base_fragment_recycler.*

/**
 * @author Pinger
 * @since 18-7-25 下午4:35
 * 列表页面布局，封装SmartRefreshLayout，方便替换
 */
abstract class BaseRecyclerFragment : BaseNavFragment(), BaseRecyclerContract.View {

    private lateinit var mPresenter: BaseRecyclerContract.Presenter
    private lateinit var mAdapter: MultiTypeAdapter

    private var mSmartRefreshLayout: SmartRefreshLayout? = null
    private var mRecyclerView: RecyclerView? = null
    private var mRefreshFooter: RecyclerClassicsFooter? = null
    private var mPage = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter = getPresenter()
    }


    override fun getContentResID(): Int {
        return R.layout.base_fragment_recycler
    }

    final override fun initContentView() {
        initPrePageView()
        mSmartRefreshLayout = getSmartRefreshLayout()
        mRecyclerView = getRecyclerView()

        if (!isEnablePureScrollMode()) {
            mSmartRefreshLayout?.refreshHeader = getRefreshHeader()
            mSmartRefreshLayout?.refreshFooter = getRefreshFooter()
        }

        mSmartRefreshLayout?.setOnRefreshListener {
            mPage = getStartPage()
            mPresenter.loadData(mPage)
        }
        mSmartRefreshLayout?.setOnLoadmoreListener {
            mPage += 1
            mPresenter.loadData(mPage)
        }

        mRecyclerView?.layoutManager = getLayoutManager()
        mAdapter = getAdapter()
        mRecyclerView?.adapter = mAdapter
        if (generateItemDivider() != null) {
            mRecyclerView?.addItemDecoration(generateItemDivider()!!)
        }
        setPageErrorRetryListener(View.OnClickListener { initData() })

        initPageView()
    }


    final override fun initData() {
        mPage = getStartPage()
        setSmartLayoutAttrs()
        if (isShowLoadingPage()) {
            showPageLoading()
        }

        if (isShowLoadingDialog()) {
            showPageLoadingDialog()
        }

        mPresenter.loadData(mPage)
    }

    override fun onStart() {
        super.onStart()
        mPresenter.onStart()
    }

    override fun onStop() {
        super.onStop()
        mPresenter.onStop()
    }

    /**
     * 获取开始加载的起始页，不一定是从0开始，默认从0开始
     */
    protected open fun getStartPage(): Int = 0


    /**
     * 从子类设置SmartRefreshLayout对象
     */
    protected open fun getSmartRefreshLayout(): SmartRefreshLayout = baseNavSmartRefreshLayout


    /**
     * 子类设置RecyclerView
     */
    protected open fun getRecyclerView(): RecyclerView = baseNavRecyclerView

    /**
     * 子类生成布局管理器
     */
    protected open fun getLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(context)
    }

    /**
     * 生成分割线
     */
    protected open fun generateItemDivider(): RecyclerView.ItemDecoration? {
        return DividerItemDecoration(ContextCompat.getColor(context!!, R.color.line_grey))
    }

    /**
     * 获取下拉刷新的头部
     */
    protected open fun getRefreshHeader(): RefreshHeader {
        return GoogleMaterialHeader(context!!)
    }

    /**
     * 获取刷新底部
     */
    protected open fun getRefreshFooter(): RefreshFooter {
        mRefreshFooter = RecyclerClassicsFooter(context!!)
        return mRefreshFooter!!
    }

    /**
     * 展示所有数据
     */
    override fun <T> showContent(datas: List<T>?) {
        if (mPage == getStartPage()) {
            mAdapter.clear()
        }

        if (datas == null || datas.isEmpty()) {
            showPageEmpty()
        } else {
            hideLoading()
            hideRefreshing()
            mAdapter.addAll(datas)
        }
    }

    /**
     * 展示一个数据
     */
    override fun <T> showContent(data: T?) {
        if (mPage == getStartPage()) {
            mAdapter.clear()
        }
        if (data == null) {
            showPageEmpty()
        } else {
            hideLoading()
            hideRefreshing()

            mAdapter.add(data)
        }
    }


    /**
     * 更新某一条数据
     */
    override fun <T> updateItem(data: T?, position: Int) {
        if (data != null && mAdapter.getCount() > 0 && position < mAdapter.getCount()) {
            mAdapter.update(data, position)
        }
    }

    /**
     * 添加一条数据
     */
    override fun <T> addItem(data: T?) {
        hideLoading()
        if (data != null) {
            mAdapter.add(data)
        }
    }


    /**
     * 插入一条数据
     */
    override fun <T> insertItem(position: Int, data: T?) {
        if (position < mAdapter.getCount() && data != null) {
            mAdapter.insert(data, position)
        }
    }


    /**
     * 结束刷新
     */
    private fun hideRefreshing(hasMore: Boolean = true) {
        if (mSmartRefreshLayout?.isRefreshing == true) {
            mSmartRefreshLayout?.finishRefresh()
        }
        if (mSmartRefreshLayout?.isLoading == true) {
            if (hasMore) {
                mSmartRefreshLayout?.finishLoadmore()
            } else {
                mSmartRefreshLayout?.finishLoadmoreWithNoMoreData()
            }
        }
    }

    /**
     * 当前页面是否激活，有View相关的操作时，做好先做一下判断
     */
    override fun isActive(): Boolean {
        return isAdded
    }

    /**
     * 有没有数据
     */
    protected open fun isEmptyRecycler(): Boolean {
        return mAdapter.itemCount == 0
    }

    /**
     * 注册展示的Holder
     */
    protected open fun <T> register(clazz: Class<out T>, holder: MultiTypeViewHolder<T, *>) {
        mAdapter.register(clazz, holder)
    }

    protected open fun <T> register(clazz: Class<out T>): OneToManyFlow<T> {
        return mAdapter.register(clazz)
    }

    /**
     * 获取子类Presenter对象
     */
    protected abstract fun getPresenter(): BaseRecyclerContract.Presenter

    /**
     * 获取子类适配器对象
     */
    private fun getAdapter(): MultiTypeAdapter {
        return MultiTypeAdapter(context)
    }

    /**
     * 设置SmartRefreshLayout相关属性
     */
    private fun setSmartLayoutAttrs() {
        mSmartRefreshLayout?.isEnableAutoLoadmore = isEnableAutoLoadmore()
        mSmartRefreshLayout?.isEnableLoadmore = isEnableLoadmore()
        mSmartRefreshLayout?.isEnableRefresh = isEnableRefresh()
        mSmartRefreshLayout?.isEnableOverScrollBounce = isEnableOverScrollBounce()
        mSmartRefreshLayout?.isEnablePureScrollMode = isEnablePureScrollMode()
    }

    /**
     * 隐藏正在加载的进度条
     */
    private fun hideLoading() {
        // 只有第0页才有加载框
        if (mPage == getStartPage()) {
            hidePageLoading()
            hidePageLoadingDialog()
        }
    }

    /**
     * 当展示空视图的时候，可以下拉，但是不可以上拉
     */
    override fun showPageEmpty(msg: String?) {
        hideLoading()
        if (mPage == getStartPage()) {
            mSmartRefreshLayout?.isEnableLoadmore = false
            hideRefreshing()
            super.showPageEmpty(msg)
        } else {
            // 如果不是第一页，并且没有数据了，就不结束加载更多
            hideRefreshing(false)
        }
    }


    override fun showPageError(msg: String?) {
        // 当加载异常时，要手动去关闭加载进度，这里统一处理
        hideLoading()
        hideRefreshing()

        if (mPage == getStartPage()) {
            mSmartRefreshLayout?.isEnableLoadmore = false
            super.showPageError(msg)
        } else {
            // 如果已经有数据了，就吐司提示
            showToast(msg ?: getString(R.string.app_loading_error))
        }
    }


    /**
     * 是否可以自动加载更多，默认可以
     */
    protected open fun isEnableAutoLoadmore() = true


    /**
     * 是否可以加载更多，默认可以
     */
    protected open fun isEnableLoadmore() = true

    /**
     * 是否可以刷新，默认可以
     */
    protected open fun isEnableRefresh() = true

    /**
     * 是否是纯净模式，不展示刷新头和底部，默认false
     */
    protected open fun isEnablePureScrollMode() = false

    /**
     * 刷新时是否可以越界回弹
     */
    protected open fun isEnableOverScrollBounce() = false

    /**
     * 默认展示加载页面
     */
    protected open fun isShowLoadingPage() = true

    /**
     * 是否展示加载对话框
     */
    protected open fun isShowLoadingDialog() = false

    /**
     * 初始化父类UI之前调用的方法
     */
    protected open fun initPrePageView() {}

    /**
     * 让子类重写，初始化页面
     */
    protected open fun initPageView() {}
}
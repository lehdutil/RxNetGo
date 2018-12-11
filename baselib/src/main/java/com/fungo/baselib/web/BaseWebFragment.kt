package com.fungo.baselib.web

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import android.webkit.*
import android.widget.ProgressBar
import com.fungo.baselib.R
import com.fungo.baselib.utils.NetStateUtils
import com.fungo.baselib.web.sonic.SonicRuntimeImpl
import com.fungo.baselib.web.sonic.SonicSessionClientImpl
import com.fungo.baseuilib.fragment.BaseFragment
import com.fungo.baseuilib.theme.UiUtils
import com.fungo.baseuilib.utils.StatusBarUtils
import com.fungo.baseuilib.utils.ViewUtils
import com.mikepenz.google_material_typeface_library.GoogleMaterial
import com.tencent.sonic.sdk.SonicConfig
import com.tencent.sonic.sdk.SonicEngine
import com.tencent.sonic.sdk.SonicSession
import com.tencent.sonic.sdk.SonicSessionConfig
import kotlinx.android.synthetic.main.base_fragment_web.*

/**
 * @author Pinger
 * @since 18-7-25 下午2:19
 * 加载网页的Fragment基类
 * 提供两种加载展示方法，默认使用进度条的方法
 */

open class BaseWebFragment : BaseFragment() {

    override fun getLayoutResID(): Int = R.layout.base_fragment_web

    private var mSonicSession: SonicSession? = null
    private var mSonicSessionClient: SonicSessionClientImpl? = null
    private var mWebUrl: String? = null
    private var mWebTitle: String? = null

    private lateinit var mIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        initSonic()
    }

    private fun init() {
        if (Build.VERSION.SDK_INT > 19) {
            WebView.setWebContentsDebuggingEnabled(true)
        }
        mIntent = getIntent(arguments)
        mWebUrl = mIntent.getStringExtra(WebConstant.KEY_WEB_URL)
        mWebTitle = mIntent.getStringExtra(WebConstant.KEY_WEB_TITLE)

        if (isCleanCache()) {
            SonicEngine.getInstance().cleanCache()
        }
    }


    override fun initView() {
        // 设置是否展示标题栏
        ViewUtils.setVisible(baseWebAppBar, isShowToolBar())

        StatusBarUtils.setStatusBarHeight(baseWebStatusView)

        if (isShowToolBar()) {
            setWebTitle(getWebTitle())

            // 左侧返回按钮
            if (isShowBackIcon()) {
                baseWebToolbar.navigationIcon =
                        UiUtils.getIconFont(context!!, GoogleMaterial.Icon.gmd_arrow_back, color = R.attr.colorWhite)
                baseWebToolbar.setNavigationOnClickListener {
                    onBackClick()
                }
            }

            // 填充Menu
            if (getMenuResID() != 0) {
                baseWebToolbar.inflateMenu(getMenuResID())
                baseWebToolbar.setOnMenuItemClickListener {
                    onMenuItemSelected(it.itemId)
                }
            }

        }

        ViewUtils.setVisible(getProgressBar(), isProgressBarLoading())

        initWebView()
    }

    override fun initData() {
        if (TextUtils.isEmpty(mWebUrl)) {
            showPageError(getString(R.string.app_web_empty))
            return
        }
        loadUrl(mWebUrl)
    }


    private fun initSonic() {
        // init sonic engine if necessary, or maybe u can do this when application created
        if (!SonicEngine.isGetInstanceAllowed()) {
            SonicEngine.createInstance(SonicRuntimeImpl(context!!), SonicConfig.Builder().build())
        }

        // if it's sonic mode , startup sonic session at first time
        if (isUseSonic()) { // sonic mode
            val sessionConfigBuilder = SonicSessionConfig.Builder()
            sessionConfigBuilder.setSupportLocalServer(true)

            // create sonic session and run sonic flow
            if (TextUtils.isEmpty(mWebUrl)) {
                return
            }
            mSonicSession = SonicEngine.getInstance().createSession(mWebUrl!!, sessionConfigBuilder.build())
            if (mSonicSession != null) {
                mSonicSessionClient = SonicSessionClientImpl()
                mSonicSession!!.bindClient(mSonicSessionClient)
            }
        }
    }


    /**
     * init WebView
     */
    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        if (getWebView() != null) {
            getWebView()!!.isLongClickable = true
            getWebView()!!.overScrollMode = View.OVER_SCROLL_NEVER
            getWebView()!!.isVerticalScrollBarEnabled = false
            getWebView()!!.webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    this@BaseWebFragment.onPageStarted(view, url, favicon)
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    if (mSonicSession != null) {
                        mSonicSession!!.sessionClient.pageFinish(url)
                    }
                    if (!TextUtils.isEmpty(view?.title)) {
                        setWebTitle(view?.title)
                    }
                    this@BaseWebFragment.onPageFinished(view, url)
                }
            }

            // Android　4.0版本没有　WebChromeClient.FileChooserParams　这个类，所有要单独处理
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWebView()!!.webChromeClient = object : WebChromeClient() {
                    /**
                     *　加载进度发生改变
                     */
                    override fun onProgressChanged(view: WebView?, newProgress: Int) {
                        if (isProgressBarLoading()) {
                            getProgressBar()?.progress = newProgress
                            if (newProgress >= 100) {
                                ViewUtils.setGone(getProgressBar())
                            }
                        }
                    }

                    //  5.0及以上调用
                    override fun onShowFileChooser(webView: WebView,
                                                   filePathCallback: ValueCallback<Array<Uri>>,
                                                   fileChooserParams: WebChromeClient.FileChooserParams): Boolean {
                        this@BaseWebFragment.onShowFileChooser(webView, filePathCallback)
                        return true
                    }

                    override fun onPermissionRequest(request: PermissionRequest) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            request.grant(request.resources)
                        }
                    }

                    override fun onReceivedTitle(view: WebView?, title: String?) {
                        super.onReceivedTitle(view, title)
                        if (!TextUtils.isEmpty(title)) {
                            setWebTitle(title)
                        }
                    }
                }
            } else {
                getWebView()!!.webChromeClient = object : WebChromeClient() {
                    // 3.0++版本
                    fun openFileChooser(uploadMsg: ValueCallback<Uri>, acceptType: String) {
                        openFileChooser(uploadMsg)
                    }

                    // 3.0--版本
                    fun openFileChooser(uploadMsg: ValueCallback<Uri>) {
                        this@BaseWebFragment.onShowFileChooser(uploadMsg)
                    }

                    fun openFileChooser(uploadMsg: ValueCallback<Uri>, acceptType: String, capture: String) {
                        openFileChooser(uploadMsg)
                    }
                }
            }

            getWebView()!!.setOnKeyListener { _, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK
                            && getWebView()?.canGoBack() == true && !isOriginalUrl() && isCanBack()) {
                        getWebView()?.goBack()
                        return@setOnKeyListener true
                    }
                }
                false
            }
            val webSettings = getWebView()!!.settings

            // add java script interface
            // note:if api level lower than 17(android 4.2), addJavascriptInterface has security
            // issue, please use x5 or see https://developer.android.com/reference/android/webkit/
            // WebView.html#addJavascriptInterface(java.lang.Object, java.lang.String)
            webSettings.javaScriptEnabled = true
            getWebView()!!.removeJavascriptInterface("searchBoxJavaBridge_")
            mIntent.putExtra(WebConstant.KEY_WEB_LOAD_URL_TIME, System.currentTimeMillis())

            // add interface
            addWebJsInteract(mSonicSessionClient, mIntent, getWebView())

            // init webview settings
            webSettings.allowContentAccess = true
            webSettings.loadsImagesAutomatically = true
            webSettings.useWideViewPort = true
            webSettings.loadWithOverviewMode = false
            webSettings.databaseEnabled = true
            webSettings.domStorageEnabled = true
            webSettings.setSupportZoom(true)
            webSettings.setAppCacheEnabled(true)
            webSettings.setSupportMultipleWindows(true)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            }
            CookieManager.getInstance().setAcceptCookie(true)
        }

    }


    /**
     * 加载Url
     */
    open fun loadUrl(url: String?) {

        // 加载之前判断一下网络是否已经连接
        if (!NetStateUtils.isConnected(context!!)) {
            showPageError(getString(R.string.app_network_disconnection))
            return
        }

        // webview is ready now, just tell session client to bind
        if (mSonicSessionClient != null && !isCanBack()) {
            mSonicSessionClient!!.bindWebView(getWebView()!!, isCanBack())
            mSonicSessionClient!!.clientReady()
        } else { // default mode
            getWebView()?.loadUrl(url)
        }
    }

    /**
     * 返回按键的处理
     */
    protected open fun onBackClick() {
        if (getWebView()?.canGoBack() == true && !isOriginalUrl() && isCanBack()) {
            getWebView()?.goBack()
        } else {
            getPageActivity()?.onBackPressedSupport()
        }
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
     * 重新加载
     */
    protected open fun onReload() {
        // 加载之前判断一下网络是否已经连接
        if (!NetStateUtils.isConnected(context!!)) {
            showPageError(getString(R.string.app_network_disconnection))
            return
        }

        if (isProgressBarLoading()) {
            setVisible(getProgressBar())
        }
        getWebView()?.reload()
    }

    /**
     * on loading start
     *
     * @param view
     * @param url
     * @param favicon
     */
    protected open fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        if (!isProgressBarLoading()) {
            placeholder?.showLoading()
        }
    }

    /**
     * on loading finish
     *
     * @param view
     * @param url
     */
    protected open fun onPageFinished(view: WebView?, url: String?) {
        placeholder?.showContent()
    }

    /**
     * get web title
     */
    protected open fun getWebTitle(): String? {
        return mWebTitle
    }

    /**
     * get web url
     */
    protected open fun getWebUrl(): String? {
        return mWebUrl
    }


    /**
     * H5内部是否可以返回，默认不可以返回
     */
    protected open fun isCanBack(): Boolean {
        return mIntent.getBooleanExtra(WebConstant.KEY_WEB_BACK, true)
    }

    /**
     * add javascript interface
     *
     * @param sonicSessionClient
     * @param intent
     * @param webView
     */
    protected open fun addWebJsInteract(sonicSessionClient: SonicSessionClientImpl?, intent: Intent?, webView: WebView?) {}

    /**
     * override url
     *
     * @param view
     * @param url
     * @return
     */
    protected open fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        return false
    }

    /**
     * set web title
     *
     * @param title
     */
    protected open fun setWebTitle(title: String?) {
        if (mWebTitle != title) {
            mWebTitle = title
        }
        baseWebToolbar?.title = title
    }

    /**
     * show file chooser
     *
     * @param webView
     * @param filePathCallback
     */
    protected open fun onShowFileChooser(webView: WebView, filePathCallback: ValueCallback<Array<Uri>>) {}

    /**
     * show file chooser
     *
     * @param filePathCallback
     */
    protected open fun onShowFileChooser(filePathCallback: ValueCallback<Uri>) {}

    /**
     * get web view
     *
     * @return
     */
    protected open fun getWebView(): WebView? = baseWebView

    /**
     * get progressbar
     */
    protected open fun getProgressBar(): ProgressBar? = baseWebProgressBar


    /**
     * is clean sonic cache
     *
     * @return
     */
    protected open fun isCleanCache(): Boolean = false

    /**
     * 是否使用sonic加载h5
     */
    protected open fun isUseSonic(): Boolean = true


    /**
     * 是不是第一个URl
     */
    protected open fun isOriginalUrl(): Boolean = getWebView()?.copyBackForwardList()?.getItemAtIndex(0)?.url == getWebView()?.copyBackForwardList()?.currentItem?.url


    /**
     * 是否使用进度条来呈现加载中占位图
     * 默认使用进度条
     */
    protected open fun isProgressBarLoading(): Boolean = true


    /**
     * 设置ToolBar的展示状态
     * @param isShow 是否展示
     */
    protected open fun setShowToolBar(isShow: Boolean) {
        ViewUtils.setVisible(baseWebAppBar, isShow)
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
     * 是否可以滑动返回
     */
    override fun isSwipeBackEnable(): Boolean {
        return mIntent.getBooleanExtra(WebConstant.KEY_WEB_SWIPE_BACK, false)
    }

    override fun onDetach() {
        mSonicSession?.destroy()
        mSonicSession = null
        getWebView()?.destroy()
        super.onDetach()
    }

    private fun getIntent(bundle: Bundle?): Intent {
        val intent = Intent()
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        return intent
    }


    private fun showPageError(msg: String?) {
        placeholder?.setPageErrorText(msg)
        placeholder?.setPageErrorRetryListener(View.OnClickListener { loadUrl(getWebUrl()) })
        placeholder?.showError()
    }


}
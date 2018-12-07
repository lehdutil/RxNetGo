package com.fungo.baselib.web

import android.annotation.TargetApi
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
import com.fungo.baselib.web.sonic.SonicRuntimeImpl
import com.fungo.baselib.web.sonic.SonicSessionClientImpl
import com.fungo.baseuilib.fragment.BaseNavFragment
import com.fungo.baseuilib.utils.ViewUtils
import com.tencent.sonic.sdk.SonicConfig
import com.tencent.sonic.sdk.SonicEngine
import com.tencent.sonic.sdk.SonicSession
import com.tencent.sonic.sdk.SonicSessionConfig

/**
 * @author Pinger
 * @since 18-7-25 下午2:19
 * 加载网页的Fragment基类
 * 提供两种加载展示方法，默认使用进度条的方法
 */

abstract class BaseWebFragment : BaseNavFragment() {

    private var mSonicSession: SonicSession? = null
    private var mSonicSessionClient: SonicSessionClientImpl? = null

    private var mWebView: WebView? = null
    protected var mWebUrl: String? = null
    protected var mWebTitle: String? = null
    private var mIntent: Intent? = null


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
        mWebUrl = getWebUrl(mIntent)
        mWebTitle = getWebTitle(mIntent)

        if (isCleanCache) {
            SonicEngine.getInstance().cleanCache()
        }
    }

    private fun getIntent(bundle: Bundle?): Intent {
        val intent = Intent()
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        return intent
    }

    override fun initContentView() {
        initWebView()
    }

    override fun initData() {
        if (TextUtils.isEmpty(mWebUrl)) {
            showPageEmpty()
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
        if (isSonicLoad) { // sonic mode
            val sessionConfigBuilder = SonicSessionConfig.Builder()
            sessionConfigBuilder.setSupportLocalServer(true)

            // create sonic session and run sonic flow
            if (TextUtils.isEmpty(mWebUrl)) {
                return
            }
            mSonicSession = SonicEngine.getInstance().createSession(mWebUrl!!, sessionConfigBuilder.build())
            if (null != mSonicSession) {
                mSonicSessionClient = SonicSessionClientImpl()
                mSonicSession!!.bindClient(mSonicSessionClient)
            }
        }
    }


    /**
     * init WebView
     */
    private fun initWebView() {
        mWebView = getWebView()
        mWebView!!.isLongClickable = true
        mWebView!!.overScrollMode = View.OVER_SCROLL_NEVER
        mWebView!!.isVerticalScrollBarEnabled = false
        mWebView!!.isDrawingCacheEnabled = true
        mWebView!!.webViewClient = object : WebViewClient() {
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

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                return this@BaseWebFragment.shouldOverrideUrlLoading(view, url)
            }

            @TargetApi(21)
            override fun shouldInterceptRequest(view: WebView?, request: WebResourceRequest): WebResourceResponse? {
                return shouldInterceptRequest(view, request.url.toString())
            }

            override fun shouldInterceptRequest(view: WebView?, url: String?): WebResourceResponse? {
                return if (mSonicSession != null) {
                    val requestResource = mSonicSession!!.sessionClient.requestResource(url)
                    if (requestResource != null) {
                        requestResource as WebResourceResponse
                    } else {
                        super.shouldInterceptRequest(view, url)
                    }
                } else super.shouldInterceptRequest(view, url)
            }
        }

        // Android　4.0版本没有　WebChromeClient.FileChooserParams　这个类，所有要单独处理
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mWebView!!.webChromeClient = object : WebChromeClient() {
                /**
                 *　加载进度发生改变
                 */
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    if (isProgressBarLoading) {
                        val progressBar = getProgressBar() ?: return
                        progressBar.progress = newProgress
                        if (newProgress >= 100) {
                            ViewUtils.setGone(progressBar)
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
            }
        } else {
            mWebView!!.webChromeClient = object : WebChromeClient() {
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

        mWebView!!.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK
                        && mWebView?.canGoBack() == true && !isOriginalUrl && canBack()) {
                    mWebView?.goBack()
                    return@setOnKeyListener true
                }
            }
            false
        }
        val webSettings = mWebView!!.settings

        // add java script interface
        // note:if api level lower than 17(android 4.2), addJavascriptInterface has security
        // issue, please use x5 or see https://developer.android.com/reference/android/webkit/
        // WebView.html#addJavascriptInterface(java.lang.Object, java.lang.String)
        webSettings.javaScriptEnabled = true
        mWebView!!.removeJavascriptInterface("searchBoxJavaBridge_")
        mIntent?.putExtra(WebConstant.KEY_WEB_LOAD_URL_TIME, System.currentTimeMillis())

        // add interface
        addWebJsInteract(mSonicSessionClient, mIntent, mWebView)

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

    override fun getPageTitle(): String? {
        return mWebTitle
    }

    /**
     * 加载Url
     */
    open fun loadUrl(url: String?) {
        // webview is ready now, just tell session client to bind
        if (mSonicSessionClient != null && !canBack()) {
            mSonicSessionClient!!.bindWebView(mWebView!!, canBack())
            mSonicSessionClient!!.clientReady()
        } else { // default mode
            mWebView?.loadUrl(url)
        }
    }

    /**
     * 返回按键的处理
     */
    override fun onBackClick() {
        if (mWebView?.canGoBack() == true && !isOriginalUrl && canBack()) {
            mWebView?.goBack()
        } else {
            getPageActivity()?.onBackPressedSupport()
        }
    }

    /**
     * on loading start
     *
     * @param view
     * @param url
     * @param favicon
     */
    protected open fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        if (!isProgressBarLoading) {
            showPageLoading()
        }
    }

    /**
     * on loading finish
     *
     * @param view
     * @param url
     */
    protected open fun onPageFinished(view: WebView?, url: String?) {
        if (!isProgressBarLoading) {
            showPageContent()
        }
    }


    /**
     * get web title
     *
     * @param intent
     * @return
     */
    protected fun getWebTitle(intent: Intent?): String? {
        return intent?.getStringExtra(WebConstant.KEY_WEB_TITLE)
    }

    /**
     * get web url
     *
     * @param intent
     * @return
     */
    protected fun getWebUrl(intent: Intent?): String? {
        return intent?.getStringExtra(WebConstant.KEY_WEB_URL)
    }


    /**
     * H5内部是否可以返回，默认不可以返回
     */
    protected open fun canBack(): Boolean {
        return mIntent?.getBooleanExtra(WebConstant.KEY_WEB_BACK, false) ?: false
    }

    /**
     * add javascript interface
     *
     * @param sonicSessionClient
     * @param intent
     * @param webView
     */
    open fun addWebJsInteract(sonicSessionClient: SonicSessionClientImpl?, intent: Intent?, webView: WebView?) {}

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
     * 是不是第一个URl
     */
    private val isOriginalUrl: Boolean
        get() = mWebView?.copyBackForwardList()?.getItemAtIndex(0)?.url == mWebView?.copyBackForwardList()?.currentItem?.url


    /**
     * get web view
     *
     * @return
     */
    protected abstract fun getWebView(): WebView

    /**
     * get progressbar
     */
    protected open fun getProgressBar(): ProgressBar? = null


    /**
     * is clean sonic cache
     *
     * @return
     */
    protected open val isCleanCache: Boolean = false

    /**
     * 是否使用sonic加载h5
     */
    protected open val isSonicLoad: Boolean = true


    /**
     * 是否使用进度条来呈现加载中占位图
     * 默认使用进度天
     */
    protected open val isProgressBarLoading: Boolean = true


    override fun onDetach() {
        mSonicSession?.destroy()
        mSonicSession = null
        mWebView?.destroy()
        mWebView = null
        super.onDetach()
    }

}
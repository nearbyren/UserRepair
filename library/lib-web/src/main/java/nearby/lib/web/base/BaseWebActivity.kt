package nearby.lib.web.base

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.ProgressBar
import androidx.core.view.isVisible
import nearby.lib.base.activity.BaseActivity
import nearby.lib.base.app.ApplicationProvider
import nearby.lib.web.jsbride.Callback
import nearby.lib.web.jsbride.ConsolePipe
import nearby.lib.web.jsbride.Handler
import nearby.lib.web.jsbride.WebViewJavascriptBridge
import nearby.lib.web.util.BaseWebChromeClient
import nearby.lib.web.util.BaseWebViewClient
import nearby.lib.web.util.WebViewPool
import java.lang.reflect.InvocationTargetException

/**
 * @author:
 * @created on: 2022/7/29 16:27
 * @description:
 */
abstract class BaseWebActivity : BaseActivity() {

    /***
     * 关于jsbridge注入失败问题
     * 需要在以下方法注册,提升注入成功率
     * onPageStarted
     * onPageCommitVisible
     * onProgressChanged
     */
    companion object {
        const val currentJsBridge = "JsBridge"
        const val currentRoutine = "Routine"
    }


    protected val mWebJsBridge by lazy {
        WebViewPool.getInstance().getWebView()
    }
    protected val mWebRoutine by lazy {
        WebViewPool.getInstance().getWebView()
    }

    open fun webPageFinished(view: WebView?, url: String?) {}

    open fun webPageTitle(view: WebView?, url: String?) {}

    abstract fun addProgress(): ProgressBar?

    open var javascriptBridge: WebViewJavascriptBridge? = null

    //1.JsBridg 2.Routine
    var currentWebView: String = currentJsBridge

    //webView配置
    lateinit var settings: WebSettings

    //加载进度条
    var webProgress: ProgressBar? = null

    //web -> h5 回调
    var toWebViewCallBack: Callback? = null

    open fun initWebViewType(typeWebView: String) {
        currentWebView = typeWebView
        when (currentWebView) {
            currentJsBridge -> {
                setupJsBridge()
            }
            currentRoutine -> {
                setupRoutine()
            }
        }

    }

    override fun onBackPressed() {
        if (currentWebView == currentJsBridge) {
            if (mWebJsBridge.canGoBack()) {
                mWebJsBridge.goBack()
                return
            }
        } else if (currentWebView == currentRoutine) {
            if (mWebRoutine.canGoBack()) {
                mWebRoutine.goBack()
                return
            }
        }

        super.onBackPressed()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (currentWebView == currentJsBridge) {
                if (mWebJsBridge.canGoBack()) {
                    mWebJsBridge.goBack()
                    return true
                }
            } else if (currentWebView == currentRoutine) {
                if (mWebRoutine.canGoBack()) {
                    mWebRoutine.goBack()
                    return true
                }
            }

        }
        return super.onKeyDown(keyCode, event)
    }

    override fun initialize(savedInstanceState: Bundle?) {
        webProgress = addProgress()
        AndroidBug5497Workaround.assistActivity(this)
//        initWebViewType(currentWebView)
    }


    //Allow Cross Domain 跨域处理
    private fun setAllowUniversalAccessFromFileURLs(webView: WebView) {
        try {
            val clazz: Class<*> = webView.settings.javaClass
            val method = clazz.getMethod(
                "setAllowUniversalAccessFromFileURLs", Boolean::class.javaPrimitiveType
            )
            method.invoke(webView.settings, true)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
    }

    /***
     * 拨打电话
     */
    fun goCallCustomer(url: String) {
//        try {
//            val callDialog =
//                WebCallMobileDialog.newInstance(url)
//            callDialog.setGravity(Gravity.BOTTOM)
//            callDialog.setHeight(177.dpToPx)
//            callDialog.setAnimationRes(R.style.lib_uikit_anim_InBottom_OutBottom)
//            callDialog.show(activity = this@BaseWebActivity, "web_call")
//
//
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
    }

    /*******************************************普通WebView***********************************************/
    private fun setupRoutine() {
        mWebRoutine.let {
            it.webViewClient = RoutineWVClient()
            it.webChromeClient = RoutineWCClient()
            it.setLifecycleOwner(this)
            setAllowUniversalAccessFromFileURLs(it)
        }
    }

    inner class RoutineWVClient : BaseWebViewClient() {

        override fun onPageFinished(view: WebView, url: String) {
            webPageFinished(view, url)
            webProgress?.let {
                it.isVisible = false
            }
            super.onPageFinished(view, url)
        }

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            //拨打电话
            if (url.startsWith(WebView.SCHEME_TEL)) {
                goCallCustomer(url)
                return true
            }
            //短信、邮箱
            if (url.startsWith("sms:") ||
                    url.startsWith(WebView.SCHEME_MAILTO) ||
                    url.startsWith("bankabc://") ||
                    url.startsWith("bocom://") ||
                    url.startsWith("tmast://") ||
                    url.startsWith("weixin://")

            ) {
                try {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(url)
                    startActivity(intent)
                } catch (ignored: Exception) {
                }
                return true
            }
            return false
        }

        override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
            webProgress?.let {
                it.isVisible = true
            }
            super.onPageStarted(view, url, favicon)
        }
    }

    inner class RoutineWCClient : BaseWebChromeClient() {
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            webProgress?.let {
                it.isVisible = newProgress != 100
                it.progress = newProgress
            }
            super.onProgressChanged(view, newProgress)
        }

        override fun onReceivedTitle(view: WebView?, title: String?) {
            webPageTitle(view, title)
            super.onReceivedTitle(view, title)
        }
    }

    /*******************************************普通WebView***********************************************/


    /*******************************************普通JsBridgeWebView***********************************************/

    open fun registerJsBridgeWeb() {}

    private fun setupJsBridge() {
        mWebJsBridge.let {
            it.webViewClient = BridgeWVClient()
            it.webChromeClient = BridgeWCClient()
            it.setLifecycleOwner(this)
            setAllowUniversalAccessFromFileURLs(it)
            javascriptBridge =
                WebViewJavascriptBridge(_context = ApplicationProvider.appContext, _webView = it)

            javascriptBridge?.consolePipe = object : ConsolePipe {
                override fun post(string: String) {
//                    println("WebViewJavascriptBridge ->  post -> string = $string ")
                }
            }

            /***
             * 描述： Web 页面配置
             */
            javascriptBridge?.register("jsWebConfiguration", object : Handler {
                override fun handler(map: HashMap<String, Any>?, json: String, callback: Callback) {
                    toWebViewCallBack = callback
//                    println("WebViewJavascriptBridge ->  jsWebConfiguration map = $map - json = $json ")

                }
            })

            //注册  web -> native  native -> web 通信
            registerJsBridgeWeb()
        }
    }

    open fun parseScheme(url: String): Boolean {
        return if (url.contains("platformapi/startapp")) {
            true
        } else if (url.contains("web-other")) {
            false
        } else {
            false
        }
    }

    inner class BridgeWVClient : BaseWebViewClient() {

        override fun onPageFinished(view: WebView, url: String) {
//            println("WebViewJavascriptBridge ->  jsBridge  onPageFinished url = $url ")
            webPageFinished(view, url)
            webProgress?.let {
                it.isVisible = false
            }
            super.onPageFinished(view, url)
        }

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
//            println("WebViewJavascriptBridge ->  jsBridge  shouldOverrideUrlLoading injectJavascript $url")
            //拨打电话
            if (url.startsWith(WebView.SCHEME_TEL)) {
                goCallCustomer(url)
                return true
            }
            //短信、邮箱
            if (url.startsWith("sms:") ||
                    url.startsWith(WebView.SCHEME_MAILTO) ||
                    url.startsWith("bankabc://") ||
                    url.startsWith("bocom://") ||
                    url.startsWith("tmast://") ||
                    url.startsWith("weixin://")
            ) else {
                //处理支付宝小程序支付监听web跳转记录标记
                if (url.startsWith("alipays://platformapi/startapp?")) {
//                    StoreUtils.getInstance().put("web_zfb_mini_program", "web_zfb_mini_program")
                }
                try {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(url)
                    startActivity(intent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                return true
            }
            return false
        }

        override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
//            println("WebViewJavascriptBridge ->  jsBridge  onPageStarted injectJavascript ")
            webProgress?.let {
                it.isVisible = true
            }
            javascriptBridge?.injectJavascript()
            super.onPageStarted(view, url, favicon)
        }

        override fun onPageCommitVisible(view: WebView?, url: String?) {
//            println("WebViewJavascriptBridge ->  jsBridge   onPageCommitVisible injectJavascript ")
            javascriptBridge?.injectJavascript()
            super.onPageCommitVisible(view, url)
        }
    }

    inner class BridgeWCClient : BaseWebChromeClient() {
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            when (newProgress) {
                in 10..80 -> {
                    javascriptBridge?.injectJavascript()
                }
            }
            webProgress?.let {
                it.isVisible = newProgress != 100
                it.progress = newProgress
            }
            super.onProgressChanged(view, newProgress)
        }

        override fun onReceivedTitle(view: WebView?, title: String?) {
//            println("WebViewJavascriptBridge ->  jsBridge  onReceivedTitle  $title")
            webPageTitle(view, title)
            super.onReceivedTitle(view, title)
        }
    }

    /***
     * app - web 数据
     */
    private fun sendWebResData(code: Int, message: String, data: Any? = null, callback: Callback) {
        val result = HashMap<String, Any>()
        result["message"] = message
        result["code"] = code
        data?.let {
            result["data"] = data
        }
        callback.call(result)
    }

    private fun statusBarUi(isVisible: Boolean = true, backgColor: String = "#FFFFFF", titleColor: String = "#8f000000") {
        this.runOnUiThread {
            barHelper.util().toolBarRoot(isVisible, backgColor, titleColor)
        }
    }

    override fun onDestroy() {
        javascriptBridge?.removeJavascript()
        javascriptBridge = null
        super.onDestroy()
    }

    /*******************************************普通JsBridgeWebView***********************************************/
}
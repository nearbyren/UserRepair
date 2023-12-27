package nearby.lib.web.util

import android.content.Context
import android.content.MutableContextWrapper
import android.os.Looper
import android.view.ViewGroup
import java.util.*

/**
 * @author:
 * @created on: 2023/4/18 13:49
 * @description:
 */
class WebViewManager private constructor() {
    companion object {

        private const val TAG = "WebViewPool"

        @Volatile
        private var instance: WebViewManager? = null

        fun getInstance(): WebViewManager {
            return instance ?: synchronized(this) {
                instance ?: WebViewManager().also { instance = it }
            }
        }
    }

    private val webViewCache: MutableList<BaseWebView> = ArrayList(1)
    private fun create(context: Context): BaseWebView {
        val webView = BaseWebView(MutableContextWrapper(context))
        webView.webChromeClient = BaseWebChromeClient()
        webView.webViewClient = BaseWebViewClient()
        return webView
    }

    fun prepare(context: Context) {
        if (webViewCache.isEmpty()) {
            Looper.myQueue().addIdleHandler {
                val webView = create(MutableContextWrapper(context))
                webViewCache.add(webView)
                false
            }
        }
    }

    fun obtain(context: Context): BaseWebView {
        if (webViewCache.isEmpty()) {
            webViewCache.add(create(MutableContextWrapper(context)))
        }
        val webView = webViewCache.removeFirst()
        val contextWrapper = webView.context as MutableContextWrapper
        contextWrapper.baseContext = context
        webView.clearHistory()
        webView.resumeTimers()
        return webView
    }

    fun recycle(webView: BaseWebView) {
        try {
            webView.stopLoading()
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null)
            webView.clearHistory()
            webView.pauseTimers()
//            webView.webChromeClient = null
//            webView.webViewClient = WebViewClient()
            val parent = webView.parent
            if (parent != null) {
                (parent as ViewGroup).removeView(webView)
            }
            val contextWrapper = webView.context as MutableContextWrapper
            contextWrapper.baseContext = webView.context.applicationContext
            destroy()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (!webViewCache.contains(webView)) {
                webViewCache.add(webView)
            }
        }
    }

    private fun destroy() {
        try {
            webViewCache.forEach {
                it.removeAllViews()
                it.destroy()
                webViewCache.remove(it)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
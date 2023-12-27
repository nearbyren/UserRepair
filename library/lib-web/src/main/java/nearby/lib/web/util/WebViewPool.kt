package nearby.lib.web.util

import android.content.Context
import android.content.MutableContextWrapper
import nearby.lib.base.app.ApplicationProvider
import java.util.*

class WebViewPool private constructor() {

    companion object {

        @Volatile
        private var instance: WebViewPool? = null

        fun getInstance(): WebViewPool {
            return instance ?: synchronized(this) {
                instance ?: WebViewPool().also { instance = it }
            }
        }
    }

    private val sPool = Stack<BaseWebView>()
    private val lock = byteArrayOf()
    private var maxSize = 1

    /**
     * 设置 webview 池容量
     */
    fun setMaxPoolSize(size: Int) {
        synchronized(lock) { maxSize = size }
    }

    /**
     * 初始化webview 放在list中
     */
    fun init(context: Context, initSize: Int = maxSize) {
        for (i in 0 until initSize) {
            val view = BaseWebView(MutableContextWrapper(context))
            view.webChromeClient = BaseWebChromeClient()
            view.webViewClient = BaseWebViewClient()
            sPool.push(view)
        }
    }

    /**
     * 获取webview
     */
    fun getWebView(): BaseWebView {
        synchronized(lock) {
            val webView: BaseWebView
            if (sPool.size > 0) {
                webView = sPool.pop()
            } else {
                webView = BaseWebView(MutableContextWrapper(ApplicationProvider.appContext))
            }

            val contextWrapper = webView.context as MutableContextWrapper
            contextWrapper.baseContext = ApplicationProvider.appContext
            return webView
        }
    }

    /**
     * 回收 WebView
     */
    fun recycle(webView: BaseWebView) {
        // 释放资源
        webView.release()

        // 根据池容量判断是否销毁 【也可以增加其他条件 如手机低内存等等】
        val contextWrapper = webView.context as MutableContextWrapper
        contextWrapper.baseContext = webView.context.applicationContext
        synchronized(lock) {
            if (sPool.size < maxSize) {
                sPool.push(webView)
            } else {
                webView.destroy()
            }
        }
    }
}
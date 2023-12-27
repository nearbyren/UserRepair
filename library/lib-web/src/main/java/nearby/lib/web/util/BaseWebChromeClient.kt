package nearby.lib.web.util

import android.webkit.ConsoleMessage
import android.webkit.JsResult
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.appcompat.app.AlertDialog

open class BaseWebChromeClient : WebChromeClient() {


    /**
     * 网页控制台输入日志
     */
    override fun onConsoleMessage(consoleMessage: ConsoleMessage): Boolean {
        return super.onConsoleMessage(consoleMessage)
    }

    /**
     * 网页警告弹框
     */
    override fun onJsAlert(
        view: WebView,
        url: String,
        message: String,
        result: JsResult
    ): Boolean {
        AlertDialog.Builder(view.context)
            .setTitle("警告")
            .setMessage(message)
            .setPositiveButton("确认") { dialog, _ ->
                dialog?.dismiss()
                result.confirm()
            }
            .setNegativeButton("取消") { dialog, _ ->
                dialog?.dismiss()
                result.cancel()
            }
            .create()
            .show()
        return true
    }

    /**
     * 网页弹出确认弹窗
     */
    override fun onJsConfirm(
        view: WebView,
        url: String,
        message: String,
        result: JsResult
    ): Boolean {
        AlertDialog.Builder(view.context)
            .setTitle("警告")
            .setMessage(message)
            .setPositiveButton("确认") { dialog, _ ->
                dialog?.dismiss()
                result.confirm()
            }
            .setNegativeButton("取消") { dialog, _ ->
                dialog?.dismiss()
                result.cancel()
            }
            .create()
            .show()
        return true
    }
}
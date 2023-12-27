package nearby.lib.web.jsbride

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.webkit.JavascriptInterface
import android.webkit.WebView
import com.alibaba.fastjson.JSON
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader

class WebViewJavascriptBridge(_context: Context?, _webView: WebView?) {
    private var context: Context? = _context
    private var webView: WebView? = _webView
    var consolePipe: ConsolePipe? = null
    private var responseCallbacks: HashMap<String, Callback> = java.util.HashMap()
    private var messageHandlers: HashMap<String, Handler> = java.util.HashMap()
    private var uniqueId = 0

    init {
        setupBridge()
    }

    @SuppressLint("SetJavaScriptEnabled", "JavascriptInterface")
    fun setupBridge() {
        // 开启js支持
        webView!!.addJavascriptInterface(this, "normalPipe")
        webView!!.addJavascriptInterface(this, "consolePipe")
    }
    fun removeJavascript(){
        webView!!.removeJavascriptInterface("normalPipe")
        webView!!.removeJavascriptInterface("consolePipe")
        responseCallbacks.clear()
        messageHandlers.clear()
    }

    @JavascriptInterface
    fun postMessage(data: String?) {
        flush(data)
    }

    @JavascriptInterface
    fun receiveConsole(data: String?) {
        if (consolePipe != null) {
            consolePipe!!.post(data!!)
        }
    }

    fun injectJavascript() {
        val script = getFromAssets(context!!, "bridge.js")
        webView!!.loadUrl("javascript:$script")
        val script1 = getFromAssets(context!!, "hookConsole.js")
        webView!!.loadUrl("javascript:$script1")
    }

    fun register(handlerName: String?, handler: Handler?) {
        messageHandlers[handlerName!!] = handler!!
    }

    fun remove(handlerName: String?) {
        messageHandlers.remove(handlerName!!)
    }

    fun call(handlerName: String?, data: java.util.HashMap<String, Any>?, callback: Callback?) {
        val message = java.util.HashMap<String?, Any?>()
        message["handlerName"] = handlerName
        if (data != null) {
            message["data"] = data
        }
        if (callback != null) {
            uniqueId += 1
            val callbackId = "native_cb_$uniqueId"
            responseCallbacks[callbackId] = callback
            message["callbackId"] = callbackId
        }
        dispatch(message)
    }

    private fun flush(messageString: String?) {
        if (messageString == null) {
//            println("WebViewJavascriptBridge ->  give data is null")
            return
        }
        val gson = Gson()
        val message = gson.fromJson(
            messageString,
            java.util.HashMap::class.java
        )
        val responseId = message["responseId"] as String?
        /* app  - >  web */
        if (responseId != null) {
//            println("WebViewJavascriptBridge ->  app  - >  web")
            val callback = responseCallbacks[responseId]
//            val responseData = message["responseData"] as LinkedTreeMap<String, Any>
            val responseData = message["responseData"] as String
            val maps = flushMap(responseData)
            val response = HashMap<String, Any>()
            for ((key, value) in maps) {
                response[key] = value
            }
            callback!!.call(response)
//            println("WebViewJavascriptBridge ->  app  - >  web $response")
            responseCallbacks.remove(responseId)
        } else {
            /* web  - >  app */
//            println("WebViewJavascriptBridge ->  web - >  app")
            val callback: Callback
            val callbackID = message["callbackId"] as String?
            if (callbackID != null) {
                callback = object : Callback {
                    override fun call(map: HashMap<String, Any>?) {
                        val msg: java.util.HashMap<String?, Any?> =
                            java.util.HashMap<String?, Any?>()
                        msg["responseId"] = callbackID
                        msg["responseData"] = map
//                        println("WebViewJavascriptBridge ->  Callback = ${Gson().toJson(map)}")
                        dispatch(msg)
                    }
                }
            } else {
                callback = object : Callback {
                    override fun call(map: HashMap<String, Any>?) {
//                        println("WebViewJavascriptBridge ->  no logic ")
                    }
                }
            }
            val handlerName = message["handlerName"] as String?
            val handler = messageHandlers[handlerName!!]
            if (handler == null) {
                val error = String.format(
                    "NoHandlerException, No handler for message from JS:%s",
                    handlerName
                )
//                println("WebViewJavascriptBridge ->  $error ")
                return
            }
//            val treeMap = message["data"] as LinkedTreeMap<String, Any>
            val data = message["data"] as String
            val treeMap = flushMap(data)
            val response = HashMap<String, Any>()
            for ((key, value) in treeMap) {
                response[key] = value
            }
//            println("WebViewJavascriptBridge ->  $response")
            handler.handler(response, data, callback)
        }
    }

    private fun flushMap(jsonString: String?): HashMap<String, Any> {
        if (TextUtils.isEmpty(jsonString)) return HashMap()
        val jsonObject: JSONObject
        try {
            jsonObject = JSONObject(jsonString)
            val keys: Iterator<String> = jsonObject.keys()
            var key: String
            var value: Any
            val valueMap = HashMap<String, Any>()
            while (keys.hasNext()) {
                key = keys.next()
                value = jsonObject[key] as Any
                valueMap[key] = value
            }
            return valueMap
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return HashMap()
    }

    private fun dispatch(message: java.util.HashMap<String?, Any?>) {
        //处理有bug
        val jsonObject = JSON.toJSON(message)
        var messageString = jsonObject.toString()
//        println("WebViewJavascriptBridge ->  dispatch $messageString")
        messageString = messageString.replace("\\", "\\\\")
        messageString = messageString.replace("\"", "\\\"")
        messageString = messageString.replace("\'", "\\\'")
        messageString = messageString.replace("\n", "\\n")
        messageString = messageString.replace("\r", "\\r")
        messageString = messageString.replace("\u000C", "\\u000C")
        messageString = messageString.replace("\u2028", "\\u2028")
        messageString = messageString.replace("\u2029", "\\u2029")
        //执行 H5 jsBridge 通信
        val javascriptCommand =
            String.format("WebViewJavascriptBridge.handleMessageFromNative('%s');", messageString)
        webView!!.post { webView!!.evaluateJavascript(javascriptCommand, null) }
    }

    private fun getFromAssets(context: Context, fileName: String): String? {
        try {
            val inputReader = InputStreamReader(context.resources.assets.open(fileName))
            val bufReader = BufferedReader(inputReader)
            var line: String?
            var result: String? = ""
            while (bufReader.readLine().also { line = it } != null) result += line
            return result
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }
}
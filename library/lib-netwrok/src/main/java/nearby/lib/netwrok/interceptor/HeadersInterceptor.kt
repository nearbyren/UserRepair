package nearby.lib.netwrok.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import java.net.URLEncoder

/**
 * @description: 请求头部拦截器
 * @since: 1.0.0
 */
class HeadersInterceptor(private val headers: Map<String, String>) : Interceptor {

    //Okhttp header 中文异常解决方案
    private fun getValidUA(userAgent: String?): String {
        if (userAgent.isNullOrEmpty()) return ""
        val sb = StringBuilder()
        var i = 0
        val length = userAgent.length
        while (i < length) {
            val c = userAgent[i]
            if (c <= '\u001f' || c >= '\u007f') {//检测为不合法字符，就转为unicode 编码
                sb.append(String.format("\\u%04x", c.code))
            } else {
                sb.append(c)
            }
            i++
        }
        return sb.toString()
    }

    private fun getValueEncoded(value: String?): String? {
        if (value == null) return "null"
        val newValue = value.replace("\n", "")
        var i = 0
        val length = newValue.length
        while (i < length) {
            val c = newValue[i]
            if (c <= '\u001f' || c >= '\u007f') {
                return URLEncoder.encode(newValue, "UTF-8")
            }
            i++
        }
        return newValue
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        requestBuilder.addHeader("sourceType", "1")
        requestBuilder.addHeader("clientType", "2")
        return chain.proceed(requestBuilder.build())
    }
}
package nearby.lib.netwrok.response

import android.content.Context
import android.text.TextUtils
import com.google.gson.Gson
import nearby.lib.netwrok.base.HttpClient
import nearby.lib.netwrok.base.HttpClientConfig
import nearby.lib.netwrok.interceptor.HttpLoggingInterceptor
import java.lang.reflect.Type

/**
 * @description: -
 * @since: 1.0.0
 */
class CorHttp {
    private val httClient by lazy { HttpClient() }
    private val LOG_TAG = "CorHttp>>>"
    private val LOG_DIVIDER = "||================================================================="
    private var BASE_URL = "https://xxx.xxx.com"

    fun setBaseUrl(baseUrl: String) {
        if (TextUtils.isEmpty(baseUrl)) return
        this.BASE_URL = baseUrl

    }

    fun getBaseUrl(): String {
        return BASE_URL
    }

    companion object {

        @Volatile
        private var instance: CorHttp? = null

        @JvmStatic
        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: CorHttp().also { instance = it }
            }
    }


    fun init(context: Context) {
        val config = HttpClientConfig.builder()
                .setBaseUrl("${BASE_URL}/xxxxx/")
//                .setCache(Cache(File(context.cacheDir.toString() + "ENSDHttpCache"), 1024L * 1024 * 100))
                .openLog(false)
                .setGson(Gson())
                .setLogger(object : HttpLoggingInterceptor.Logger {
                    override fun log(message: String) {
                        if (message.contains("--> END") || message.contains("<-- END")) {
//                            LogUtil.d(message)
//                            LogUtil.e(LOG_DIVIDER)
                            LogUtils.e(LOG_TAG, "||  $message")
                            LogUtils.e(LOG_TAG, LOG_DIVIDER)
                        } else if (message.contains("-->") || message.contains("<--")) {
                            LogUtils.e(LOG_TAG, LOG_DIVIDER)
                            LogUtils.e(LOG_TAG, "||  $message")
//                            LogUtil.e(LOG_DIVIDER)
//                            LogUtil.d(message)
                        } else {
                            LogUtils.e(LOG_TAG, "||  $message")
//                        LogUtil.dJson(message)
                        }
                    }
                })
//                .addInterceptor(TokenInterceptor(context))
                .setHeaders(mapOf(Pair("ensd", "ejiayou")))
                .build()
        httClient.init(context, config)
    }

    fun getClient(): HttpClient {
        return httClient
    }

    @JvmOverloads
    suspend fun <T> get(
        url: String,
        headers: Map<String, String>? = null,
        params: Map<String, Any>? = null,
        type: Type,
        isInfoResponse: Boolean = true,
    ): ResponseHolder<T> = httClient.get(url, headers, params, type, isInfoResponse)


    @JvmOverloads
    suspend fun <T> post(
        url: String,
        headers: Map<String, String>? = null,
        params: Map<String, Any>? = null,
        type: Type,
        isInfoResponse: Boolean = true,
    ): ResponseHolder<T> = httClient.postJson(url, headers, params, type, isInfoResponse)


    @JvmOverloads
    suspend fun <T> postForm(
        url: String,
        headers: Map<String, String>? = null,
        params: Map<String, Any>? = null,
        type: Type,
        isInfoResponse: Boolean = true,
    ): ResponseHolder<T> = httClient.postForm(url, headers, params, type, isInfoResponse)


    @JvmOverloads
    suspend fun <T> postJsonArray(
        url: String,
        headers: Map<String, String>? = null,
        params: MutableList<Any>? = null,
        type: Type,
        isInfoResponse: Boolean = true,
    ): ResponseHolder<T> = httClient.postJsonArray(url, headers, params, type, isInfoResponse)


    @JvmOverloads
    suspend fun <T> postString(
        url: String,
        headers: Map<String, String>? = null,
        content: String? = null,
        type: Type,
        isInfoResponse: Boolean = true,
    ): ResponseHolder<T> = httClient.postJsonString(url, headers, content, type, isInfoResponse)
}
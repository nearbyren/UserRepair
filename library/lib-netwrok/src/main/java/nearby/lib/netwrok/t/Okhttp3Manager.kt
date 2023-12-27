package nearby.lib.netwrok.t

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.text.TextUtils
import com.alibaba.fastjson.JSON
import nearby.lib.base.app.ApplicationProvider
import nearby.lib.base.manager.MainThreadManager
import nearby.lib.netwrok.R
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okio.BufferedSink
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit


class Okhttp3Manager private constructor() {

    val mClient by lazy {
        //创建OkHttpClient对象
        OkHttpClient.Builder()
            .connectTimeout((15 * 1000).toLong(), TimeUnit.MILLISECONDS) //连接超时
            .readTimeout((15 * 1000).toLong(), TimeUnit.MILLISECONDS) //读取超时
            .writeTimeout((15 * 1000).toLong(), TimeUnit.MILLISECONDS) //写入超时
            .addInterceptor(AddAccessTokenInterceptor()) //拦截器用于设置header
            .addInterceptor(ReceivedAccessTokenInterceptor()) //拦截器用于接收并持久化cookie
            //                .addInterceptor(new GzipRequestInterceptor()) //开启Gzip压缩
//            .sslSocketFactory(SSLSocketManager.sslSocketFactory()) //配置（只有https请求需要配置）
            .hostnameVerifier(SSLSocketManager.hostnameVerifier()) //配置（只有https请求需要配置）
            //                .proxy(Proxy.NO_PROXY)
            .build()
    }

    val UPDATA = 100

    /**
     * 保证只有一个实例
     *
     * @return
     */
    companion object {
        private val TAG = Okhttp3Manager::class.java.simpleName

        @Volatile
        private var instance: Okhttp3Manager? = null
            get() {
                if (field == null) {
                    field = Okhttp3Manager()
                }
                return field
            }

        //Synchronized添加后就是线程安全的的懒汉模式
        @Synchronized
        @JvmStatic
        fun instance(): Okhttp3Manager {
            return instance!!
        }
    }

    /**
     * get请求，异步方式，是在子线程中执行的，需要切换到主线程才能更新UI
     *
     * @param url
     * @param onCommonSingleParamCallback
     */
    fun getAsync(
        url: String,
        onCommonSingleParamCallback: OnCommonSingleParamCallback<String>
    ) {
        //2.创建Request对象，设置一个url地址（百度地址）,设置请求方式。
        val request = Request.Builder().url(url).method("GET", null).build()
        //        Request request = new Request.Builder()
//                .url(url)
//                .get()//默认就是GET请求，可以不写（最好写上，要清晰表达出来）
//                .build()
        //3.创建一个call对象,参数就是Request请求对象
        val call = mClient.newCall(request)
        //4 执行Call
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("getAsync onFailure e*******$e")
                println("getAsync onFailure e detailMessage*******" + e.message)
                MainThreadManager {
                    onCommonSingleParamCallback.onError(
                         ApplicationProvider.appContext.resources.getString(R.string.library_network_sneak_off)
                    )
                }
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val responseString = response.body?.string()
                println(
                    "getAsync onResponse responseString*****$responseString"
                )
                MainThreadManager {
                    if (!TextUtils.isEmpty(responseString)) {
                        val baseResponse: BaseResponse<*>
                        baseResponse = try {
                            JSON.parseObject(
                                responseString,
                                BaseResponse::class.java
                            )
                        } catch (e: Exception) {
                            //如果不是标准json字符串，就返回错误提示
                            onCommonSingleParamCallback.onError(
                                 ApplicationProvider.appContext.resources.getString(
                                    R.string.library_server_sneak_off
                                )
                            )
                            return@MainThreadManager
                        }
                        onCommonSingleParamCallback.onSuccess(responseString)
                    } else {
                        onCommonSingleParamCallback.onError(
                             ApplicationProvider.appContext.resources.getString(
                                R.string.library_server_sneak_off
                            )
                        )
                    }
                }
            }
        })
    }

    /**
     * get请求，添加请求参数，异步方式，是在子线程中执行的，需要切换到主线程才能更新UI
     *
     * @param url
     * @param bodyParams
     * @param onCommonSingleParamCallback
     */
    fun getAsync(
        url: String,
        bodyParams: Map<String, String>,
        onCommonSingleParamCallback: OnCommonSingleParamCallback<String>
    ) {
        var urlNew = url
        // 设置HTTP请求参数
        urlNew += getBodyParams(bodyParams)
        //2.创建Request对象，设置一个url地址,设置请求方式。
        val request = Request.Builder().url(urlNew).get().build()
        //        Request request = new Request.Builder()
//                .url(url)
//                .get()//默认就是GET请求，可以不写（最好写上，要清晰表达出来）
//                .build()
        //3.创建一个call对象,参数就是Request请求对象
        val call = mClient.newCall(request)
        //4 执行Call
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("getAsync onFailure e*******$e")
                println("getAsync onFailure e detailMessage*******" + e.message)
                MainThreadManager {
                    onCommonSingleParamCallback.onError(
                         ApplicationProvider.appContext.resources.getString(R.string.library_network_sneak_off)
                    )
                }
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val responseString = response.body?.string()
                println(
                    "getAsync onResponse responseString*****$responseString"
                )
                MainThreadManager {
                    if (!TextUtils.isEmpty(responseString)) {
                        val baseResponse: BaseResponse<*>
                        baseResponse = try {
                            JSON.parseObject(
                                responseString,
                                BaseResponse::class.java
                            )
                        } catch (e: Exception) {
                            //如果不是标准json字符串，就返回错误提示
                            onCommonSingleParamCallback.onError(
                                 ApplicationProvider.appContext.resources.getString(
                                    R.string.library_server_sneak_off
                                )
                            )
                            return@MainThreadManager
                        }
                        onCommonSingleParamCallback.onSuccess(responseString)
                    } else {
                        onCommonSingleParamCallback.onError(
                             ApplicationProvider.appContext.resources.getString(
                                R.string.library_server_sneak_off
                            )
                        )
                    }
                }
            }
        })
    }

    /**
     * get请求，添加请求参数和header参数，异步方式，是在子线程中执行的，需要切换到主线程才能更新UI
     *
     * @param url
     * @param headerParams
     * @param bodyParams
     * @param onCommonSingleParamCallback
     */
    fun getAsync(
        url: String,
        headerParams: Map<String, String>,
        bodyParams: Map<String, String>,
        onCommonSingleParamCallback: OnCommonSingleParamCallback<String>
    ) {
        var urlNew = url
        // 设置HTTP请求参数
        urlNew += getBodyParams(bodyParams)
        val headers = setHeaderParams(headerParams)
        //2.创建Request对象，设置一个url地址,设置请求方式。
        val request = Request.Builder().url(urlNew).get().headers(headers).build()
        //        Request request = new Request.Builder()
//                .url(url)
//                .get()//默认就是GET请求，可以不写（最好写上，要清晰表达出来）
//                .build()
        //3.创建一个call对象,参数就是Request请求对象
        val call = mClient.newCall(request)
        //4 执行Call
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("getAsync onFailure e*******$e")
                println("getAsync onFailure e detailMessage*******" + e.message)
                MainThreadManager {
                    onCommonSingleParamCallback.onError(
                         ApplicationProvider.appContext.resources.getString(R.string.library_network_sneak_off)
                    )
                }
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val responseString = response.body?.string()
                println(
                    "getAsync onResponse responseString*****$responseString"
                )
                MainThreadManager {
                    if (!TextUtils.isEmpty(responseString)) {
                        val baseResponse: BaseResponse<*>
                        baseResponse = try {
                            JSON.parseObject(
                                responseString,
                                BaseResponse::class.java
                            )
                        } catch (e: Exception) {
                            //如果不是标准json字符串，就返回错误提示
                            onCommonSingleParamCallback.onError(
                                 ApplicationProvider.appContext.resources.getString(
                                    R.string.library_server_sneak_off
                                )
                            )
                            return@MainThreadManager
                        }
                        onCommonSingleParamCallback.onSuccess(responseString)
                    } else {
                        onCommonSingleParamCallback.onError(
                             ApplicationProvider.appContext.resources.getString(
                                R.string.library_server_sneak_off
                            )
                        )
                    }
                }
            }
        })
    }

    /**
     * 添加参数
     *
     * @param bodyParams
     * @return
     */
    private fun getBodyParams(bodyParams: Map<String, String>): String {
        //1.添加请求参数
        //遍历map中所有参数到builder
        return if (bodyParams.size > 0) {
            val stringBuffer = StringBuffer("?")
            for (key in bodyParams.keys) {
                if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(bodyParams[key])) {
                    //如果参数不是null并且不是""，就拼接起来
                    stringBuffer.append("&")
                    stringBuffer.append(key)
                    stringBuffer.append("=")
                    stringBuffer.append(bodyParams[key])
                }
            }
            stringBuffer.toString()
        } else {
            ""
        }
    }

    /**
     * 添加headers
     *
     * @param headerParams
     * @return
     */
    private fun setHeaderParams(headerParams: Map<String, String>): Headers {
        val headersbuilder = Headers.Builder()
        if (headerParams.size > 0) {
            for (key in headerParams.keys) {
                if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(headerParams[key])) {
                    //如果参数不是null并且不是""，就拼接起来
                    headersbuilder.add(key, headerParams[key]!!)
                }
            }
        }
        val headers = headersbuilder.build()
        return headers
    }

    /**
     * post请求提交字符串，异步方式，是在子线程中执行的，需要切换到主线程才能更新UI
     *
     * @param url
     * @param bodyParams
     * @param onCommonSingleParamCallback
     */
    fun postAsyncString(
        url: String,
        bodyParams: Map<String, String>,
        onCommonSingleParamCallback: OnCommonSingleParamCallback<String>
    ) {
        val jsonObject = JSONObject(bodyParams)
        val requestData = jsonObject.toString()
        println(
            "postAsyncString requestData*****$requestData"
        )
        val mediaType = "application/json charset=utf-8".toMediaTypeOrNull() //"类型,字节码"
        //2.通过RequestBody.create 创建requestBody对象
        val requestBody = RequestBody.create(mediaType, requestData)
        //3.创建Request对象，设置URL地址，将RequestBody作为post方法的参数传入
        val request = Request.Builder().url(url).post(requestBody).build()
        //4.创建一个call对象,参数就是Request请求对象
        val call = mClient.newCall(request)
        //4 执行Call
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println(
                    "postAsyncString onFailure e*******$e"
                )
                println("postAsyncString onFailure e detailMessage*******" + e.message)
                MainThreadManager {
                    onCommonSingleParamCallback.onError(
                         ApplicationProvider.appContext.resources.getString(R.string.library_network_sneak_off)
                    )
                }
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val responseString = response.body?.string()
                println(
                    "postAsyncString onResponse responseString*****$responseString"
                )
                MainThreadManager {
                    if (!TextUtils.isEmpty(responseString)) {
                        val baseResponse: BaseResponse<*>
                        baseResponse = try {
                            JSON.parseObject(
                                responseString,
                                BaseResponse::class.java
                            )
                        } catch (e: Exception) {
                            //如果不是标准json字符串，就返回错误提示
                            onCommonSingleParamCallback.onError(
                                 ApplicationProvider.appContext.resources.getString(
                                    R.string.library_server_sneak_off
                                )
                            )
                            return@MainThreadManager
                        }
                        onCommonSingleParamCallback.onSuccess(responseString)
                    } else {
                        onCommonSingleParamCallback.onError(
                             ApplicationProvider.appContext.resources.getString(
                                R.string.library_server_sneak_off
                            )
                        )
                    }
                }
            }
        })
    }

    /**
     * post请求提交流，异步方式，是在子线程中执行的，需要切换到主线程才能更新UI
     *
     * @param url
     * @param requestData
     * @param onCommonSingleParamCallback
     */
    fun postAsyncStream(
        url: String,
        requestData: String,
        onCommonSingleParamCallback: OnCommonSingleParamCallback<String>
    ) {
        println("requestData*****$requestData")

        //2.通过new RequestBody 创建requestBody对象
        val requestBody: RequestBody = object : RequestBody() {
            override fun contentType(): MediaType? {
                return "text/x-markdown charset=utf-8".toMediaTypeOrNull()
            }

            @Throws(IOException::class)
            override fun writeTo(sink: BufferedSink) {
                sink.writeUtf8(requestData)
            }
        }
        //3.创建Request对象，设置URL地址，将RequestBody作为post方法的参数传入
        val request = Request.Builder().url(url).post(requestBody).build()
        //4.创建一个call对象,参数就是Request请求对象
        val call = mClient.newCall(request)
        //4 执行Call
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println(
                    "postAsyncStream onFailure e*******$e"
                )
                println("postAsyncStream onFailure e detailMessage*******" + e.message)
                MainThreadManager {
                    onCommonSingleParamCallback.onError(
                         ApplicationProvider.appContext.resources.getString(R.string.library_network_sneak_off)
                    )
                }
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val responseString = response.body?.string()
                println(
                    "postAsyncStream onResponse responseString*****$responseString"
                )
                MainThreadManager {
                    if (!TextUtils.isEmpty(responseString)) {
                        val baseResponse: BaseResponse<*>
                        baseResponse = try {
                            JSON.parseObject(
                                responseString,
                                BaseResponse::class.java
                            )
                        } catch (e: Exception) {
                            //如果不是标准json字符串，就返回错误提示
                            onCommonSingleParamCallback.onError(
                                 ApplicationProvider.appContext.resources.getString(
                                    R.string.library_server_sneak_off
                                )
                            )
                            return@MainThreadManager
                        }
                        onCommonSingleParamCallback.onSuccess(responseString)
                    } else {
                        onCommonSingleParamCallback.onError(
                             ApplicationProvider.appContext.resources.getString(
                                R.string.library_server_sneak_off
                            )
                        )
                    }
                }
            }
        })
    }

    /**
     * post请求提交键值对，异步方式，是在子线程中执行的，需要切换到主线程才能更新UI
     *
     * @param url
     * @param bodyParams
     * @param onCommonSingleParamCallback
     */
    fun postAsyncKeyValuePairs(
        url: String,
        bodyParams: Map<String, String>,
        onCommonSingleParamCallback: OnCommonSingleParamCallback<String>
    ) {
        if (bodyParams.size > 0) {
            println(
                "postAsyncKeyValuePairs bodyParams*****$bodyParams"
            )
            println(
                "postAsyncKeyValuePairs bodyParams json*****" + MapManager.mapToJsonStr(bodyParams)
            )
        }

        //2.通过new FormBody()调用build方法,创建一个RequestBody,可以用add添加键值对
        val formEncodingBuilder = FormBody.Builder()
        //1.添加请求参数
        //遍历map中所有参数到builder
        if (bodyParams.isNotEmpty()) {
            for (key in bodyParams.keys) {
                if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(bodyParams[key])) {
                    //如果参数不是null，才把参数传给后台
                    bodyParams[key]?.let { formEncodingBuilder.add(key, it) }
                }
            }
        }

        //构建请求体
        val requestBody: RequestBody = formEncodingBuilder.build()
        //3.创建Request对象，设置URL地址，将RequestBody作为post方法的参数传入
        val request = Request.Builder().url(url).post(requestBody).build()
        //4.创建一个call对象,参数就是Request请求对象
        val call = mClient.newCall(request)
        //4 执行Call
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println(
                    "postAsyncKeyValuePairs onFailure e*******$e"
                )
                println(
                    "postAsyncKeyValuePairs onFailure e detailMessage*******" + e.message
                )
                MainThreadManager {
                    onCommonSingleParamCallback.onError(
                         ApplicationProvider.appContext.resources.getString(R.string.library_network_sneak_off)
                    )
                }
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val responseString = response.body?.string()
                println(
                    "postAsyncKeyValuePairs onResponse responseString*****$responseString"
                )
                MainThreadManager {
                    if (!TextUtils.isEmpty(responseString)) {
                        val baseResponse: BaseResponse<*>
                        baseResponse = try {
                            JSON.parseObject(
                                responseString,
                                BaseResponse::class.java
                            )
                        } catch (e: Exception) {
                            //如果不是标准json字符串，就返回错误提示
                            onCommonSingleParamCallback.onError(
                                 ApplicationProvider.appContext.resources.getString(
                                    R.string.library_server_sneak_off
                                )
                            )
                            return@MainThreadManager
                        }
                        onCommonSingleParamCallback.onSuccess(responseString)
                    } else {
                        onCommonSingleParamCallback.onError(
                             ApplicationProvider.appContext.resources.getString(
                                R.string.library_server_sneak_off
                            )
                        )
                    }
                }
            }
        })
    }

    /**
     * post请求不携带参数，异步方式，是在子线程中执行的，需要切换到主线程才能更新UI
     *
     * @param url
     * @param onCommonSingleParamCallback
     */
    fun postAsync(
        url: String,
        onCommonSingleParamCallback: OnCommonSingleParamCallback<String>
    ) {
        //这句话是重点Request
        //3.创建Request对象，设置URL地址，将RequestBody作为post方法的参数传入
        val request = Request.Builder().post(RequestBody.create(null, "")).url(url).build()
        //4.创建一个call对象,参数就是Request请求对象
        val call = mClient.newCall(request)
        //4 执行Call
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println(
                    "postAsyncKeyValuePairs onFailure e*******$e"
                )
                println(
                    "postAsyncKeyValuePairs onFailure e detailMessage*******" + e.message
                )
                MainThreadManager {
                    onCommonSingleParamCallback.onError(
                         ApplicationProvider.appContext.resources.getString(R.string.library_network_sneak_off)
                    )
                }
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val responseString = response.body?.string()
                println(
                    "postAsyncKeyValuePairs onResponse responseString*****$responseString"
                )
                MainThreadManager {
                    if (!TextUtils.isEmpty(responseString)) {
                        val baseResponse: BaseResponse<*>
                        baseResponse = try {
                            JSON.parseObject(
                                responseString,
                                BaseResponse::class.java
                            )
                        } catch (e: Exception) {
                            //如果不是标准json字符串，就返回错误提示
                            onCommonSingleParamCallback.onError(
                                 ApplicationProvider.appContext.resources.getString(
                                    R.string.library_server_sneak_off
                                )
                            )
                            return@MainThreadManager
                        }
                        onCommonSingleParamCallback.onSuccess(responseString)
                    } else {
                        onCommonSingleParamCallback.onError(
                             ApplicationProvider.appContext.resources.getString(
                                R.string.library_server_sneak_off
                            )
                        )
                    }
                }
            }
        })
    }

    /**
     * post请求上传Form表单，异步方式，提交表单，是在子线程中执行的，需要切换到主线程才能更新UI
     * 这个函数可以把服务器返回的数据统一处理
     *
     * @param url
     * @param bodyParams
     * @param onCommonSingleParamCallback
     */
    fun postAsyncForm(
        url: String,
        bodyParams: Map<String, String>,
        onCommonSingleParamCallback: OnCommonSingleParamCallback<String>
    ) {
        if (bodyParams.size > 0) {
            println(
                "postAsyncForm bodyParams String*******$bodyParams"
            )
            println(
                "postAsyncKeyValuePairs bodyParams json*****" + MapManager.mapToJsonStr(bodyParams)
            )
        }

        // form 表单形式上传
        val multipartBodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
        //1.添加请求参数
        //遍历map中所有参数到builder
        if (bodyParams.size > 0) {
            for (key in bodyParams.keys) {
                if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(bodyParams[key])) {
                    //如果参数不是null，才把参数传给后台
                    bodyParams[key]?.let { multipartBodyBuilder.addFormDataPart(key, it) }
                }
            }
        }

        //构建请求体
        val requestBody: RequestBody = multipartBodyBuilder.build()
        val request = Request.Builder().post(requestBody).url(url).build()
        //3 将Request封装为Call
        val call = mClient.newCall(request)
        //4 执行Call
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("postAsyncForm onFailure e*******$e")
                println("postAsyncForm onFailure e detailMessage*******" + e.message)
                MainThreadManager {
                    onCommonSingleParamCallback.onError(
                         ApplicationProvider.appContext.resources.getString(R.string.library_network_sneak_off)
                    )
                }
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
//                //服务器返回的是加密字符串，要解密
//                String dataEncrypt = response.body?.string()
//                LogManager.println("login onResponse dataEncrypt*****" + dataEncrypt)
                val responseString = response.body?.string()
                //                try {
//                    responseString = AESManager.aesDecrypt(dataEncrypt)
                println(
                    "postAsyncForm onResponse responseString*****$responseString"
                )
                MainThreadManager {
                    if (!TextUtils.isEmpty(responseString)) {
                        val baseResponse: BaseResponse<*>
                        baseResponse = try {
                            JSON.parseObject(
                                responseString,
                                BaseResponse::class.java
                            )
                        } catch (e: Exception) {
                            //如果不是标准json字符串，就返回错误提示
                            onCommonSingleParamCallback.onError(
                                 ApplicationProvider.appContext.resources.getString(
                                    R.string.library_server_sneak_off
                                )
                            )
                            return@MainThreadManager
                        }
                        onCommonSingleParamCallback.onSuccess(responseString)
                    } else {
                        onCommonSingleParamCallback.onError(
                             ApplicationProvider.appContext.resources.getString(
                                R.string.library_server_sneak_off
                            )
                        )
                    }
                }
                //                } catch (Exception e) {
//                    e.printStackTrace()
//                }
            }
        })
    }

    /**
     * post请求上传Form表单和图片文件（上传java服务器），异步方式，提交表单，是在子线程中执行的，需要切换到主线程才能更新UI
     * 这个函数可以把服务器返回的数据统一处理
     *
     * @param url
     * @param bodyParams
     * @param fileList
     * @param onCommonSingleParamCallback
     */
    fun postAsyncFormAndFiles(
        url: String,
        bodyParams: Map<String, String>,
        fileList: List<File>,
        onCommonSingleParamCallback: OnCommonSingleParamCallback<String>
    ) {
        if (bodyParams.size > 0) {
           println(
                "postAsyncForm bodyParams String*******$bodyParams"
            )
           println(
                "postAsyncKeyValuePairs bodyParams json*****" + MapManager.mapToJsonStr(bodyParams)
            )
        }
        //        MediaType MEDIA_TYPE = MediaType.parse("image/png")
        val MEDIA_TYPE = "image/*".toMediaTypeOrNull()
        // form 表单形式上传
        val multipartBodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
        //1.添加请求参数
        //遍历map中所有参数到builder
        if (bodyParams.isNotEmpty()) {
            for (key in bodyParams.keys) {
                if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(bodyParams[key])) {
                    //如果参数不是null，才把参数传给后台
                    bodyParams[key]?.let { multipartBodyBuilder.addFormDataPart(key, it) }
                }
            }
        }

        //遍历fileList中所有图片绝对路径到builder，并约定key如"upload"作为上传php服务器接受多张图片的key
        if (fileList.size > 0) {
            for (i in fileList.indices) {
                if (fileList[i].exists()) {
                    multipartBodyBuilder.addFormDataPart(
                        "upload", fileList[i].name, RequestBody.create(
                            MEDIA_TYPE,
                            fileList[i]
                        )
                    )
                }
            }
        } else {
            println("postAsyncFormAndFile fileList.size() = 0")
        }

        //构建请求体
        val requestBody: RequestBody = multipartBodyBuilder.build()
        val request = Request.Builder().post(requestBody).url(url).build()
        //3 将Request封装为Call
        val call = mClient.newCall(request)
        //4 执行Call
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println(
                    "postAsyncFormAndFiles onFailure e*******$e"
                )
                println(
                    "postAsyncFormAndFiles onFailure e detailMessage*******" + e.message
                )
                MainThreadManager {
                    onCommonSingleParamCallback.onError(
                         ApplicationProvider.appContext.resources.getString(R.string.library_network_sneak_off)
                    )
                }
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val responseString = response.body?.string()
                println(
                    "postAsyncFormAndFiles onResponse responseString*****$responseString"
                )
                MainThreadManager {
                    if (!TextUtils.isEmpty(responseString)) {
                        val baseResponse: BaseResponse<*>
                        baseResponse = try {
                            JSON.parseObject(
                                responseString,
                                BaseResponse::class.java
                            )
                        } catch (e: Exception) {
                            //如果不是标准json字符串，就返回错误提示
                            onCommonSingleParamCallback.onError(
                                 ApplicationProvider.appContext.resources.getString(
                                    R.string.library_server_sneak_off
                                )
                            )
                            return@MainThreadManager
                        }
                        onCommonSingleParamCallback.onSuccess(responseString)
                    } else {
                        onCommonSingleParamCallback.onError(
                             ApplicationProvider.appContext.resources.getString(
                                R.string.library_server_sneak_off
                            )
                        )
                    }
                }
            }
        })
    }

    /**
     * post请求上传Form表单和文件（上传java服务器），异步方式，提交表单，是在子线程中执行的，需要切换到主线程才能更新UI
     *
     * @param url
     * @param bodyParams
     * @param fileMap
     */
    fun postAsyncFormAndFiles(
        url: String,
        bodyParams: Map<String, String>,
        fileMap: Map<String, File>,
        filesMap: Map<String, List<File>>,
        onCommonSingleParamCallback: OnCommonSingleParamCallback<String>
    ) {
        if (bodyParams.size > 0) {
           println(
                "postAsyncForm bodyParams String*******$bodyParams"
            )
           println(
                "postAsyncKeyValuePairs bodyParams json*****" + MapManager.mapToJsonStr(bodyParams)
            )
        }
        val MEDIA_TYPE = "image/*".toMediaTypeOrNull()
        // form 表单形式上传
        val multipartBodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
        //1.添加请求参数
        //遍历map中所有参数到builder
        if (bodyParams.isNotEmpty()) {
            for (key in bodyParams.keys) {
                if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(bodyParams[key])) {
                    //如果参数不是null，才把参数传给后台
                    bodyParams[key]?.let { multipartBodyBuilder.addFormDataPart(key, it) }
                }
            }
        }

        //遍历fileMap中所有图片绝对路径到builder，并约定key如"upload[]"作为php服务器接受多张图片的key
        if (fileMap.size > 0) {
            for (key in fileMap.keys) {
                if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(bodyParams[key])) {
                    bodyParams[key]?.let {
                        //如果参数不是null，才把参数传给后台
                        multipartBodyBuilder.addFormDataPart(
                            key, fileMap[key]?.name, RequestBody.create(
                                MEDIA_TYPE,
                                it
                            )
                        )
                        println("fileMap.get(key).getName()*****" + fileMap[key]?.name
                        )
                    }
                }
            }
        }

        //遍历filesMap中所有图片绝对路径到builder，并约定key如"upload[]"作为php服务器接受多张图片的key
        if (filesMap.isNotEmpty()) {
            for (key in filesMap.keys) {
                if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(bodyParams[key])) {
                    //如果参数不是null，才把参数传给后台
                    filesMap[key]?.let {
                        if (filesMap[key] != null && it.size > 0) {
                            for (i in it.indices) {
                                multipartBodyBuilder.addFormDataPart(
                                    key, it[i].name, RequestBody.create(
                                        MEDIA_TYPE,
                                        it[i]
                                    )
                                )
                                println(
                                    "filesMap.get(key).get(i).getName()*****" + it[i].name
                                )
                            }
                        }
                    }
                }
            }
        }

        //构建请求体
        val requestBody: RequestBody = multipartBodyBuilder.build()
        val request = Request.Builder().post(requestBody).url(url).build()
        //3 将Request封装为Call
        val call = mClient.newCall(request)
        //4 执行Call
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println(
                    "postAsyncFormAndFiles onFailure e*******$e"
                )
                println(
                    "postAsyncFormAndFiles onFailure e detailMessage*******" + e.message
                )
                MainThreadManager {
                    onCommonSingleParamCallback.onError(
                         ApplicationProvider.appContext.resources.getString(R.string.library_network_sneak_off)
                    )
                }
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val responseString = response.body?.string()
               println(
                    "postAsyncFormAndFiles onResponse responseString*****$responseString"
                )
                MainThreadManager {
                    if (!TextUtils.isEmpty(responseString)) {
                        val baseResponse: BaseResponse<*>
                        baseResponse = try {
                            JSON.parseObject(
                                responseString,
                                BaseResponse::class.java
                            )
                        } catch (e: Exception) {
                            //如果不是标准json字符串，就返回错误提示
                            onCommonSingleParamCallback.onError(
                                 ApplicationProvider.appContext.resources.getString(
                                    R.string.library_server_sneak_off
                                )
                            )
                            return@MainThreadManager
                        }
                        onCommonSingleParamCallback.onSuccess(responseString)
                    } else {
                        onCommonSingleParamCallback.onError(
                             ApplicationProvider.appContext.resources.getString(
                                R.string.library_server_sneak_off
                            )
                        )
                    }
                }
            }
        })
    }

    /**
     * post请求上传Form表单和文件（上传php服务器），异步方式，提交表单，是在子线程中执行的，需要切换到主线程才能更新UI
     *
     * @param url
     * @param bodyParams
     * @param fileList
     * @param onCommonSingleParamCallback
     */
    fun postAsyncPhpFormAndFiles(
        url: String,
        bodyParams: Map<String, String>,
        fileList: List<File>,
        onCommonSingleParamCallback: OnCommonSingleParamCallback<String>
    ) {
        if (bodyParams.size > 0) {
           println(
                "postAsyncForm bodyParams String*******$bodyParams"
            )
           println(
                "postAsyncKeyValuePairs bodyParams json*****" + MapManager.mapToJsonStr(bodyParams)
            )
        }
        val MEDIA_TYPE = "image/png".toMediaTypeOrNull()
        // form 表单形式上传
        val multipartBodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
        //1.添加请求参数
        //遍历map中所有参数到builder
        if (bodyParams.isNotEmpty()) {
            for (key in bodyParams.keys) {
                if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(bodyParams[key])) {
                    //如果参数不是null，才把参数传给后台
                    bodyParams[key]?.let { multipartBodyBuilder.addFormDataPart(key, it) }
                }
            }
        }

        //遍历fileList中所有图片绝对路径到builder，并约定key如"upload[]"作为php服务器接受多张图片的key
        if (fileList.size > 0) {
            for (i in fileList.indices) {
                if (fileList[i].exists()) {
                    multipartBodyBuilder.addFormDataPart(
                        "upload[]", fileList[i].name, RequestBody.create(
                            MEDIA_TYPE,
                            fileList[i]
                        )
                    )
                }
            }
        }


        //构建请求体
        val requestBody: RequestBody = multipartBodyBuilder.build()
        val request = Request.Builder().post(requestBody).url(url).build()
        //3 将Request封装为Call
        val call = mClient.newCall(request)
        //4 执行Call
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println(
                    "postAsyncFormAndFiles onFailure e*******$e"
                )
                println(
                    "postAsyncFormAndFiles onFailure e detailMessage*******" + e.message
                )
                MainThreadManager {
                    onCommonSingleParamCallback.onError(
                         ApplicationProvider.appContext.resources.getString(R.string.library_network_sneak_off)
                    )
                }
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val responseString = response.body?.string()

                println(
                    "postAsyncFormAndFiles onResponse responseString*****$responseString"
                )
                MainThreadManager {
                    if (!TextUtils.isEmpty(responseString)) {
                        val baseResponse: BaseResponse<*>
                        baseResponse = try {
                            JSON.parseObject(
                                responseString,
                                BaseResponse::class.java
                            )
                        } catch (e: Exception) {
                            //如果不是标准json字符串，就返回错误提示
                            onCommonSingleParamCallback.onError(
                                 ApplicationProvider.appContext.resources.getString(
                                    R.string.library_server_sneak_off
                                )
                            )
                            return@MainThreadManager
                        }
                        onCommonSingleParamCallback.onSuccess(responseString)
                    } else {
                        onCommonSingleParamCallback.onError(
                             ApplicationProvider.appContext.resources.getString(
                                R.string.library_server_sneak_off
                            )
                        )
                    }
                }
            }
        })
    }

//    /**
//     * get请求下载文件，是在子线程中执行的，需要切换到主线程才能更新UI
//     *
//     * @param url
//     * @param bodyParams
//     * @param onCommonSingleParamCallback
//     */
//    public void getAppContentLength(String url,
//                                           Map<String, String> bodyParams,
//                                           OnCommonSingleParamCallback<Long> onCommonSingleParamCallback) {
//        println("bodyParams String*******" + bodyParams.toString())
//        Request request = new Request.Builder()
//                .get()
//                .url(url)
//                .build()
//        //3 将Request封装为Call
//        Call call = mClient.newCall(request)
//        //4 执行Call
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(@NonNull Call call, @NonNull IOException e) {
//                println("getAppContentLength onFailure e*******" + e.toString())
//                println("getAppContentLength onFailure e detailMessage*******" + e.getMessage())
//                MainThreadManager mainThreadManager = new MainThreadManager()
//                mainThreadManager.setOnSubThreadToMainThreadCallback(new OnSubThreadToMainThreadCallback() {
//                    @Override
//                    public void onSuccess() {
//                        onCommonSingleParamCallback.onError(context.getResources().getString(R.string.library_network_sneak_off))
//                    }
//                })
//                mainThreadManager.subThreadToUIThread()
//            }
//
//            @Override
//            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//                if (response != null && response.isSuccessful()) {
//                    long contentLength = response.body?.contentLength()
//                    println("getAppContentLength onResponse contentLength*******" + contentLength)
//                    MainThreadManager mainThreadManager = new MainThreadManager()
//                    mainThreadManager.setOnSubThreadToMainThreadCallback(new OnSubThreadToMainThreadCallback() {
//                        @Override
//                        public void onSuccess() {
//                            onCommonSingleParamCallback.onSuccess(contentLength)
//                        }
//                    })
//                    mainThreadManager.subThreadToUIThread()
//                }
//            }
//        })
//    }
//
//    /**
//     * get请求下载文件，是在子线程中执行的，需要切换到主线程才能更新UI
//     *
//     * @param url
//     * @param bodyParams
//     * @param onDownloadCallback
//     */
//    public void downloadApp(String url, Map<String, String> bodyParams, OnDownloadCallback<String> onDownloadCallback) {
//        println("downloadApp bodyParams String*******" + bodyParams.toString())
//        String alreadyDownloadLength = null
//        String appContentLength = null
//        //遍历map中所有参数到builder
//        if (bodyParams.size() > 0) {
//            if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(bodyParams.get(key))) {
//                    //如果参数不是null，才把参数传给后台
//                if ("alreadyDownloadLength".equals(key)) {
//                    alreadyDownloadLength = bodyParams.get(key)
//                } else if ("appContentLength".equals(key)) {
//                    appContentLength = bodyParams.get(key)
//                }
//            }
//        }
//
//        Request request = new Request.Builder()
//                .get()
//                //确定下载的范围,添加此头,则服务器就可以跳过已经下载好的部分
//                .addHeader("Range", "bytes=" + alreadyDownloadLength + "-" + appContentLength)
//                .url(url)
//                .build()
//        //3 将Request封装为Call
//        Call call = mClient.newCall(request)
//        //4 执行Call
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(@NonNull Call call, @NonNull IOException e) {
//                println("getAppContentLength onFailure e*******" + e.toString())
//                println("getAppContentLength onFailure e detailMessage*******" + e.getMessage())
//                MainThreadManager mainThreadManager = new MainThreadManager()
//                mainThreadManager.setOnSubThreadToMainThreadCallback(new OnSubThreadToMainThreadCallback() {
//                    @Override
//                    public void onSuccess() {
//                        onDownloadCallback.onError(context.getResources().getString(R.string.library_network_sneak_off))
//                    }
//                })
//                mainThreadManager.subThreadToUIThread()
//            }
//
//            @Override
//            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//                String alreadyDownloadLength = null
//                String appContentLength = null
//                String fileName = null
//                String FILEPATH = null
//                //遍历map中所有参数到builder
//                if (bodyParams.size() > 0) {
//                    for (String key : bodyParams.keySet()) {
//                        if ("alreadyDownloadLength".equals(key)) {
//                            alreadyDownloadLength = bodyParams.get(key)
//                            println("downloadApp alreadyDownloadLength*****" + alreadyDownloadLength)
//                        } else if ("appContentLength".equals(key)) {
//                            appContentLength = bodyParams.get(key)
//                            println("downloadApp appContentLength*****" + appContentLength)
//                        } else if ("fileName".equals(key)) {
//                            fileName = bodyParams.get(key)
//
//                            //查询数据库，看看有没有数据
//                            AppInfo appInfo = AppInfoDaoManager.getInstance(context).queryAppInfoById(1)
//                            if (appInfo != null) {
//                                println("versionUpdate appInfoList*****" + appInfo.toString())
//                                //如果存在数据，则更新这条数据
//                                appInfo.set_id(appInfo.get_id())
//                                appInfo.setFileName(fileName)
//                                AppInfoDaoManager.getInstance(context).updateAppInfo(appInfo)
//                            } else {
//                                appInfo = new AppInfo()
//                                appInfo.setFileName(fileName)
//                                //如果不存在数据，则插入这条数据
//                                AppInfoDaoManager.getInstance(context).insertAppInfo(appInfo)
//                            }
//                            println("downloadApp fileName*****" + fileName)
//                        } else if ("FILEPATH".equals(key)) {
//                            FILEPATH = bodyParams.get(key)
//                            println("downloadApp FILEPATH*****" + FILEPATH)
//                        }
//                    }
//                }
//                if (appContentLength != null && !"".equals(appContentLength) &&
//                        fileName != null && !"".equals(fileName) &&
//                        FILEPATH != null && !"".equals(FILEPATH)) {
//                    long alreadyDownloadLengthL = Long.valueOf(alreadyDownloadLength)
//                    long appContentLengthL = Long.valueOf(appContentLength)
//                    println("downloadApp alreadyDownloadLengthL*****" + alreadyDownloadLengthL)
//                    println("downloadApp appContentLengthL*****" + appContentLengthL)
//                    File dirs = new File(FILEPATH)
//                    if (!dirs.exists()) {
//                        println("downloadApp*****!dirs.exists()")
//                        dirs.mkdirs()
//                    }
//                    File file = new File(dirs, fileName)
//                    if (!file.exists()) {
//                        file.createNewFile()
//                        println("downloadApp*****!file.exists()")
//                    } else {
//                        println("downloadApp file.getAbsolutePath()*****" + file.getAbsolutePath())
//                    }
//
//                    if (file.exists() && appContentLengthL == file.length()) {
//                        //查询数据库，看看有没有数据
//                        AppInfo appInfo = AppInfoDaoManager.getInstance(context).queryAppInfoById(1)
//                        if (appInfo != null) {
//                            println("versionUpdate appInfoList*****" + appInfo.toString())
//                            //如果存在数据，则更新这条数据
//                            appInfo.set_id(appInfo.get_id())
//                            appInfo.setFileName(fileName)
//                            appInfo.setAppContentLength(appContentLengthL)
//                            AppInfoDaoManager.getInstance(context).updateAppInfo(appInfo)
//                        } else {
//                            appInfo = new AppInfo()
//                            appInfo.setFileName(fileName)
//                            appInfo.setAppContentLength(appContentLengthL)
//                            //如果不存在数据，则插入这条数据
//                            AppInfoDaoManager.getInstance(context).insertAppInfo(appInfo)
//                        }
//                        MainThreadManager mainThreadManager = new MainThreadManager()
//                        mainThreadManager.setOnSubThreadToMainThreadCallback(new OnSubThreadToMainThreadCallback() {
//                            @Override
//                            public void onSuccess() {
//                                onDownloadCallback.onDownloading(appContentLengthL)
//                                onDownloadCallback.onSuccess("")
//                            }
//                        })
//                        mainThreadManager.subThreadToUIThread()
//                    } else {
//                        InputStream inputStream = null
//                        BufferedInputStream bufferedInputStream = null
//                        FileOutputStream fileOutputStream = null
//                        BufferedOutputStream bufferedOutputStream = null
//                        try {
//                            inputStream = response.body?.byteStream()
//                            bufferedInputStream = new BufferedInputStream(inputStream)
//                            fileOutputStream = new FileOutputStream(file, false)
//                            bufferedOutputStream = new BufferedOutputStream(fileOutputStream)
//                            int bytesPerBuffer = 1024 * 1024
//                            byte[] buffer = new byte[bytesPerBuffer]//缓冲数组1024kB
//                            int len
//
//                            long alreadyDownloadLengthDownloading = alreadyDownloadLengthL
//                            while ((len = bufferedInputStream.read(buffer)) != -1) {
//                                bufferedOutputStream.write(buffer, 0, len)
//                                alreadyDownloadLengthDownloading += len
//                                if (Okhttp3Manager.isNetworkAvailable(context)) {//连网呢
//                                    long finalAlreadyDownloadLengthDownloading = alreadyDownloadLengthDownloading
//                                    MainThreadManager mainThreadManager = new MainThreadManager()
//                                    mainThreadManager.setOnSubThreadToMainThreadCallback(new OnSubThreadToMainThreadCallback() {
//                                        @Override
//                                        public void onSuccess() {
//                                            onDownloadCallback.onDownloading(finalAlreadyDownloadLengthDownloading)
//                                        }
//                                    })
//                                    mainThreadManager.subThreadToUIThread()
//                                    println("alreadyDownloadLengthDownloading=" + alreadyDownloadLengthDownloading)
//                                } else {//没连网呢
//                                    onDownloadCallback.onError(context.getResources().getString(R.string.library_please_check_the_network_connection))
//                                    break
//                                }
//                            }
//
//                            bufferedOutputStream.flush()
//                            if (file.exists()) {
//                                alreadyDownloadLengthDownloading = file.length()
//                            }
//                            if (alreadyDownloadLengthDownloading == appContentLengthL) {
//                                //查询数据库，看看有没有数据
//                                AppInfo appInfo = AppInfoDaoManager.getInstance(context).queryAppInfoById(1)
//                                if (appInfo != null) {
//                                    println("versionUpdate appInfoList*****" + appInfo.toString())
//                                    //如果存在数据，则更新这条数据
//                                    appInfo.set_id(appInfo.get_id())
//                                    appInfo.setFileName(fileName)
//                                    appInfo.setAppContentLength(alreadyDownloadLengthDownloading)
//                                    AppInfoDaoManager.getInstance(context).updateAppInfo(appInfo)
//                                } else {
//                                    appInfo = new AppInfo()
//                                    appInfo.setFileName(fileName)
//                                    appInfo.setAppContentLength(alreadyDownloadLengthDownloading)
//                                    //如果不存在数据，则插入这条数据
//                                    AppInfoDaoManager.getInstance(context).insertAppInfo(appInfo)
//                                }
//
//                                MainThreadManager mainThreadManager = new MainThreadManager()
//                                mainThreadManager.setOnSubThreadToMainThreadCallback(new OnSubThreadToMainThreadCallback() {
//                                    @Override
//                                    public void onSuccess() {
//                                        onDownloadCallback.onSuccess("")
//                                    }
//                                })
//                                mainThreadManager.subThreadToUIThread()
//                            } else {
//                                onDownloadCallback.onError("下载失败，请检查网络连接")
//                            }
//                        } finally {
//                            //关闭IO流
//                            IOManager.closeAll(bufferedInputStream, bufferedOutputStream)
//                        }
//                    }
//                } else {
//                    println("downloadApp*****" + "找不到文件名")
//                    onDownloadCallback.onError("找不到文件名")
//                }
//            }
//        })
//    }
//
//    /**
//     * get请求下载文件，是在子线程中执行的，需要切换到主线程才能更新UI
//     *
//     * @param url
//     * @param bodyParams
//     * @param callback
//     */
//    public void getDownloadApp(String url, Map<String, String> bodyParams, Callback callback) {
//        println("bodyParams String*******" + bodyParams.toString())
//        String alreadyDownloadLength = null
//        String appContentLength = null
//        //遍历map中所有参数到builder
//        if (bodyParams.size() > 0) {
//            for (String key : bodyParams.keySet()) {
//    if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(bodyParams.get(key))) {
//        //如果参数不是null，才把参数传给后台
//                if ("alreadyDownloadLength".equals(key)) {
//                    alreadyDownloadLength = bodyParams.get(key)
//                } else if ("appContentLength".equals(key)) {
//                    appContentLength = bodyParams.get(key)
//                }
//}
//            }
//        }
//
//        println("getDownloadApp alreadyDownloadLength*******" + alreadyDownloadLength)
//        println("getDownloadApp appContentLength*******" + appContentLength)
//        Request request = null
//        if (alreadyDownloadLength != null && !"".equals(alreadyDownloadLength)
//                && appContentLength != null && !"".equals(appContentLength)) {
//            request = new Request.Builder()
//                    //确定下载的范围,添加此头,则服务器就可以跳过已经下载好的部分
//                    .addHeader("range", "bytes=" + alreadyDownloadLength + "-" + appContentLength)
//                    .url(url)
//                    .build()
//        } else {
//            request = new Request.Builder()
//                    //确定下载的范围,添加此头,则服务器就可以跳过已经下载好的部分
////                .addHeader("RANGE", "bytes=" + alreadyDownloadLengthL + "-" + appContentLengthL)
//                    .url(url)
//                    .build()
//        }
//
//        //3 将Request封装为Call
//        Call call = mClient.newCall(request)
//        //4 执行Call
//        call.enqueue(callback)
//    }

    //    /**
    //     * get请求下载文件，是在子线程中执行的，需要切换到主线程才能更新UI
    //     *
    //     * @param url
    //     * @param bodyParams
    //     * @param onCommonSingleParamCallback
    //     */
    //    public void getAppContentLength(String url,
    //                                           Map<String, String> bodyParams,
    //                                           OnCommonSingleParamCallback<Long> onCommonSingleParamCallback) {
    //        println("bodyParams String*******" + bodyParams.toString())
    //        Request request = new Request.Builder()
    //                .get()
    //                .url(url)
    //                .build()
    //        //3 将Request封装为Call
    //        Call call = mClient.newCall(request)
    //        //4 执行Call
    //        call.enqueue(new Callback() {
    //            @Override
    //            public void onFailure(@NonNull Call call, @NonNull IOException e) {
    //                println("getAppContentLength onFailure e*******" + e.toString())
    //                println("getAppContentLength onFailure e detailMessage*******" + e.getMessage())
    //                MainThreadManager mainThreadManager = new MainThreadManager()
    //                mainThreadManager.setOnSubThreadToMainThreadCallback(new OnSubThreadToMainThreadCallback() {
    //                    @Override
    //                    public void onSuccess() {
    //                        onCommonSingleParamCallback.onError(context.getResources().getString(R.string.library_network_sneak_off))
    //                    }
    //                })
    //                mainThreadManager.subThreadToUIThread()
    //            }
    //
    //            @Override
    //            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
    //                if (response != null && response.isSuccessful()) {
    //                    long contentLength = response.body?.contentLength()
    //                    println("getAppContentLength onResponse contentLength*******" + contentLength)
    //                    MainThreadManager mainThreadManager = new MainThreadManager()
    //                    mainThreadManager.setOnSubThreadToMainThreadCallback(new OnSubThreadToMainThreadCallback() {
    //                        @Override
    //                        public void onSuccess() {
    //                            onCommonSingleParamCallback.onSuccess(contentLength)
    //                        }
    //                    })
    //                    mainThreadManager.subThreadToUIThread()
    //                }
    //            }
    //        })
    //    }
    //
    //    /**
    //     * get请求下载文件，是在子线程中执行的，需要切换到主线程才能更新UI
    //     *
    //     * @param url
    //     * @param bodyParams
    //     * @param onDownloadCallback
    //     */
    //    public void downloadApp(String url, Map<String, String> bodyParams, OnDownloadCallback<String> onDownloadCallback) {
    //        println("downloadApp bodyParams String*******" + bodyParams.toString())
    //        String alreadyDownloadLength = null
    //        String appContentLength = null
    //        //遍历map中所有参数到builder
    //        if (bodyParams.size() > 0) {
    //            if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(bodyParams.get(key))) {
    //                    //如果参数不是null，才把参数传给后台
    //                if ("alreadyDownloadLength".equals(key)) {
    //                    alreadyDownloadLength = bodyParams.get(key)
    //                } else if ("appContentLength".equals(key)) {
    //                    appContentLength = bodyParams.get(key)
    //                }
    //            }
    //        }
    //
    //        Request request = new Request.Builder()
    //                .get()
    //                //确定下载的范围,添加此头,则服务器就可以跳过已经下载好的部分
    //                .addHeader("Range", "bytes=" + alreadyDownloadLength + "-" + appContentLength)
    //                .url(url)
    //                .build()
    //        //3 将Request封装为Call
    //        Call call = mClient.newCall(request)
    //        //4 执行Call
    //        call.enqueue(new Callback() {
    //            @Override
    //            public void onFailure(@NonNull Call call, @NonNull IOException e) {
    //                println("getAppContentLength onFailure e*******" + e.toString())
    //                println("getAppContentLength onFailure e detailMessage*******" + e.getMessage())
    //                MainThreadManager mainThreadManager = new MainThreadManager()
    //                mainThreadManager.setOnSubThreadToMainThreadCallback(new OnSubThreadToMainThreadCallback() {
    //                    @Override
    //                    public void onSuccess() {
    //                        onDownloadCallback.onError(context.getResources().getString(R.string.library_network_sneak_off))
    //                    }
    //                })
    //                mainThreadManager.subThreadToUIThread()
    //            }
    //
    //            @Override
    //            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
    //                String alreadyDownloadLength = null
    //                String appContentLength = null
    //                String fileName = null
    //                String FILEPATH = null
    //                //遍历map中所有参数到builder
    //                if (bodyParams.size() > 0) {
    //                    for (String key : bodyParams.keySet()) {
    //                        if ("alreadyDownloadLength".equals(key)) {
    //                            alreadyDownloadLength = bodyParams.get(key)
    //                            println("downloadApp alreadyDownloadLength*****" + alreadyDownloadLength)
    //                        } else if ("appContentLength".equals(key)) {
    //                            appContentLength = bodyParams.get(key)
    //                            println("downloadApp appContentLength*****" + appContentLength)
    //                        } else if ("fileName".equals(key)) {
    //                            fileName = bodyParams.get(key)
    //
    //                            //查询数据库，看看有没有数据
    //                            AppInfo appInfo = AppInfoDaoManager.getInstance(context).queryAppInfoById(1)
    //                            if (appInfo != null) {
    //                                println("versionUpdate appInfoList*****" + appInfo.toString())
    //                                //如果存在数据，则更新这条数据
    //                                appInfo.set_id(appInfo.get_id())
    //                                appInfo.setFileName(fileName)
    //                                AppInfoDaoManager.getInstance(context).updateAppInfo(appInfo)
    //                            } else {
    //                                appInfo = new AppInfo()
    //                                appInfo.setFileName(fileName)
    //                                //如果不存在数据，则插入这条数据
    //                                AppInfoDaoManager.getInstance(context).insertAppInfo(appInfo)
    //                            }
    //                            println("downloadApp fileName*****" + fileName)
    //                        } else if ("FILEPATH".equals(key)) {
    //                            FILEPATH = bodyParams.get(key)
    //                            println("downloadApp FILEPATH*****" + FILEPATH)
    //                        }
    //                    }
    //                }
    //                if (appContentLength != null && !"".equals(appContentLength) &&
    //                        fileName != null && !"".equals(fileName) &&
    //                        FILEPATH != null && !"".equals(FILEPATH)) {
    //                    long alreadyDownloadLengthL = Long.valueOf(alreadyDownloadLength)
    //                    long appContentLengthL = Long.valueOf(appContentLength)
    //                    println("downloadApp alreadyDownloadLengthL*****" + alreadyDownloadLengthL)
    //                    println("downloadApp appContentLengthL*****" + appContentLengthL)
    //                    File dirs = new File(FILEPATH)
    //                    if (!dirs.exists()) {
    //                        println("downloadApp*****!dirs.exists()")
    //                        dirs.mkdirs()
    //                    }
    //                    File file = new File(dirs, fileName)
    //                    if (!file.exists()) {
    //                        file.createNewFile()
    //                        println("downloadApp*****!file.exists()")
    //                    } else {
    //                        println("downloadApp file.getAbsolutePath()*****" + file.getAbsolutePath())
    //                    }
    //
    //                    if (file.exists() && appContentLengthL == file.length()) {
    //                        //查询数据库，看看有没有数据
    //                        AppInfo appInfo = AppInfoDaoManager.getInstance(context).queryAppInfoById(1)
    //                        if (appInfo != null) {
    //                            println("versionUpdate appInfoList*****" + appInfo.toString())
    //                            //如果存在数据，则更新这条数据
    //                            appInfo.set_id(appInfo.get_id())
    //                            appInfo.setFileName(fileName)
    //                            appInfo.setAppContentLength(appContentLengthL)
    //                            AppInfoDaoManager.getInstance(context).updateAppInfo(appInfo)
    //                        } else {
    //                            appInfo = new AppInfo()
    //                            appInfo.setFileName(fileName)
    //                            appInfo.setAppContentLength(appContentLengthL)
    //                            //如果不存在数据，则插入这条数据
    //                            AppInfoDaoManager.getInstance(context).insertAppInfo(appInfo)
    //                        }
    //                        MainThreadManager mainThreadManager = new MainThreadManager()
    //                        mainThreadManager.setOnSubThreadToMainThreadCallback(new OnSubThreadToMainThreadCallback() {
    //                            @Override
    //                            public void onSuccess() {
    //                                onDownloadCallback.onDownloading(appContentLengthL)
    //                                onDownloadCallback.onSuccess("")
    //                            }
    //                        })
    //                        mainThreadManager.subThreadToUIThread()
    //                    } else {
    //                        InputStream inputStream = null
    //                        BufferedInputStream bufferedInputStream = null
    //                        FileOutputStream fileOutputStream = null
    //                        BufferedOutputStream bufferedOutputStream = null
    //                        try {
    //                            inputStream = response.body?.byteStream()
    //                            bufferedInputStream = new BufferedInputStream(inputStream)
    //                            fileOutputStream = new FileOutputStream(file, false)
    //                            bufferedOutputStream = new BufferedOutputStream(fileOutputStream)
    //                            int bytesPerBuffer = 1024 * 1024
    //                            byte[] buffer = new byte[bytesPerBuffer]//缓冲数组1024kB
    //                            int len
    //
    //                            long alreadyDownloadLengthDownloading = alreadyDownloadLengthL
    //                            while ((len = bufferedInputStream.read(buffer)) != -1) {
    //                                bufferedOutputStream.write(buffer, 0, len)
    //                                alreadyDownloadLengthDownloading += len
    //                                if (Okhttp3Manager.isNetworkAvailable(context)) {//连网呢
    //                                    long finalAlreadyDownloadLengthDownloading = alreadyDownloadLengthDownloading
    //                                    MainThreadManager mainThreadManager = new MainThreadManager()
    //                                    mainThreadManager.setOnSubThreadToMainThreadCallback(new OnSubThreadToMainThreadCallback() {
    //                                        @Override
    //                                        public void onSuccess() {
    //                                            onDownloadCallback.onDownloading(finalAlreadyDownloadLengthDownloading)
    //                                        }
    //                                    })
    //                                    mainThreadManager.subThreadToUIThread()
    //                                    println("alreadyDownloadLengthDownloading=" + alreadyDownloadLengthDownloading)
    //                                } else {//没连网呢
    //                                    onDownloadCallback.onError(context.getResources().getString(R.string.library_please_check_the_network_connection))
    //                                    break
    //                                }
    //                            }
    //
    //                            bufferedOutputStream.flush()
    //                            if (file.exists()) {
    //                                alreadyDownloadLengthDownloading = file.length()
    //                            }
    //                            if (alreadyDownloadLengthDownloading == appContentLengthL) {
    //                                //查询数据库，看看有没有数据
    //                                AppInfo appInfo = AppInfoDaoManager.getInstance(context).queryAppInfoById(1)
    //                                if (appInfo != null) {
    //                                    println("versionUpdate appInfoList*****" + appInfo.toString())
    //                                    //如果存在数据，则更新这条数据
    //                                    appInfo.set_id(appInfo.get_id())
    //                                    appInfo.setFileName(fileName)
    //                                    appInfo.setAppContentLength(alreadyDownloadLengthDownloading)
    //                                    AppInfoDaoManager.getInstance(context).updateAppInfo(appInfo)
    //                                } else {
    //                                    appInfo = new AppInfo()
    //                                    appInfo.setFileName(fileName)
    //                                    appInfo.setAppContentLength(alreadyDownloadLengthDownloading)
    //                                    //如果不存在数据，则插入这条数据
    //                                    AppInfoDaoManager.getInstance(context).insertAppInfo(appInfo)
    //                                }
    //
    //                                MainThreadManager mainThreadManager = new MainThreadManager()
    //                                mainThreadManager.setOnSubThreadToMainThreadCallback(new OnSubThreadToMainThreadCallback() {
    //                                    @Override
    //                                    public void onSuccess() {
    //                                        onDownloadCallback.onSuccess("")
    //                                    }
    //                                })
    //                                mainThreadManager.subThreadToUIThread()
    //                            } else {
    //                                onDownloadCallback.onError("下载失败，请检查网络连接")
    //                            }
    //                        } finally {
    //                            //关闭IO流
    //                            IOManager.closeAll(bufferedInputStream, bufferedOutputStream)
    //                        }
    //                    }
    //                } else {
    //                    println("downloadApp*****" + "找不到文件名")
    //                    onDownloadCallback.onError("找不到文件名")
    //                }
    //            }
    //        })
    //    }
    //
    //    /**
    //     * get请求下载文件，是在子线程中执行的，需要切换到主线程才能更新UI
    //     *
    //     * @param url
    //     * @param bodyParams
    //     * @param callback
    //     */
    //    public void getDownloadApp(String url, Map<String, String> bodyParams, Callback callback) {
    //        println("bodyParams String*******" + bodyParams.toString())
    //        String alreadyDownloadLength = null
    //        String appContentLength = null
    //        //遍历map中所有参数到builder
    //        if (bodyParams.size() > 0) {
    //            for (String key : bodyParams.keySet()) {
    //    if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(bodyParams.get(key))) {
    //        //如果参数不是null，才把参数传给后台
    //                if ("alreadyDownloadLength".equals(key)) {
    //                    alreadyDownloadLength = bodyParams.get(key)
    //                } else if ("appContentLength".equals(key)) {
    //                    appContentLength = bodyParams.get(key)
    //                }
    //}
    //            }
    //        }
    //
    //        println("getDownloadApp alreadyDownloadLength*******" + alreadyDownloadLength)
    //        println("getDownloadApp appContentLength*******" + appContentLength)
    //        Request request = null
    //        if (alreadyDownloadLength != null && !"".equals(alreadyDownloadLength)
    //                && appContentLength != null && !"".equals(appContentLength)) {
    //            request = new Request.Builder()
    //                    //确定下载的范围,添加此头,则服务器就可以跳过已经下载好的部分
    //                    .addHeader("range", "bytes=" + alreadyDownloadLength + "-" + appContentLength)
    //                    .url(url)
    //                    .build()
    //        } else {
    //            request = new Request.Builder()
    //                    //确定下载的范围,添加此头,则服务器就可以跳过已经下载好的部分
    ////                .addHeader("RANGE", "bytes=" + alreadyDownloadLengthL + "-" + appContentLengthL)
    //                    .url(url)
    //                    .build()
    //        }
    //
    //        //3 将Request封装为Call
    //        Call call = mClient.newCall(request)
    //        //4 执行Call
    //        call.enqueue(callback)
    //    }
    /**
     * post的请求参数，构造RequestBody
     *
     * @param bodyParams
     * @return
     */
    private fun setRequestBody(bodyParams: Map<String, String>): RequestBody {
        val formEncodingBuilder = FormBody.Builder()
        val iterator = bodyParams.keys.iterator()
        while (iterator.hasNext()) {
            val key = iterator.next()
            if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(bodyParams[key])) {
                //如果参数不是null，才把参数传给后台
                formEncodingBuilder.add(key, bodyParams[key]!!)
            }
        }
        val body = formEncodingBuilder.build()
        return body
    }

    /**
     * 判断网络是否可用
     *
     * @param context
     * @return
     */
    fun isNetworkAvailable(): Boolean {
        val connectivityManager =  ApplicationProvider.appContext
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        //如果仅仅是用来判断网络连接
        //connectivityManager.getActiveNetworkInfo().isAvailable()
        val info = connectivityManager.allNetworkInfo
        //            println("isNetworkAvailable*****" + info.toString())
        for (i in info.indices) {
            if (info[i].state == NetworkInfo.State.CONNECTED) {
                return true
            }
        }
        return false
    }

}
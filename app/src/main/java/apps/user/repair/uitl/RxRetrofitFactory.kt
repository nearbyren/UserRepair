package apps.user.repair.uitl


import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

/**
 * @author:
 * @created on: 2022/7/7 17:17
 * @description:
 */
class RxRetrofitFactory {
    companion object {

        private val okHttpClient: OkHttpClient =
            OkHttpClient.Builder()
//                .addInterceptor(RxOutLogInterceptor())
                .build()

        //普通json
        fun <T> createGson(clazz: Class<T>): T {
            return Retrofit.Builder()
                .baseUrl("https://xxx")
                // 添加Gson转换器
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build()
                .create(clazz)
        }
    }
}
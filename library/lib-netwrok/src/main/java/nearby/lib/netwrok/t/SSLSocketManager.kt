package nearby.lib.netwrok.t

import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.*

object SSLSocketManager {

    /**
     * 获取这个SSLSocketFactory
     *
     * @return
     */
    @JvmStatic
    fun sslSocketFactory(): SSLSocketFactory {
        return try {
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustManager(), SecureRandom())
            sslContext.socketFactory
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    /**
     * 获取trustManager
     *
     * @return
     */
    fun trustManager(): Array<TrustManager> {
        return arrayOf(
            object : X509TrustManager {
                override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
                override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf()
                }
            }
        )
    }

    /**
     * 获取hostnameVerifier
     *
     * @return
     */
    @JvmStatic
    fun hostnameVerifier(): HostnameVerifier {
        return HostnameVerifier { s, sslSession -> true }
    }

}
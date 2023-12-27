package nearby.lib.base.network


import android.annotation.SuppressLint
import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.os.Handler
import android.os.Looper
import nearby.lib.base.app.ApplicationProvider
import nearby.lib.base.network.NetworkUtils.getNetWorkState
import java.util.concurrent.CopyOnWriteArrayList

/**
 * @Author: let
 * @date: 2022/11/15 17:29
 * @description: 网络状态变化的监听类，根据android不同版本的系统，有 [ConnectivityManager.registerNetworkCallback]和注册广播两种实现方式；
 */
object NetworkListenerHelper {

    @Volatile
    private var mListenerList: CopyOnWriteArrayList<NetworkConnectedListener>? = null
    private var uiHandler = Handler(Looper.getMainLooper())
    private var myNetworkCallback = MyNetworkCallback()

    /**
     * 注册网络状态的监听；
     */
    fun registerNetworkListener() {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> {
                val connectivityManager =
                    ApplicationProvider.appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                connectivityManager.registerDefaultNetworkCallback(myNetworkCallback)
            }

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> {
                val connectivityManager =
                    ApplicationProvider.appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val builder: NetworkRequest.Builder = NetworkRequest.Builder()
                builder.addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                val networkRequest = builder.build()
                connectivityManager.registerNetworkCallback(networkRequest, myNetworkCallback)
            }

            else -> {
                // 通过广播的方式监听网络；
                val mNetworkBroadcastReceiver = NetworkBroadcastReceiver()
                val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
                ApplicationProvider.appContext.registerReceiver(mNetworkBroadcastReceiver, filter)
                mNetworkBroadcastReceiver.setBroadcastCallback(object :
                    NetworkBroadcastReceiver.NetworkBroadcastCallback {
                    override fun onNetworkBroadcastCallback(
                        isConnected: Boolean, networkStatus: NetworkStatus
                    ) {
                        uiHandler.post { notifyAllListeners(isConnected, networkStatus) }

                    }
                })
            }
        }
    }

    /**
     * 通知所有接收者；
     *
     * @param isConnected
     * @param networkStatus
     */
    private fun notifyAllListeners(isConnected: Boolean, networkStatus: NetworkStatus) {
        mListenerList?.let {
            if (it.size > 0) {
                for (listener in it) {
                    listener?.onNetworkConnected(isConnected, networkStatus)
                }
            }
        }
    }

    /**
     * 添加回调的监听者；
     */
    @Synchronized
    fun addListener(listener: NetworkConnectedListener?) {
        if (listener == null) {
            return
        }
        if (mListenerList == null) {
            mListenerList = CopyOnWriteArrayList()
        }
        // 防止重复添加；
        mListenerList?.let {
            if (!it.contains(listener)) {
                it.add(listener)
            }
        }
    }

    /**
     * 移除某个回调实例；
     *
     * @param listener
     */
    @Synchronized
    fun removeListener(listener: NetworkConnectedListener) {
        mListenerList?.let {
            if (it.size > 0) {
                it.remove(listener)
            }
        }
    }

    fun unregisterNetworkCallback() {
        val connectivityManager =
            ApplicationProvider.appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            connectivityManager.unregisterNetworkCallback(myNetworkCallback)
        }
    }

    interface NetworkConnectedListener {
        /**
         * @param isConnected
         * @param networkStatus
         */
        fun onNetworkConnected(isConnected: Boolean, networkStatus: NetworkStatus)
    }

    @SuppressLint("NewApi")
    private class MyNetworkCallback : NetworkCallback() {
        //当用户与网络连接（或断开连接）（可以是WiFi或蜂窝网络）时，这两个功能均作为默认回调;
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            // 需要同步获取一次网络状态；
            val netWorkState = getNetWorkState(ApplicationProvider.appContext)
            uiHandler.post { notifyAllListeners(true, netWorkState) }
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            // 需要同步获取一次网络状态；
            val netWorkState = getNetWorkState(ApplicationProvider.appContext)
            uiHandler.post { notifyAllListeners(false, netWorkState) }
        }

        override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
            super.onCapabilitiesChanged(network, networkCapabilities)

            // 表示能够和互联网通信（这个为true表示能够上网）
            if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
                when {
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    //网络状态监听 onCapabilitiesChanged#网络类型为wifi
                    }

                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    //网络状态监听 onCapabilitiesChanged#蜂窝网络
                    }

                    else -> {
                        //网络状态监听 onCapabilitiesChanged#其他网络
                    }
                }
            }
                //网络状态监听 onCapabilitiesChanged#network=$network
        }
    }

}
package nearby.lib.base.network


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.text.TextUtils

/**
 * @Author: let
 * @date: 2021/11/15 17:28
 * @description: 网络状态的监听广播
 */
class NetworkBroadcastReceiver : BroadcastReceiver() {
    private var mBroadcastCallback: NetworkBroadcastCallback? = null
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == null) {
            return
        }
        val action = intent.action
        if (TextUtils.equals(intent.action, ConnectivityManager.CONNECTIVITY_ACTION)) {
            // 申请权限；
//        if (!XXPermissions.isGrantedPermission(context, Permission.WRITE_EXTacERNAL_STORAGE,
//                Permission.READ_EXTERNAL_STORAGE)) {
//        }
//        NetworkInfo mobNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
//        NetworkInfo wifiNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//        if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
//            //WIFI和移动网络均未连接
//            netContentListener.netContent(false);
//        } else {
//            //WIFI连接或者移动网络连接
//            netContentListener.netContent(true);
//        }
            val isOnline = NetworkUtils.isOnline(context)
            val networkStatus = NetworkUtils.getNetWorkState(context)
            if (mBroadcastCallback != null) {
                mBroadcastCallback!!.onNetworkBroadcastCallback(isOnline, networkStatus)
            }
        }
    }

    fun setBroadcastCallback(broadcastCallback: NetworkBroadcastCallback?) {
        mBroadcastCallback = broadcastCallback
    }

    interface NetworkBroadcastCallback {
        /**
         * 根据监听的结果返回连接状态和网络状态；
         *
         * @param isConnected
         * @param networkStatus
         */
        fun onNetworkBroadcastCallback(isConnected: Boolean, networkStatus: NetworkStatus)
    }
}
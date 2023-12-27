package apps.user.repair.service

import android.content.Context
import com.tencent.android.tpush.XGPushBaseReceiver
import com.tencent.android.tpush.XGPushClickedResult
import com.tencent.android.tpush.XGPushRegisterResult
import com.tencent.android.tpush.XGPushShowedResult
import com.tencent.android.tpush.XGPushTextMessage
import nearby.lib.signal.livebus.LiveBus

class MessageXGPushBaseReceiver : XGPushBaseReceiver() {
    override fun onRegisterResult(context: Context?, p1: Int, p2: XGPushRegisterResult?) {
        println("推送测试 onRegisterResult ")
        //註冊成功的位置
    }

    override fun onUnregisterResult(context: Context?, p1: Int) {
        println("推送测试 onUnregisterResult ")
    }

    override fun onSetTagResult(context: Context?, p1: Int, p2: String?) {
        println("推送测试 onSetTagResult ")
    }

    override fun onDeleteTagResult(context: Context?, p1: Int, p2: String?) {
        println("推送测试 onDeleteTagResult ")
    }

    override fun onSetAccountResult(context: Context?, p1: Int, p2: String?) {
        println("推送测试 onSetAccountResult ")
    }

    override fun onDeleteAccountResult(context: Context?, p1: Int, p2: String?) {
        println("推送测试 onDeleteAccountResult ")
    }

    override fun onSetAttributeResult(context: Context?, p1: Int, p2: String?) {
        println("推送测试 onSetAttributeResult ")
    }

    override fun onQueryTagsResult(context: Context?, p1: Int, p2: String?, p3: String?) {
        println("推送测试 onQueryTagsResult ")
    }

    override fun onDeleteAttributeResult(context: Context?, p1: Int, p2: String?) {
        println("推送测试 onDeleteAttributeResult ")
    }

    override fun onTextMessage(context: Context?, p1: XGPushTextMessage?) {
        println("推送测试 onTextMessage ")
    }

    override fun onNotificationClickedResult(context: Context?, p1: XGPushClickedResult?) {
        println("推送测试 onNotificationClickedResult ")
        //點擊處理的位置
        context?.let {
            LiveBus.get("tab").post("tab")
        }

    }

    override fun onNotificationShowedResult(context: Context?, p1: XGPushShowedResult?) {
        println("推送测试 onNotificationShowedResult ")
        //接收消息的位置

    }
}
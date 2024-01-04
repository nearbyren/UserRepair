package apps.user.repair


import com.tencent.android.tpush.XGIOperateCallback
import com.tencent.android.tpush.XGPushConfig
import com.tencent.android.tpush.XGPushManager
import com.xsj.crasheye.Crasheye
import nearby.lib.base.app.ApplicationProvider
import nearby.lib.base.app.ModuleInit
import nearby.lib.base.app.ModuleInitDelegate
import nearby.lib.netwrok.response.CorHttp

class RepairApplication : ApplicationProvider() {

    init {
        ModuleInitDelegate.register(ModuleInit())
    }
    override fun onCreate() {
        super.onCreate()
        // Obtain the FirebaseAnalytics instance.
        CorHttp.getInstance().init(this)
        Crasheye.init(this, "ad9a3v6t")
        XGPushConfig.enableDebug(this,BuildConfig.DEBUG);
        XGPushManager.registerPush(this,object : XGIOperateCallback {
            override fun onSuccess(p0: Any?, p1: Int) {
              println("注册成功，设备token为 $p0 - p1  $p1")
            }

            override fun onFail(p0: Any?, errCode: Int, msg: String?) {
                println("注册失败，错误码：$errCode,错误信息：$msg")
            }
        })
    }
}
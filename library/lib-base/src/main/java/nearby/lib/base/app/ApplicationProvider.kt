package nearby.lib.base.app

import android.app.Application
import android.content.Context

/**
 * @author:
 * @created on: 2022/10/21 10:29
 * @description:
 */
open class ApplicationProvider : Application() {

    companion object {
        // 全局共享的 Application
        lateinit var appContext: Application
    }

    override fun onCreate() {
        super.onCreate()
        appContext = this
        ModuleInitDelegate.reorder()
        ModuleInitDelegate.onCreate()
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        ModuleInitDelegate.attachBaseContext(base)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        ModuleInitDelegate.onLowMemory()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        ModuleInitDelegate.onTrimMemory(level)
    }

    override fun onTerminate() {
        super.onTerminate()
        ModuleInitDelegate.onTerminate()
    }

    open fun environment(): String {
        return ""
    }
}

package nearby.lib.base.app

import android.content.Context

/**
 * @author:
 * @created on: 2022/10/21 10:28
 * @description:
 */
interface IModuleInit {
    fun onCreate() {}
    fun attachBaseContext(base: Context) {}
    fun onLowMemory() {}
    fun onTrimMemory(level: Int) {}
    fun onTerminate() {}
}

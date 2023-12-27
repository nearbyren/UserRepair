package nearby.lib.base.app

import android.content.Context

/**
 * @author:
 * @created on: 2022/10/21 10:29
 * @description:
 */
object ModuleInitDelegate : IModuleInit {

    private val moduleList = mutableListOf<IModuleInit>()

    fun register(vararg modules: IModuleInit) {
        moduleList.addAll(modules)
    }

    fun reorder() {
        moduleList.sortBy { (it as BaseModuleInit).priority }
    }

    override fun onCreate() {
        moduleList.forEach { it.onCreate() }
    }

    override fun attachBaseContext(base: Context) {
        moduleList.forEach { it.attachBaseContext(base) }
    }

    override fun onLowMemory() {
        moduleList.forEach { it.onLowMemory() }
    }

    override fun onTrimMemory(level: Int) {
        moduleList.forEach { it.onTrimMemory(level) }
    }

    override fun onTerminate() {
        moduleList.forEach { it.onTerminate() }
    }
}

package nearby.lib.base.app

/**
 * @author:
 * @created on: 2022/10/21 10:30
 * @description:
 */
class ModuleInit : BaseModuleInit() {

    override fun onCreate() {
        val context = ApplicationProvider.appContext

        println("ModuleInit onCreate 例子")
    }

    override fun onLowMemory() {
        super.onLowMemory()
        println("ModuleInit onLowMemory 例子")
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        println("ModuleInit onTrimMemory 例子")
    }
}

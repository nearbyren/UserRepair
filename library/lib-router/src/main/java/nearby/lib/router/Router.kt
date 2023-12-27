package nearby.lib.router

import android.app.Application
import android.net.Uri
import com.alibaba.android.arouter.launcher.ARouter
import kotlin.properties.Delegates

/**
 * @description: ARouter工具类
 * @since: 1.0.0
 */
object Router {


    private var openRouter: Boolean = false
    fun isOpen(): Boolean {
        return openRouter
    }
    fun init(context: Application, debug: Boolean) {
        if (debug) {
            ARouter.openLog()
            ARouter.openDebug()
        }
        openRouter = true
        ARouter.init(context)
    }

    fun inject(what: Any) {
        ARouter.getInstance().inject(what)
    }

    fun path(path: String): PostcardWrapper {
        return PostcardWrapper(path)
    }

    fun uri(uri: Uri): PostcardWrapper {
        return PostcardWrapper(uri)
    }
}
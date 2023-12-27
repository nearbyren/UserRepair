package nearby.lib.router

import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.callback.NavigationCallback

/**
 * @description: 简单的路由跳转回调
 * @since: 1.0.0
 */
abstract class SimpleRouteCallback : NavigationCallback {

    override fun onArrival(postcard: Postcard?) {
        after()
    }

    override fun onFound(postcard: Postcard?) {
        before()
    }

    override fun onInterrupt(postcard: Postcard?) {
        // do nothing
        interrupt()
    }

    override fun onLost(postcard: Postcard?) {
        after()
    }

    /**
     * this method means startActivity before，sometimes would not be called
     */
    abstract fun before()

    /**
     * this method means startActivity after ,include onLost and onArrival
     */
    abstract fun after()

    abstract fun interrupt()
}
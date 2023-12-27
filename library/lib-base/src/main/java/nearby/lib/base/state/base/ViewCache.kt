package nearby.lib.base.state.base

import java.util.*

object ViewCache {
    private val mCache: MutableMap<Class<out BaseStateView?>?, BaseStateView?> =
        HashMap<Class<out BaseStateView?>?, BaseStateView?>()

    operator fun get(stateClazz: Class<out BaseStateView>?): BaseStateView? {
        val stateView: BaseStateView = mCache[stateClazz] ?: return add(stateClazz)
        return stateView
    }

    /**
     * 创建状态页面后，将母版放置到缓存队列，下次使用时可以通过原型直接clone
     */
    fun add(stateClazz: Class<out BaseStateView?>?): BaseStateView? {
        var stateView: BaseStateView? = null
        try {
            stateView = stateClazz?.newInstance()
            mCache[stateClazz] = stateView
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return stateView
    }

    /**
     * 创建状态页面后，将母版放置到缓存队列，下次使用时可以通过原型直接clone
     */
    fun add(stateView: BaseStateView): BaseStateView {
        mCache[stateView.javaClass] = stateView
        return stateView
    }
}

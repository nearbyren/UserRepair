package nearby.lib.base.state

import android.app.Activity
import android.view.View
import nearby.lib.base.state.base.BaseStateView
import nearby.lib.base.state.base.ViewCache
import nearby.lib.base.state.mask.MaskView
import nearby.lib.base.state.mask.MaskedView
import nearby.lib.base.state.target.Target
import nearby.lib.base.state.target.ViewTarget

class StateBox private constructor() {
    private var config: Config? = null
    fun setConfig(config: Config?) {
        this.config = config
    }

    fun inject(target: View, listener: OnReloadListener?): BoxManager {
        println("测试 inject ")
        val maskView = MaskView(target, listener) //构造遮罩层
        val maskedView = MaskedView(target, listener) //构造被遮罩层
        val targetContext: Target = ViewTarget(target, maskView, maskedView)
        targetContext.replaceView()
        return BoxManager(targetContext, config)
    }

    fun inject(activity: Activity, viewId: Int, listener: OnReloadListener?): BoxManager {
        val target = activity.findViewById<View>(viewId)
        println("target $target")
        return inject(target, listener)
    }

    fun inject(views: View, viewId: Int, listener: OnReloadListener?): BoxManager {
        println("views $views")
        val target = views.findViewById<View>(viewId)
        return inject(target, listener)
    }

    /**
     * 全局配置
     */
    class Config {
        private var defaultPageState: Class<out BaseStateView?>? = null //初始默认界面
        fun addStateView(state: Class<out BaseStateView?>?): Config {
            if (state != null) {
                ViewCache.add(state)
            }
            return this
        }

        fun addStateView(state: Class<out BaseStateView?>?, isDefault: Boolean): Config {
            if (state != null) {
                ViewCache.add(state)
            }
            if (isDefault) {
                defaultPageState = state
            }
            return this
        }

        fun getDefaultPageState(): Class<out BaseStateView?>? {
            return defaultPageState
        }
    }

    interface OnReloadListener {
        fun onReload(v: View?)
    }

    companion object {
        val default = StateBox()
    }
}

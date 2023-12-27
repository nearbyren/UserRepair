package nearby.lib.base.state

import nearby.lib.base.state.base.BaseStateView
import nearby.lib.base.state.base.ViewCache
import nearby.lib.base.state.mask.MaskView
import nearby.lib.base.state.mask.MaskedView
import nearby.lib.base.state.target.Target
import nearby.lib.base.state.util.StateBoxUtil

class BoxManager internal constructor(target: Target, config: StateBox.Config?) {
    private val maskView: MaskView
    private val maskedView: MaskedView

    /**
     * 应用系统页面全局配置信息
     *
     * @param config 配置参数
     */
    private fun appConfig(config: StateBox.Config) {
        if (StateBoxUtil.checkNotNull(maskView, maskedView)) {
            println("测试 appConfig show ")
            config.getDefaultPageState()?.let { maskView.show(it, maskedView) }
        }
    }

    /**
     * 隐藏遮罩层
     */
    fun hidden() {
        println("测试 hidden")
        if (StateBoxUtil.checkNotNull(maskView, maskedView)) {
            maskView.show(MaskedView::class.java, maskedView)
        }
    }

    /**
     * 按照类名显示遮罩层里已有的状态
     *
     * @param state class
     */
    fun show(state: Class<out BaseStateView?>?) {
        println("测试 show")
        if (StateBoxUtil.checkNotNull(maskView)) {
            if (state != null) {
                maskView.show(state, maskedView)
            }
        }
    }

    /**
     * 返回遮罩层
     */
    fun getMaskView(): MaskView {
        return maskView
    }

    /**
     * 往遮罩层里添加页面状态
     *
     * @param state 页面状态
     * @return StateService
     */
    fun addStatePage(state: BaseStateView?): BoxManager {
        if (state != null) {
            ViewCache.add(state)
        }
        return this
    }

    /**
     * 构造方法
     *
     * @param target 操作目标
     * @param config 默认配置
     */
    init {
        maskView = target.maskView
        maskedView = target.maskedView
        if (StateBoxUtil.checkNotNull(config)) {
            println("测试 appConfig ")
            appConfig(config!!)
        }
    }
}

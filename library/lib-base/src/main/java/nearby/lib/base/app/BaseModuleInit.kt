package nearby.lib.base.app

/**
 * @author:
 * @created on: 2022/10/21 10:28
 * @description:
 */
abstract class BaseModuleInit: IModuleInit {
    /**
     * 模块初始化优先级 越高初始化越快
     * Module中设置优先级越小越优先初始化
     */
    open val priority: Int = 0
}

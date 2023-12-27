package nearby.lib.router.component

/**
 * @author:
 * @created on: 2022/8/29 18:08
 * @description:整体组件回调处理
 *
 */
interface ComponentCallbackHandling<T> {

    fun callback(result: T)
}
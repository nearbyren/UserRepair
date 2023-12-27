package nearby.lib.uikit.widgets

import android.view.View

/**
 * @description: View相关扩展
 * @since: 1.0.0
 */

fun <T : View> T.click(action: (T) -> Unit) {
    setOnClickListener {
        action(this)
    }
}

fun <T : View> T.longClick(action: (T) -> Boolean) {
    setOnLongClickListener {
        action(this)
    }
}

/**d
 * 带有限制快速点击的点击事件
 */
fun <T : View> T.singleClick(interval: Long = 500L, action: ((T) -> Unit)?) {
    setOnClickListener(SingleClickListener(interval, action))
}

class SingleClickListener<T : View>(
    private val interval: Long = 500L,
    private var clickFunc: ((T) -> Unit)?
) : View.OnClickListener {
    private var lastClickTime = 0L

    override fun onClick(v: View) {
        val nowTime = System.currentTimeMillis()
        if (nowTime - lastClickTime > interval) {
            // 单次点击事件
            clickFunc?.invoke(v as T)
            lastClickTime = nowTime
        }
    }
}
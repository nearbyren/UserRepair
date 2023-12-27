package nearby.lib.base.uitl

import androidx.annotation.StringRes


data class ToastEvent(
    var content: String? = null, // Toast内容字符串
    @StringRes var contentResId: Int? = null, // Toast内容字符串Id
    var showLong: Boolean = false // 是否长时间显示
)
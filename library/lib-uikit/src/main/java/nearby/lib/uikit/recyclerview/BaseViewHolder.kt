package nearby.lib.uikit.recyclerview

import android.util.SparseArray
import android.view.View
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView.ViewHolder

/**
 * @description: ViewHolder的基类
 * @since: 1.0.0
 */
@Suppress("UNCHECKED_CAST")
class BaseViewHolder(itemView: View) : ViewHolder(itemView) {
    private val views: SparseArray<View>

    init {
        views = SparseArray()
    }

    fun <T : View> getView(@IdRes resId: Int): T {
        var v: View? = views.get(resId)
        if (v == null) {
            v = itemView.findViewById(resId)
            views.put(resId, v)
        }
        return v as T
    }
}
package nearby.lib.uikit.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

/**
 * @description: 多种Type的RecyclerView适配器
 * @since: 1.0.0
 */
abstract class BaseMultiRecyclerAdapter : RecyclerView.Adapter<BaseViewHolder>() {

    var context: Context? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        this.context =  parent.context
        val itemView =
            LayoutInflater.from(context).inflate(getLayoutId(viewType), parent, false)
        return BaseViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        onBindItem(holder, getItemViewType(position), position)
    }

    abstract @LayoutRes
    fun getLayoutId(viewType: Int): Int

    abstract fun onBindItem(holder: BaseViewHolder, viewType: Int, position: Int)
}
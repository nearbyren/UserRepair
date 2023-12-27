package nearby.lib.base.network

import android.content.Context
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import nearby.lib.base.R
import nearby.lib.base.state.base.BaseStateView

class NetworkStateView  : BaseStateView() {
    override fun onCreateView(): Int {
        return R.layout.layout_network
    }

    override fun onViewCreate(context: Context?, view: View?) {
        super.onViewCreate(context, view)
        get<AppCompatTextView>(R.id.tv_retry).setOnClickListener {
            getOnReloadListener()?.onReload(it)
        }
    }
}
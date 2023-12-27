package nearby.lib.base.loading

import android.content.Context
import android.view.View
import nearby.lib.base.R
import nearby.lib.base.state.base.BaseStateView

class LoadingStateView : BaseStateView() {

    override fun onCreateView(): Int {
        return R.layout.layout_loading
    }

    override fun onViewCreate(context: Context?, view: View?) {
        super.onViewCreate(context, view)
    }

}
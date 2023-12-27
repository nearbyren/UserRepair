package nearby.lib.base.state.mask

import android.view.View
import nearby.lib.base.state.StateBox
import nearby.lib.base.state.base.BaseStateView

class MaskedView(view: View, onReloadListener: StateBox.OnReloadListener?) : BaseStateView(view, view.context, onReloadListener) {
    protected override fun onCreateView(): Int {
        return 0
    }

    fun hide() {
        obtainRootView()?.setVisibility(View.INVISIBLE)
    }

    fun show() {
        obtainRootView()?.setVisibility(View.VISIBLE)
    }
}

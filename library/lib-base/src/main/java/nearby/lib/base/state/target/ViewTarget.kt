package nearby.lib.base.state.target

import android.view.View
import android.view.ViewGroup
import nearby.lib.base.state.mask.MaskView
import nearby.lib.base.state.mask.MaskedView
import nearby.lib.base.state.util.StateBoxUtil

class ViewTarget(
    //初始界面
    override var view: View,
    //遮罩层
    override var maskView: MaskView, maskedView: MaskedView) : Target {
    //被遮罩层
    override val maskedView: MaskedView

    override fun replaceView() {
        if (StateBoxUtil.checkNotNull(view, maskView)) {
            val parentView = view.parent
            val childIndex =
                if (parentView == null) -1 else (parentView as ViewGroup).indexOfChild(view)
            if (childIndex >= 0) {
                (parentView as ViewGroup).removeViewAt(childIndex) //移除需要被屏蔽的view
                maskView.setupSuccessLayout(maskedView)
                parentView.addView(maskView, childIndex, view.layoutParams)
            } else {
                maskView.setupSuccessLayout(maskedView)
            }
        }
    }

    init {
        maskView = maskView
        this.maskedView = maskedView
    }
}



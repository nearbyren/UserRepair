package nearby.lib.base.state.target

import android.view.View
import nearby.lib.base.state.mask.MaskView
import nearby.lib.base.state.mask.MaskedView

interface Target {
    fun replaceView()
    val maskView: MaskView
    val view: View
    val maskedView: MaskedView
}

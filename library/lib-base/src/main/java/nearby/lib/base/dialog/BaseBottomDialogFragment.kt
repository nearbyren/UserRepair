package nearby.lib.base.dialog

import android.view.Gravity
import nearby.lib.base.R

/**
 * @description: 底部弹出框
 * @since: 1.0.0
 */
abstract class BaseBottomDialogFragment : BaseDialogFragment() {

    init {
        setGravity(Gravity.BOTTOM)
        setAnimationRes(R.style.lib_uikit_anim_InBottom_OutBottom)
    }
}
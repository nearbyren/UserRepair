package nearby.lib.base.loading

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import nearby.lib.base.R
import nearby.lib.base.databinding.LoadingDialogBinding
import nearby.lib.base.dialog.BaseBindDialogFragment

/**
 * @description: 加载弹框
 * @since: 1.0.0
 */
class LoadingDialog : BaseBindDialogFragment<LoadingDialogBinding>() {
    private var cancelAction: (() -> Unit)? = null

    override fun getDialogStyle(): Int {
        return STYLE_NO_FRAME
    }

    override fun getDialogTheme(): Int {
        return R.style.lib_uikit_TransparentDialog
    }

    override fun getLayoutId(): Int {
        return R.layout.loading_dialog
    }

    override fun initialize(view: View, savedInstanceState: Bundle?) {
//        dialog?.setCancelable(false)
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.setOnCancelListener { cancelAction?.invoke() }
    }

    override fun show(activity: FragmentActivity, tag: String?) {
        super.show(activity, tag)
        binding.loadingView.start()
    }

    override fun show(fragment: Fragment, tag: String?) {
        super.show(fragment, tag)
        binding.loadingView.start()
    }

    override fun dismiss() {
        binding.loadingView.stop()
        binding.loadingView.clearAnimation()
        dialog?.setOnCancelListener(null)
        super.dismiss()
    }

    override fun dismissAllowingStateLoss() {
        binding.loadingView.stop()
        binding.loadingView.clearAnimation()
        dialog?.setOnCancelListener(null)
        super.dismissAllowingStateLoss()
    }

    fun setCancelAction(action: (() -> Unit)? = null) {
        this.cancelAction = action
    }
}
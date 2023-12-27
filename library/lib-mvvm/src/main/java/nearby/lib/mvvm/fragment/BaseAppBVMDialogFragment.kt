package nearby.lib.mvvm.fragment

import androidx.databinding.ViewDataBinding
import nearby.lib.base.loading.LoadingDialog
import nearby.lib.mvvm.vm.BaseViewModel
import java.lang.ref.WeakReference

/**
 * @author:
 * @created on: 2022/9/9 12:55
 * @description:
 */
abstract class BaseAppBVMDialogFragment<B : ViewDataBinding, VM : BaseViewModel> :
    BaseBVMDialogFragment<B, VM>() {

    private val weakFragment by lazy { WeakReference(LoadingDialog()) }

//    private val loadingDialog by lazy { LoadingDialog() }
//    private val loadingDialog by lazy { LoadingIosDialog() }

    override fun showLoadingView(isShow: Boolean) {
        try {
            if (isShow) {
                if (isVisible && !(weakFragment.get()?.isAdded)!!) {
                    weakFragment.get()?.show(this)
                }
            } else {
                if (isVisible) {
                    weakFragment.get()?.dismissAllowingStateLoss()
                }
            }
        } catch (e: Exception) {

        }
    }
   /* override fun showLoadingView(isShow: Boolean) {
        try {
            if (isShow) {
                if (isVisible && !loadingDialog.isAdded) {
                    loadingDialog.show(this)
                }
            } else {
                if (isVisible) {
                    loadingDialog.dismissAllowingStateLoss()
                }
            }
        } catch (e: Exception) {

        }
    }*/
}
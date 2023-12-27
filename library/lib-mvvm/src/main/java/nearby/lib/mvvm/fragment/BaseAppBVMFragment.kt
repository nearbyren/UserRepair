package nearby.lib.mvvm.fragment

import androidx.databinding.ViewDataBinding
import nearby.lib.base.loading.LoadingDialog
import nearby.lib.mvvm.vm.BaseViewModel
import java.lang.ref.WeakReference

/**
 * @description: -
 * @since: 1.0.0
 */
abstract class BaseAppBVMFragment<B : ViewDataBinding, VM : BaseViewModel> :
    BaseBVMFragment<B, VM>() {
    private val weakFragment by lazy { WeakReference(LoadingDialog()) }
//    private val loadingDialog by lazy { LoadingDialog() }

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
     }
 */


}


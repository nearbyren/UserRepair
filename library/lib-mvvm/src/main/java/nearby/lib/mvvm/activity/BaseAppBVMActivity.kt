package nearby.lib.mvvm.activity

import androidx.databinding.ViewDataBinding
import nearby.lib.base.loading.LoadingDialog
import nearby.lib.mvvm.vm.BaseViewModel
import java.lang.ref.WeakReference

/**
 * @author: lr
 * @created on: 2022/7/10 2:36 下午
 * @description:
 */
abstract class BaseAppBVMActivity<B : ViewDataBinding, VM : BaseViewModel> :
    BaseBVMActivity<B, VM>() {
    private val weakFragment by lazy { WeakReference(LoadingDialog()) }

//    private val loadingDialog by lazy { LoadingDialog() }


    override fun showLoadingView(isShow: Boolean) {
         try {
             if (isShow) {
                 if (!isFinishing && !(weakFragment.get()?.isAdded)!!) {
                     weakFragment.get()?.show(this)
                 }
             } else {
                 if (!isFinishing) {

                     weakFragment.get()?.dismissAllowingStateLoss()
                 }
             }
         } catch (e: Exception) {
         }
     }
   /* override fun showLoadingView(isShow: Boolean) {
        try {
            if (isShow) {
                if (!isFinishing && !loadingDialog.isAdded) {
                    loadingDialog.show(this)
                }
            } else {
                if (!isFinishing) {

                    loadingDialog.dismissAllowingStateLoss()
                }
            }
        } catch (e: Exception) {
        }
    }*/

}
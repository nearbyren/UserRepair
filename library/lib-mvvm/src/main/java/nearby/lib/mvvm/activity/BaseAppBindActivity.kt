package nearby.lib.mvvm.activity

import androidx.databinding.ViewDataBinding
import nearby.lib.base.loading.LoadingDialog
import nearby.lib.mvvm.activity.bind.BaseBindActivity
import java.lang.ref.WeakReference

/**
 * @author: lr
 * @created on: 2022/7/10 11:13 上午
 * @description:
 */

abstract class BaseAppBindActivity<B : ViewDataBinding> : BaseBindActivity<B>() {

    private val weakFragment by lazy { WeakReference(LoadingDialog()) }

//    private val loadingDialog by lazy { LoadingDialog() }
//    private val loadingDialog by lazy { LoadingIosDialog() }

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
    }
*/
}
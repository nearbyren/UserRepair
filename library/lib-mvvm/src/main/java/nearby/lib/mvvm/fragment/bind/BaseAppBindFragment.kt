package nearby.lib.mvvm.fragment.bind

import androidx.databinding.ViewDataBinding
import nearby.lib.base.loading.LoadingDialog
import java.lang.ref.WeakReference

/**
 * @author: lr
 * @created on: 2022/7/10 3:33 下午
 * @description:
 */
abstract class BaseAppBindFragment<B : ViewDataBinding> : BaseBindFragment<B>() {

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
    /*override fun showLoadingView(isShow: Boolean) {
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
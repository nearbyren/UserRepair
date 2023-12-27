package nearby.lib.mvvm.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import nearby.lib.base.dialog.BaseBindDialogFragment2
import nearby.lib.base.exts.observeNonNull
import nearby.lib.mvvm.vm.BaseViewModel

/**
 * @author:
 * @created on: 2022/9/9 12:38
 * @description:
 */
abstract class BaseBVMDialogFragment<B : ViewDataBinding, VM : BaseViewModel> : BaseBindDialogFragment2<B>() {

    protected lateinit var viewModel: VM
        private set


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (getRootView() != null) {
            return getRootView()
        }
        injectDataBinding(inflater, container)
        injectViewModel()
        initialize(savedInstanceState)
        initInternalObserver()
        return getRootView()

    }

    protected fun injectViewModel() {
        val vm = createViewModel()
        viewModel = ViewModelProvider(this, BaseViewModel.createViewModelFactory(vm))
                .get(vm::class.java)
        viewModel.application = requireActivity().application
        lifecycle.addObserver(viewModel)
    }

    protected fun initInternalObserver() {
        viewModel._loadingEvent.observeNonNull(this) {
            showLoadingView(it)
        }
        viewModel._toastEvent.observeNonNull(this) {
            showToast(it)
        }
        viewModel._pageNavigationEvent.observeNonNull(this) {
            navigate(it)
        }
        viewModel._backPressEvent.observeNonNull(this) {
            backPress(it)
        }
        viewModel._finishPageEvent.observeNonNull(this) {
            finishPage(it)
        }
    }

    protected abstract fun createViewModel(): VM;
}
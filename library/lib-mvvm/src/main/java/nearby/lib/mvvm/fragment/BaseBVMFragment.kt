package nearby.lib.mvvm.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import nearby.lib.base.exts.observeNonNull
import nearby.lib.mvvm.fragment.bind.BaseBindFragment
import nearby.lib.mvvm.vm.BaseViewModel

/**
 * @author: lr
 * @created on: 2022/7/10 3:45 下午
 * @description:基于MVVM模式的Fragment的基类
 */
abstract class BaseBVMFragment<B : ViewDataBinding, VM : BaseViewModel> : BaseBindFragment<B>() {
    protected lateinit var viewModel: VM
        private set

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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

    override fun onDestroy() {
        lifecycle.removeObserver(viewModel)
        super.onDestroy()

    }

    protected fun initInternalObserver() {
        viewModel._loadingEvent.observeNonNull(this) {
            showLoadingView(it)
        }
        viewModel._pageNavigationEvent.observeNonNull(this) {
            navigate(it)
        }
        viewModel._pageDataNavigationEvent.observeNonNull(this) {
            navigateData(it)
        }
        viewModel._backPressEvent.observeNonNull(this) {
            backPress(it)
        }
        viewModel._finishPageEvent.observeNonNull(this) {
            finishPage(it)
        }
    }

    protected abstract fun createViewModel(): VM
}
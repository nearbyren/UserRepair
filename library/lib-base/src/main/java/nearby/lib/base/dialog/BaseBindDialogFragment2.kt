package nearby.lib.base.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 * @author:
 * @contact: albertlii@163.com
 * @time: 2020/7/23 5:06 PM
 * @description: 带有DataBinding的DialogFragment基类
 * @since: 1.0.0
 */
abstract class BaseBindDialogFragment2<B : ViewDataBinding> : BaseDialogFragment2() {
    protected lateinit var binding: B

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (getRootView() != null) {
            return getRootView()
        }
        injectDataBinding(inflater, container)
        return getRootView()
//        return super.onCreateView(inflater, container, savedInstanceState)
    }

    protected open fun injectDataBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        binding.lifecycleOwner = this
        setRootView(binding.root)
    }

    override fun onDestroyView() {
        binding.unbind()
        super.onDestroyView()
    }

    override fun getDialogStyle(): Int? {
        return null
    }

    override fun getDialogTheme(): Int? {
        return null
    }
}
package nearby.lib.web.base

import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 * @author:
 * @created on: 2022/7/29 16:27
 * @description:
 */
abstract class BaseBindWebActivity<B : ViewDataBinding> : BaseWebActivity() {

    protected val simpleBindName: String get() = javaClass.simpleName

    protected lateinit var binding: B
        private set

    //将layout绑定
    override fun initContentView() {
        injectDataBinding()
    }

    private fun injectDataBinding() {
        binding = DataBindingUtil.setContentView(this, layoutRes())
        binding.lifecycleOwner = this
    }

    override fun onDestroy() {
        binding.unbind()//解绑
        super.onDestroy()
    }
}
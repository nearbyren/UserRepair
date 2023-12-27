package nearby.lib.base.dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 * @description: 带有DataBinding的Dialog基类
 * @since: 1.0.0
 */
abstract class BaseBindDialog<B : ViewDataBinding> : BaseDialog {
    protected lateinit var binding: B

    constructor(context: Context) : super(context)

    constructor(context: Context, themeResId: Int) : super(context, themeResId)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialize(savedInstanceState)
        refreshAttributes()
    }

    override fun initContentView() {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(getContext()),
            getLayoutId(), null, false
        )
        setContentView(binding.root)
    }
}
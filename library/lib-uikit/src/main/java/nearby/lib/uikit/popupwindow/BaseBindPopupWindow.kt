package nearby.lib.uikit.popupwindow

import android.content.Context
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 * @description: 带有DataBinding的PopupWindow基类
 * @since: 1.0.0
 */
abstract class BaseBindPopupWindow<B : ViewDataBinding>(context: Context) :
    BasePopupWindow(context) {

    protected lateinit var binding: B

    override fun initContentView() {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            getLayoutId(), null, false
        )
        contentView = binding.root
    }
}
package apps.user.repair.dialog

import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import apps.user.repair.R
import apps.user.repair.databinding.FragmentSignOutBinding
import apps.user.repair.http.IndexViewModel
import com.app.toast.ToastX
import com.app.toast.expand.dp
import nearby.lib.base.dialog.BaseBindDialogFragment
import nearby.lib.base.exts.observeNonNull
import nearby.lib.base.uitl.AppManager
import nearby.lib.base.uitl.ToastEvent
import nearby.lib.mvvm.fragment.BaseAppBVMDialogFragment

class SignOutDialogFragment : BaseAppBVMDialogFragment<FragmentSignOutBinding, IndexViewModel>() {
    override fun createViewModel(): IndexViewModel {
        return IndexViewModel()
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_sign_out
    }

    override fun initialize(savedInstanceState: Bundle?) {
        binding.cancel.setOnClickListener { dismiss() }
        binding.confirm.setOnClickListener {
            viewModel.logout()
        }
        viewModel.logoutDto.observeNonNull(this) {
            if (!TextUtils.isEmpty(it.msg)) {
                toast(it.msg)
                return@observeNonNull
            }
            AppManager.getInstance().finishAllActivity()
            dismiss()
        }
    }

    private fun toast(text: String) {
        ToastX.with(requireActivity()).text(text) //文字
            .backgroundColor(nearby.lib.base.R.color.blue) //背景
            .animationMode(ToastX.ANIM_MODEL_SLIDE) //动画模式 弹出或者渐变
            .textColor(nearby.lib.uikit.R.color.white) //文字颜色
            .position(ToastX.POSITION_TOP) //显示的位置 顶部或者底部
            .textGravity(Gravity.CENTER) //文字的位置
            .duration(ToastX.DURATION_LONG) //显示的时间 单位ms
            .textSize(14f) //文字大小 单位sp
            .padding(10.dp, 10.dp) //左右内边距
            .margin(15.dp, 15.dp) //左右外边距
            .radius(10f.dp) //圆角半径
            .offset(44.dp) //距离顶部或者底部的偏移量
            .duration(2000).show() //显示
    }

}
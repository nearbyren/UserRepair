package apps.user.repair.ui.info

import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import apps.user.repair.R
import apps.user.repair.databinding.ActivityEditPasswordBinding
import apps.user.repair.http.IndexViewModel
import apps.user.repair.uitl.SPreUtil
import com.app.toast.ToastX
import com.app.toast.expand.dp
import nearby.lib.base.bar.BarHelperConfig
import nearby.lib.base.exts.observeNonNull
import nearby.lib.base.uitl.AppManager
import nearby.lib.base.uitl.ToastUtils
import nearby.lib.mvvm.activity.BaseAppBVMActivity
import nearby.lib.mvvm.activity.BaseAppBindActivity


class EditPasswordActivity : BaseAppBVMActivity<ActivityEditPasswordBinding, IndexViewModel>() {

    override fun layoutRes(): Int {
        return R.layout.activity_edit_password
    }


    override fun initialize(savedInstanceState: Bundle?) {
        binding.button.setOnClickListener {
            //密碼1
            val password = binding.passwordEt.text.toString()
            val password1 = binding.passwordEt1.text.toString()
            val password2 = binding.passwordEt2.text.toString()
            if (TextUtils.isEmpty(password)) {
                toast("請輸入舊密碼")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(password1)) {
                toast("請輸入新密碼")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(password2)) {
                toast("請輸入再次確認新密碼")
                return@setOnClickListener
            }
            if (password1 != password2) {
                ToastUtils.showToast("兩次新密碼不一致")
                return@setOnClickListener
            }
            viewModel.revisePasswordId(password2, 1)
        }
        viewModel.editPasswordDto.observeNonNull(this) {
            if (!TextUtils.isEmpty(it.msg)) {
                toast(it.msg)
                return@observeNonNull
            }
            AppManager.getInstance().finishAllActivity()
        }
    }

    private fun toast(text: String) {
        ToastX.with(this)
            .text(text) //文字
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
            .duration(2000)
            .show() //显示
    }

    override fun createViewModel(): IndexViewModel {
        return IndexViewModel()
    }

    override fun initBarHelperConfig(): BarHelperConfig {
        return BarHelperConfig.builder().setBack(true).setIconLeft(R.drawable.icon_white_left)
            .setBgColor(R.color.toolbar_bg_color)
            .setTitle(
                title = getString(R.string.info_03),
                titleColor = nearby.lib.base.R.color.white
            ).build()
    }

}
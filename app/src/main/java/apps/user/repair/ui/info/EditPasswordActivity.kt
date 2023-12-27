package apps.user.repair.ui.info

import android.os.Bundle
import android.text.TextUtils
import apps.user.repair.R
import apps.user.repair.databinding.ActivityEditPasswordBinding
import apps.user.repair.uitl.SPreUtil
import nearby.lib.base.bar.BarHelperConfig
import nearby.lib.base.uitl.ToastUtils
import nearby.lib.mvvm.activity.BaseAppBindActivity


class EditPasswordActivity : BaseAppBindActivity<ActivityEditPasswordBinding>() {

    override fun layoutRes(): Int {
        return R.layout.activity_edit_password
    }


    override fun initialize(savedInstanceState: Bundle?) {
        binding.button.setOnClickListener {
            //密碼1
            val password = binding.passwordEt.text.toString()
            val password1 = binding.passwordEt1.text.toString()
            val password2 = binding.passwordEt2.text.toString()
            val oldPassowrd = SPreUtil[this@EditPasswordActivity, "password", ""]
            if (TextUtils.isEmpty(password)) {
                ToastUtils.showToast("請輸入舊密碼")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(password1)) {
                ToastUtils.showToast("請輸入新密碼")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(password2)) {
                ToastUtils.showToast("請輸入再次確認新密碼")
                return@setOnClickListener
            }
            if (password != oldPassowrd) {
                ToastUtils.showToast("請輸入正確的舊密碼")
                return@setOnClickListener
            }
            if (password1 != password2) {
                ToastUtils.showToast("兩次密碼不一致")
                return@setOnClickListener
            }
            SPreUtil.put(this@EditPasswordActivity, "password", password2.toString())
            finishPage(EditPasswordActivity@ this)
        }
    }

    override fun initBarHelperConfig(): BarHelperConfig {
        return BarHelperConfig.builder().setBack(true).setIconLeft(R.drawable.icon_white_left)
            .setBgColor(R.color.toolbar_bg_color)
            .setTitle(title = getString(R.string.info_03), titleColor = nearby.lib.base.R.color.white).build()
    }

}
package apps.user.repair.ui.info

import android.os.Bundle
import android.text.TextUtils
import androidx.core.view.isVisible
import apps.user.repair.R
import apps.user.repair.databinding.ActivityEditEmailBinding
import apps.user.repair.http.IndexViewModel
import apps.user.repair.uitl.SPreUtil
import nearby.lib.base.bar.BarHelperConfig
import nearby.lib.base.uitl.ToastUtils
import nearby.lib.mvvm.activity.BaseAppBVMActivity
import nearby.lib.mvvm.activity.BaseAppBindActivity


class EditEmailActivity : BaseAppBVMActivity<ActivityEditEmailBinding,IndexViewModel>() {

    override fun layoutRes(): Int {
        return R.layout.activity_edit_email
    }


    override fun initialize(savedInstanceState: Bundle?) {
        binding.getCode.setOnClickListener {
            val email = binding.emailEt.text
            if (TextUtils.isEmpty(email)) {
                return@setOnClickListener
            }
            binding.hasSent.isVisible = true
        }
        binding.button.setOnClickListener {
            val email = binding.emailEt.text
            val password = binding.passwordEt.text
            if (TextUtils.isEmpty(email)) {
                ToastUtils.showToast("請輸入郵件賬號")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(password)) {
                ToastUtils.showToast("請輸入密碼")
                return@setOnClickListener
            }
            SPreUtil.put(this, "email", email.toString())
            SPreUtil.put(this, "email_has", true)
            finishPage(EditEmailActivity::class.java)
            viewModel.updateEmail()
        }
    }

    override fun createViewModel(): IndexViewModel {
       return IndexViewModel()
    }

    override fun initBarHelperConfig(): BarHelperConfig {
        return BarHelperConfig.builder().setBack(true).setIconLeft(R.drawable.icon_white_left)
            .setBgColor(R.color.toolbar_bg_color)
            .setTitle(title = getString(R.string.info_17), titleColor = nearby.lib.base.R.color.white).build()
    }

}
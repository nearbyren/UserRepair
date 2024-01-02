package apps.user.repair.ui

import android.content.Intent
import android.os.Bundle
import apps.user.repair.R
import apps.user.repair.databinding.ActivityActivateBinding
import nearby.lib.mvvm.activity.BaseAppBindActivity


class ActivateActivity : BaseAppBindActivity<ActivityActivateBinding>() {


    companion object {
        const val type: String = "type"
    }

    override fun layoutRes(): Int {
        return R.layout.activity_activate
    }


    override fun initialize(savedInstanceState: Bundle?) {
        binding.login.setOnClickListener {
            val intent = Intent(this, SignInLoginActivity::class.java)
            intent.putExtra(type, "1")
            navigateData(intent)
        }
        binding.sign.setOnClickListener {
            val intent = Intent(this, SignInLoginActivity::class.java)
            intent.putExtra(type, "0")
            navigateData(intent)
        }
    }
}
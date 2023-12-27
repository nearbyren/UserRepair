package apps.user.repair.ui.info

import android.os.Bundle
import apps.user.repair.R
import apps.user.repair.databinding.ActivityRermsDetailsBinding
import nearby.lib.base.bar.BarHelperConfig
import nearby.lib.mvvm.activity.BaseAppBindActivity


class RermsDetailsActivity : BaseAppBindActivity<ActivityRermsDetailsBinding>() {

    override fun layoutRes(): Int {
        return R.layout.activity_rerms_details
    }


    override fun initialize(savedInstanceState: Bundle?) {
    }

    override fun initBarHelperConfig(): BarHelperConfig {
        return BarHelperConfig.builder().setBack(true).setIconLeft(R.drawable.icon_white_left)
            .setBgColor(R.color.toolbar_bg_color).setTitle(title = getString(R.string.info_14), titleColor = nearby.lib.base.R.color.white).build()
    }

}
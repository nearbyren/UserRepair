package apps.user.repair.ui.info

import android.os.Bundle
import apps.user.repair.R
import apps.user.repair.databinding.ActivityAboutUsBinding
import nearby.lib.base.bar.BarHelperConfig
import nearby.lib.mvvm.activity.BaseAppBindActivity


class AboutUsActivity : BaseAppBindActivity<ActivityAboutUsBinding>() {

    override fun layoutRes(): Int {
        return R.layout.activity_about_us
    }


    override fun initialize(savedInstanceState: Bundle?) {

    }

    override fun initBarHelperConfig(): BarHelperConfig {
        return BarHelperConfig.builder().setBack(true).setIconLeft(R.drawable.icon_white_left)
            .setBgColor(R.color.toolbar_bg_color).setTitle(
                title = getString(R.string.info_13),
                titleColor = nearby.lib.base.R.color.white
            ).build()
    }

}
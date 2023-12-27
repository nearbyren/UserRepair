package apps.user.repair.ui

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import apps.user.repair.R
import apps.user.repair.databinding.ActivitySubmitCardBinding
import apps.user.repair.uitl.ConstantUtil
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine
import com.zhihu.matisse.internal.entity.CaptureStrategy
import nearby.lib.base.bar.BarHelperConfig
import nearby.lib.mvvm.activity.BaseAppBindActivity
import java.io.File


class SubmitCardActivity : BaseAppBindActivity<ActivitySubmitCardBinding>() {

    override fun layoutRes(): Int {
        return R.layout.activity_submit_card
    }


    override fun initialize(savedInstanceState: Bundle?) {
        binding.clUpload.setOnClickListener {
            Matisse.from(this)
                .choose(MimeType.ofImage())
                .countable(true)
                .maxSelectable(1)
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(GlideEngine())
                .showPreview(false) // Default is `true`
                .capture(true)
                .captureStrategy(CaptureStrategy(true, "PhotoPicker"))
                .forResult(ConstantUtil.ALBUM_REQUEST_CODE)
        }
        binding.upload.setOnClickListener {
            navigate(MainActivity::class.java)
            finishPage(SubmitCardActivity@this)
        }
    }

    override fun initBarHelperConfig(): BarHelperConfig {
        return BarHelperConfig.builder().setBack(true).setIconLeft(R.drawable.icon_back_left)
            .setBgColor(nearby.lib.base.R.color.white).setTitle(title = "").build()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (ConstantUtil.ALBUM_REQUEST_CODE == requestCode && resultCode == RESULT_OK) {
            data?.let {
                val photoPath = Matisse.obtainPathResult(it)[0]
                photoPath?.let {
                    val f = File(it)
                    Glide.with(this).load(f).into(binding.cardImg)
                    binding.cardBg.isVisible = false
                }
            }
        }
    }

}
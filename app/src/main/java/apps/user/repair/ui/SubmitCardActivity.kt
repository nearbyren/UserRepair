package apps.user.repair.ui

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import com.bumptech.glide.Glide
import apps.user.repair.R
import apps.user.repair.databinding.ActivitySubmitCardBinding
import apps.user.repair.http.IndexViewModel
import apps.user.repair.uitl.ConstantUtil
import com.app.toast.ToastX
import com.app.toast.expand.dp
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine
import com.zhihu.matisse.internal.entity.CaptureStrategy
import nearby.lib.base.bar.BarHelperConfig
import nearby.lib.base.exts.observeNonNull
import nearby.lib.mvvm.activity.BaseAppBVMActivity
import java.io.File


class SubmitCardActivity : BaseAppBVMActivity<ActivitySubmitCardBinding, IndexViewModel>() {
    lateinit var email: String
    lateinit var password: String
    var schoolImage: String? = ""
    override fun layoutRes(): Int {
        return R.layout.activity_submit_card
    }


    override fun initialize(savedInstanceState: Bundle?) {
        intent.extras?.let {
            email = it.getString("email").toString()
            password = it.getString("password").toString()
            println("email $email - password = $password")
        }
        binding.clUpload.setOnClickListener {
            goXXPermissions()
        }
        binding.upload.setOnClickListener {
            val schoolName = binding.schoolName.text.toString()
            if (TextUtils.isEmpty(schoolName)) {
                toast("請輸入學校名稱")
                return@setOnClickListener
            }
            val name = binding.contactPersonNameEt.text.toString()
            if (TextUtils.isEmpty(name)) {
                toast("請輸入姓名")
                return@setOnClickListener
            }
            val telephone = binding.telephoneEt.text.toString()
            if (TextUtils.isEmpty(telephone)) {
                toast("請輸入電話")
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(schoolImage)) {
                toast("請上傳學校卡片")
                return@setOnClickListener
            }
            viewModel.enroll(email, password, schoolName, name, telephone, schoolImage!!)
        }
        viewModel.enrollDto.observeNonNull(this) {
            if (!TextUtils.isEmpty(it.msg)) {
                toast(it.msg!!)
                return@observeNonNull
            }
            toast("註冊成功")
            navigate(MainActivity::class.java)
            finishPage(SubmitCardActivity@ this)
        }
        viewModel.fileSuccess.observeNonNull(this) {
            if (!TextUtils.isEmpty(it.msg)) {
                toast(it.msg!!)
                return@observeNonNull
            }
            toast("图片上传成功")
            schoolImage = it.path
            Glide.with(this).load(schoolImage).into(binding.cardImg)
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
        return BarHelperConfig.builder().setBack(true).setIconLeft(R.drawable.icon_back_left)
            .setBgColor(nearby.lib.base.R.color.white).setTitle(title = "").build()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (ConstantUtil.ALBUM_REQUEST_CODE == requestCode && resultCode == RESULT_OK) {
            data?.let {
                val photoPath = Matisse.obtainPathResult(it)[0]
                photoPath?.let { photoPath ->
                    upload(photoPath)
                }
            }
        }
    }

    private fun upload(path: String) {
        viewModel.upload(File(path))
    }

    private fun goXXPermissions() {
        val p = mutableListOf(
            Permission.READ_EXTERNAL_STORAGE,
            Permission.CAMERA
        )
        XXPermissions.with(this)
            // 申请多个权限
            .permission(p)
            // 设置权限请求拦截器（局部设置）
            //.interceptor(new PermissionInterceptor())
            // 设置不触发错误检测机制（局部设置）
            //.unchecked()
            .request(object : OnPermissionCallback {

                override fun onGranted(permissions: MutableList<String>, allGranted: Boolean) {
                    if (!allGranted) {
                        println("获取部分权限成功，但部分权限未正常授予")
                        return
                    }
                    println("获取相機权限成功")
                    goAlbum(1)

                }

                override fun onDenied(permissions: MutableList<String>, doNotAskAgain: Boolean) {
                    if (doNotAskAgain) {
                        println("被永久拒绝授权，请手动授予录音和日历权限")
                        // 如果是被永久拒绝就跳转到应用权限系统设置页面
                        XXPermissions.startPermissionActivity(this@SubmitCardActivity, permissions)
                    } else {
                        println("获取相機权限失败")
                    }
                }
            })
    }

    private fun goAlbum(selectable: Int) {
        Matisse.from(this)
            .choose(MimeType.ofImage())
            .countable(true)
            .maxSelectable(selectable)
            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
            .thumbnailScale(0.85f)
            .imageEngine(GlideEngine())
            .showPreview(false) // Default is `true`
            .capture(true)
            .captureStrategy(CaptureStrategy(true, "PhotoPicker"))
            .forResult(ConstantUtil.ALBUM_REQUEST_CODE)
    }


}
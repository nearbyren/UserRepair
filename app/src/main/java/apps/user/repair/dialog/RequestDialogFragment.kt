package apps.user.repair.dialog

import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import apps.user.repair.R
import apps.user.repair.databinding.FragmentRequestBinding
import apps.user.repair.http.IndexViewModel
import apps.user.repair.model.FileDto
import apps.user.repair.uitl.ConstantUtil
import apps.user.repair.uitl.ConstantUtil.ALBUM_REQUEST_CODE
import apps.user.repair.uitl.FileService
import apps.user.repair.uitl.RxRetrofitFactory
import apps.user.repair.uitl.SPreUtil
import com.app.toast.ToastX
import com.app.toast.expand.dp
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine
import com.zhihu.matisse.internal.entity.CaptureStrategy
import nearby.lib.base.exts.observeNonNull
import nearby.lib.base.uitl.ToastEvent
import nearby.lib.base.uitl.ToastUtils
import nearby.lib.mvvm.fragment.BaseAppBVMDialogFragment
import nearby.lib.uikit.recyclerview.SpaceItemDecoration
import nearby.lib.uikit.widgets.dpToPx
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.Calendar


class RequestDialogFragment : BaseAppBVMDialogFragment<FragmentRequestBinding, IndexViewModel>() {
    override fun createViewModel(): IndexViewModel {
        return IndexViewModel()
    }


    override fun getLayoutId(): Int {
        return R.layout.fragment_request
    }

    override fun initialize(savedInstanceState: Bundle?) {
        arguments?.let {
            type = it.getInt("type")
            println("維修類型 type = $type")
        }
        binding.iconBack.setOnClickListener { dismiss() }
        binding.rgUrgent.setOnCheckedChangeListener { _, p1 ->
            when (p1) {
                R.id.radio1 -> {
                    println("radio1")
                    binding.clUrgent.isVisible = true
                }

                R.id.radio2 -> {
                    println("radio2")
                    binding.clUrgent.isVisible = false
                }
            }

        }


        //維修地點照片
        binding.addressBtn.setOnClickListener {
            repType = ConstantUtil.ALBUM_LEAFLET
            goXXPermissions()
        }

        //維修位置照片
        binding.recycle.adapter = itemAlbumAdapter
        binding.recycle.layoutManager = GridLayoutManager(context, 3)
        binding.recycle.addItemDecoration(SpaceItemDecoration(0, 10.dpToPx, 10.dpToPx))
        binding.recycle.setHasFixedSize(true)
        binding.recycle.itemAnimator = null
        itemAlbumAdapter.setOnItemClickListener(object :
            apps.user.repair.adapter.ItemClickListener {
            override fun onItemClick(obj: Any, position: Int) {
                repType = ConstantUtil.ALBUM_MULTIPLE
                Log.e("position", "position---->$position")
                if (list.size === position && mMaxNumber - list.size > 0) {
                    goXXPermissions()
                } else {
                    //跳转至删除或者预览页面
                }
            }
        })
        itemAlbumAdapter.setOnDeleteClickListener(object : apps.user.repair.adapter.DeleteListener {
            override fun onDeleteClick(obj: Any, position: Int) {
                removeList(position)
            }
        })

        //緊急選擇
        binding.clUrgent.setOnClickListener {
            showTimePickerDialog()
        }
        //提交上傳
        binding.confirm.setOnClickListener {
            //維修地點
            val address = binding.addressEt.text.toString()
            //維修詳情
            val detail = binding.detailEt.text.toString()
            //維修地址圖
            if (TextUtils.isEmpty(addressImage)) {
                ToastUtils.showToast("請上傳維修地址圖")
                return@setOnClickListener
            }
            //維修位置圖
            if (list.size == 0) {
                ToastUtils.showToast("請上傳維修位置圖")
                return@setOnClickListener
            }
            var locationImage = StringBuilder()
            val size = list.size
            for (i in 0 until size) {
                val t = size - 1
                println("我来了 $t ")
                if (list.size > 1 && i < t) {
                    locationImage.append("${list[i]},")
                } else {
                    locationImage.append("${list[i]}")
                }
            }
            //是否緊急
            val urgent = if (binding.clUrgent.isVisible) 1 else 0
            if (urgent == 1 && TextUtils.isEmpty(urgentTime)) {
                ToastUtils.showToast("请选择紧急时间")
                return@setOnClickListener
            }
            //緊急事件
            urgentTime?.let { time ->
                urgentTime = time
            }
            val id = SPreUtil[requireActivity(), "id", "1"]
            viewModel.submit(
                address,
                detail,
                addressImage!!,
                locationImage.toString(),
                urgent,
                urgentTime,
                type,
                id.toString()
            )
        }
        viewModel.submit.observeNonNull(this) {
            if (!TextUtils.isEmpty(it.msg)) {
                toast(it.msg!!)
                return@observeNonNull
            }
            toast("维修请求成")
            dismiss()
        }

        viewModel.fileSuccess.observeNonNull(this) {
            if (!TextUtils.isEmpty(it.msg)) {
                toast(it.msg!!)
                return@observeNonNull
            }
            toast("上传成功")
            if (repType == ConstantUtil.ALBUM_LEAFLET) {
                addressImage = it.path
                Glide.with(requireActivity()).load(addressImage).into(binding.ivRepLoc1)
            } else {
                list.add(it.path!!)
                itemAlbumAdapter.nodfiyData(list)
            }
        }
    }

    private fun toast(text: String) {
        ToastX.with(requireActivity())
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
                    //动态申请权限
                    if (repType == ConstantUtil.ALBUM_LEAFLET) {
                        goAlbum(1)
                    } else {
                        val size = mMaxNumber - list.size
                        goAlbum(size)
                    }
                }

                override fun onDenied(permissions: MutableList<String>, doNotAskAgain: Boolean) {
                    if (doNotAskAgain) {
                        println("被永久拒绝授权，请手动授予录音和日历权限")
                        // 如果是被永久拒绝就跳转到应用权限系统设置页面
                        XXPermissions.startPermissionActivity(context, permissions)
                    } else {
                        println("获取相機权限失败")
                    }
                }
            })
    }

    private val itemAlbumAdapter by lazy {
        apps.user.repair.adapter.ItemAddAlbumAdapter(
            requireActivity(),
            list,
            mMaxNumber
        )
    }
    private var list = mutableListOf<String>()
    private var mMaxNumber: Int = 6
    private var addressImage: String? = null
    private var urgentTime: String? = null
    private var type: Int = 8

    //1.維修地點照片 2.維修位置照片
    var repType: Int = ConstantUtil.ALBUM_LEAFLET


    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val timePickerDialog =
            DatePickerDialog(requireActivity(), { p0, p1, p2, p3 ->
                println("p0 = $p0 - 年 = $p1 月 = $p2 日 = $p3")
                var pp2: String? = null
                var pp3: String? = null
                if (p2 == 9) {
                    pp2 = "10"
                } else {
                    pp2 = "0${p2 + 1}"
                }
                if (p3 < 9) {
                    pp3 = "0${p3}"
                } else {
                    pp3 = p3.toString()
                }
                urgentTime = "$p1-$pp2-$pp3"
                binding.urgentText.text = urgentTime

            }, hour, month, day)
        timePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        timePickerDialog.show();
    }

    /**
     * 删除图片
     *
     * @param position
     */
    private fun removeList(position: Int) {
        if (list.size == 0) return
        list.removeAt(position)
        itemAlbumAdapter.nodfiyData(list)
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
            .forResult(ALBUM_REQUEST_CODE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            ALBUM_REQUEST_CODE -> {
                data?.let {
                    when (repType) {
                        ConstantUtil.ALBUM_LEAFLET -> {
                            val photoPath = Matisse.obtainPathResult(it)[0]
                            photoPath?.let { path ->
                                upload(path)
                            }
                        }

                        ConstantUtil.ALBUM_MULTIPLE -> {
                            val pathList = Matisse.obtainPathResult(it)
                            for (i in 0 until pathList.size) {
                                val path = pathList[i]
                                println("图片${(i + 1)} 地址 $path")
                                upload(path)
                            }
                        }

                        else -> {}
                    }
                }

            }
        }
    }

    private fun upload(path: String) {
        val f = File(path)
        val requestFile =
            f.asRequestBody("application/otcet-stream".toMediaTypeOrNull())
        val part = MultipartBody.Part.createFormData(
            "file",
            "avatar_${System.currentTimeMillis()}",
            requestFile
        )
        viewModel.upload(File(path))
    }

}
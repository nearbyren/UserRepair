package apps.user.repair.dialog

import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
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
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine
import com.zhihu.matisse.internal.entity.CaptureStrategy
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
            //是否緊急
            val urgent = if (binding.clUrgent.isVisible) 1 else 0
            //緊急事件
            urgentTime?.let { time ->
                urgentTime = time
            }
            viewModel.submit(address, detail, addressImage!!, "", urgent, urgentTime, type, "")
        }

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
                urgentTime = "$p1/$p2/$p3"

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
                                upload(repType, path)
                            }
                        }

                        ConstantUtil.ALBUM_MULTIPLE -> {
                            val pathList = Matisse.obtainPathResult(it)
                            for (i in 0 until pathList.size) {
                                val path = pathList[i]
                                println("图片${(i + 1)} 地址 $path")
                                upload(repType, path)
                            }
                            itemAlbumAdapter.nodfiyData(list)
                        }

                        else -> {}
                    }
                }

            }
        }
    }

    private fun upload(repType: Int, path: String) {
        val f = File(path)
        val requestFile =
            f.asRequestBody("application/otcet-stream".toMediaTypeOrNull())
        val part = MultipartBody.Part.createFormData(
            "file",
            "avatar_${System.currentTimeMillis()}",
            requestFile
        )
        RxRetrofitFactory.createGson(FileService::class.java).upload(part)
            .enqueue(object : Callback<FileDto?> {
                override fun onResponse(
                    call: Call<FileDto?>,
                    response: Response<FileDto?>
                ) {
                    response.body()?.let {
                        if (repType == ConstantUtil.ALBUM_LEAFLET) {
                            addressImage = it.data
                            Glide.with(requireActivity()).load(f)
                                .into(binding.ivRepLoc1)
                        } else {
                            list.add(path)
                        }
                    }
                }

                override fun onFailure(call: Call<FileDto?>, t: Throwable) {
                    ToastUtils.showToast("上传头像失败")
                }

            })
    }

}
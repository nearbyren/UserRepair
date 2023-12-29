package apps.user.repair.dialog

import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import apps.user.repair.R
import apps.user.repair.databinding.FragmentRequestBinding
import apps.user.repair.uitl.ConstantUtil
import apps.user.repair.uitl.ConstantUtil.ALBUM_REQUEST_CODE
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine
import com.zhihu.matisse.internal.entity.CaptureStrategy
import nearby.lib.base.dialog.BaseBindDialogFragment
import nearby.lib.base.uitl.ToastUtils
import nearby.lib.uikit.recyclerview.SpaceItemDecoration
import nearby.lib.uikit.widgets.dpToPx
import java.io.File
import java.util.Calendar


class RequestDialogFragment : BaseBindDialogFragment<FragmentRequestBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_request
    }

    private val itemAlbumAdapter by lazy {
        apps.user.repair.adapter.ItemAddAlbumAdapter(
            requireActivity(),
            list,
            mMaxNumber
        )
    }
    private var list = mutableListOf<String>()
    private var mMaxNumber: Int = 5

    //1.維修地點照片 2.維修位置照片
    var repType: Int = ConstantUtil.ALBUM_LEAFLET
    override fun initialize(view: View, savedInstanceState: Bundle?) {
        binding.iconBack.setOnClickListener { dismiss() }
        binding.rgUrgent.setOnCheckedChangeListener { rg, p1 ->
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

        //維修地點
//        binding.addressEt
        //維修詳情
//        binding.detailEt

        //維修地點照片
//        binding.ivRepLoc1
        binding.addressBtn.setOnClickListener {
            repType = ConstantUtil.ALBUM_LEAFLET
            goAlbum(1)
        }
//        binding.addressText
        //維修位置照片


        binding.recycle.adapter = itemAlbumAdapter
        binding.recycle.layoutManager = GridLayoutManager(context, 3)
        binding.recycle.addItemDecoration(SpaceItemDecoration(0, 10.dpToPx, 10.dpToPx))
        binding.recycle.setHasFixedSize(true)
        binding.recycle.itemAnimator = null

        itemAlbumAdapter.setOnItemClickListener(object : apps.user.repair.adapter.ItemClickListener {
            override fun onItemClick(obj: Any, position: Int) {
                repType = ConstantUtil.ALBUM_MULTIPLE
                Log.e("position", "position---->$position")
                if (list.size === position && mMaxNumber - list.size > 0) {
                    //动态申请权限
                    val size = mMaxNumber - list.size
                    goAlbum(size)
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
            ToastUtils.showToast("提交")
        }

    }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val timePickerDialog =
            DatePickerDialog(requireActivity(),{ p0, p1, p2, p3 ->
                    println("p0 = $p0 - 年 = $p1 月 = $p2 日 = $p3")

                }, hour, month, day)
        timePickerDialog.datePicker.minDate= System.currentTimeMillis()-1000
        timePickerDialog.show();
    }

    /**
     * 删除图片
     *
     * @param position
     */
    private fun removeList(position: Int) {
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
                            photoPath?.let {
                                val f = File(it)
                                Glide.with(this).load(f).into(binding.ivRepLoc1)
                            }
                        }

                        ConstantUtil.ALBUM_MULTIPLE -> {
                            val pathList = Matisse.obtainPathResult(it)
                            for (i in 0 until pathList.size) {
                                val path = pathList[i]
                                println("图片${(i + 1)} 地址 $path")
                                list.add(path)
                            }
                            itemAlbumAdapter.nodfiyData(list)
                        }

                        else -> {}
                    }
                }

            }
        }
    }
}
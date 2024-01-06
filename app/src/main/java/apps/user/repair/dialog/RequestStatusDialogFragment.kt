package apps.user.repair.dialog

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import androidx.annotation.Nullable
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import apps.user.repair.R
import apps.user.repair.databinding.FragmentRequestStatusBinding
import apps.user.repair.http.IndexViewModel
import apps.user.repair.model.AlbumDto
import apps.user.repair.model.InventoryDto
import apps.user.repair.uitl.ConstantUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import nearby.lib.base.dialog.BaseBindDialogFragment
import nearby.lib.base.exts.observeNonNull
import nearby.lib.base.exts.observeNullable
import nearby.lib.base.uitl.ToastEvent
import nearby.lib.mvvm.fragment.BaseAppBVMDialogFragment
import nearby.lib.uikit.recyclerview.BaseRecyclerAdapter
import nearby.lib.uikit.recyclerview.SpaceItemDecoration
import nearby.lib.uikit.widgets.dpToPx
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


class RequestStatusDialogFragment :
    BaseAppBVMDialogFragment<FragmentRequestStatusBinding, IndexViewModel>() {

    private var albumDtos = mutableListOf<AlbumDto>()
    private val itemAlbumAdapter by lazy { apps.user.repair.adapter.ItemShowAlbumAdapter() }
    override fun createViewModel(): IndexViewModel {
        return IndexViewModel()
    }


    override fun getLayoutId(): Int {
        return R.layout.fragment_request_status
    }

    override fun initialize(savedInstanceState: Bundle?) {
        binding.iconBack.setOnClickListener { dismiss() }

        viewModel.inventoryDto.observeNonNull(this) {

        }
        arguments?.let { bundle ->
            val numbers = bundle.getString("numbers")
            println("订单状态 $numbers")
            numbers?.let {
                viewModel.numbers(it)
            }
        }
        viewModel.inventoryDto.observeNonNull(this) {
            it?.let {
                toUI(it)
            }
        }
    }

    private fun toUI(it: InventoryDto) {
        binding.top.address.text = it.repairAddress
        binding.top.schoolName.text = it.repairAddress
        binding.top.schoolAddress.text = "描述詳情:  ${it.describes}"
        binding.top.urgent.text = "${if (it.urgent == 1) "是否緊急: 是" else "是否緊急: 否"}"
        Glide.with(requireActivity()).load(it.addressImage).into(binding.top.image)
        println("目前狀態 ${it.state}")
        when (it.state) {
            ConstantUtil.SERVICE_STATUS_QUOTE -> {
                binding.top.addressStatus.text = "準備報價中"
                binding.top.addressStatus.setCompoundDrawables(
                    ContextCompat.getDrawable(
                        requireActivity(), R.drawable.index_status_shape_1
                    ), null, null, null
                )
                binding.top.addressStatus.setTextColor(
                    ContextCompat.getColor(
                        requireActivity(), R.color.item_status_1
                    )
                )


                //隱藏已報價按鈕
                binding.status23.isVisible = false
                binding.dowQuote.isVisible = false
                binding.confirm.isVisible = false

                //隱藏已完成
                binding.filled.isVisible = false
            }

            ConstantUtil.SERVICE_STATUS_QUOTED -> {
                binding.top.addressStatus.text = "已报价，等待確認"
                binding.top.addressStatus.setCompoundDrawables(
                    ContextCompat.getDrawable(
                        requireActivity(), R.drawable.index_status_shape_2
                    ), null, null, null
                )
                binding.top.addressStatus.setTextColor(
                    ContextCompat.getColor(
                        requireActivity(), R.color.item_status_2
                    )
                )

                //隱藏已報價按鈕
                binding.status23.isVisible = true
                binding.dowQuote.isVisible = true
                binding.dowQuote.setOnClickListener {


//                        Glide.with(requireActivity()).asBitmap().load("https://profile-avatar.csdnimg.cn/e8d347414ee14d94bebc9d07578665ef_m0_37792384.jpg!1")
//                            .into(object : CustomTarget<Bitmap?>() {
//
//                                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap?>?) {
//                                    saveImageToGallery(requireActivity(),resource)
//                                }
//
//                                override fun onLoadCleared(@Nullable placeholder: Drawable?) {}
//                            })

                    saveImageToExternalStorage(
                        requireActivity(),
                        "https://profile-avatar.csdnimg.cn/e8d347414ee14d94bebc9d07578665ef_m0_37792384.jpg!1",
                        "image_" + System.currentTimeMillis() + ".jpg"
                    )

                }

                binding.confirm.isVisible = true
                Glide.with(this).load(R.drawable.baojia).into(binding.baojiaimg)

            }

            ConstantUtil.SERVICE_STATUS_CONFIRM -> {
                binding.top.addressStatus.text = "已确认，正在安排师傅"
                binding.top.addressStatus.setCompoundDrawables(
                    ContextCompat.getDrawable(
                        requireActivity(), R.drawable.index_status_shape_3
                    ), null, null, null
                )
                binding.top.addressStatus.setTextColor(
                    ContextCompat.getColor(
                        requireActivity(), R.color.item_status_3
                    )
                )
                Glide.with(this).load(R.drawable.baojia).into(binding.baojiaimg)
                binding.status23.isVisible = true
                //隱藏已報價按鈕
                binding.dowQuote.isVisible = false
                binding.confirm.isVisible = false
                //隱藏已完成
                binding.filled.isVisible = false
            }

            ConstantUtil.SERVICE_STATUS_FINISH -> {
                binding.top.addressStatus.text = "已完成"
                binding.top.addressStatus.setCompoundDrawables(
                    ContextCompat.getDrawable(
                        requireActivity(), R.drawable.index_status_shape_4
                    ), null, null, null
                )
                binding.top.addressStatus.setTextColor(
                    ContextCompat.getColor(
                        requireActivity(), R.color.item_status_4
                    )
                )

                //隱藏維修信息
                binding.top.detail.isVisible = false
                binding.top.infoDetail.isVisible = false
                //隱藏已報價按鈕
                binding.status23.isVisible = false
                binding.dowQuote.isVisible = false
                binding.confirm.isVisible = false


                binding.filled.isVisible = true


                Glide.with(this).load(R.drawable.fapiao).into(binding.fapiao)

                albumDtos.add(AlbumDto("水喉渠務", R.drawable.item1))
                albumDtos.add(AlbumDto("防漏防水", R.drawable.item2))
                albumDtos.add(AlbumDto("門窗", R.drawable.item3))
                albumDtos.add(AlbumDto("木工", R.drawable.item4))
                albumDtos.add(AlbumDto("廢物處理", R.drawable.item1))
                albumDtos.add(AlbumDto("冷氣", R.drawable.item2))
                itemAlbumAdapter.setItems(albumDtos)
                binding.recycle.adapter = itemAlbumAdapter
                binding.recycle.layoutManager = GridLayoutManager(context, 3)
                binding.recycle.addItemDecoration(SpaceItemDecoration(0, 10.dpToPx, 10.dpToPx))
                binding.recycle.setHasFixedSize(true)
                binding.recycle.itemAnimator = null
                itemAlbumAdapter.setOnItemClickListener(listener = object :
                    BaseRecyclerAdapter.OnItemClickListener<AlbumDto> {
                    override fun onItemClick(holder: Any, item: AlbumDto, position: Int) {
                    }
                })

            }

            else -> {}
        }

        val result = "要求維修日期：${it.urgentTime}"
        val ssb = SpannableStringBuilder(result)
        ssb.setSpan(
            HomeClickSpan(
                requireContext(),
                ContextCompat.getColor(requireActivity(), nearby.lib.uikit.R.color.blue),
                ""
            ), 7, result.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE
        )
        binding.top.date.highlightColor = Color.TRANSPARENT
        binding.top.date.text = ssb

    }

    private fun saveImageToExternalStorage(context: Context, imageUrl: String, fileName: String) {
        Glide.with(context)
            .asBitmap()
            .load(imageUrl)
            .into(object : SimpleTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    // 在这里处理 Bitmap，例如保存到外部存储

                    // 保存到外部存储
                    saveBitmapToExternalStorage(context, resource, fileName)
                }
            })
    }

    private fun saveBitmapToExternalStorage(context: Context, bitmap: Bitmap, fileName: String) {
        val externalStorageState = Environment.getExternalStorageState()

        if (externalStorageState == Environment.MEDIA_MOUNTED) {
            val externalStorageDirectory = context.getExternalFilesDir(null)

            if (externalStorageDirectory != null) {
                val file = File(externalStorageDirectory, fileName)

                try {
                    FileOutputStream(file).use { out ->
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
                        // 图片保存成功
                    }
                    println("保存圖片成功路徑 ${file.absolutePath}")
                    // 其次把文件插入到系统图库
                    try {
                        MediaStore.Images.Media.insertImage(
                            context.contentResolver,
                            file.absolutePath,
                            fileName,
                            null
                        )
                        context.sendBroadcast(
                            Intent(
                                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                                Uri.parse(file.absolutePath)
                            )
                        )
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    // 图片保存失败
                }
            }
        }
    }

    class HomeClickSpan(var context: Context, var colorId: Int, var url: String) : ClickableSpan() {
        override fun onClick(widget: View) {
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.color = colorId
            ds.isUnderlineText = false; //去掉下划线

        }
    }

}
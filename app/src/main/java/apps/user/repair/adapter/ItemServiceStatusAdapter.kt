package apps.user.repair.adapter

import androidx.core.content.ContextCompat
import apps.user.repair.R
import apps.user.repair.databinding.ItemServiceStatusBinding
import apps.user.repair.model.ServiceDto
import apps.user.repair.uitl.ConstantUtil
import com.bumptech.glide.Glide
import nearby.lib.uikit.recyclerview.BaseBindRecyclerAdapter


class ItemServiceStatusAdapter : BaseBindRecyclerAdapter<ItemServiceStatusBinding, ServiceDto>() {

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_service_status
    }

    override fun onBindingItem(binding: ItemServiceStatusBinding, item: ServiceDto, position: Int) {
        binding.address.text = item.repairAddress
        binding.addressNo.text = "${item.describes} "

        Glide.with(context).load(item.addressImage).into(binding.image)
        println("我来了.... ${item.state}")
        when (item.state) {
            ConstantUtil.SERVICE_STATUS_QUOTE -> {
                binding.addressStatus.text = "準備報價中"
                println("我来了....when ${ConstantUtil.SERVICE_STATUS_QUOTE}")
                val img = ContextCompat.getDrawable(context, R.drawable.index_status_shape_1)
                binding.addressStatus.setCompoundDrawables(img, null, null, null)
                binding.addressStatus.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.item_status_1
                    )
                )
            }

            ConstantUtil.SERVICE_STATUS_QUOTED -> {
                binding.addressStatus.text = "已报价，等待確認"
                println("我来了....when ${ConstantUtil.SERVICE_STATUS_QUOTED}")
                val img = ContextCompat.getDrawable(context, R.drawable.index_status_shape_2)
                binding.addressStatus.setCompoundDrawables(img, null, null, null)
                binding.addressStatus.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.item_status_2
                    )
                )
            }

            ConstantUtil.SERVICE_STATUS_CONFIRM -> {
                binding.addressStatus.text = "已确认，正在安排师傅"
                println("我来了....when ${ConstantUtil.SERVICE_STATUS_CONFIRM}")
                val img = ContextCompat.getDrawable(context, R.drawable.index_status_shape_3)
                binding.addressStatus.setCompoundDrawables(img, null, null, null)
                binding.addressStatus.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.item_status_3
                    )
                )
            }

            ConstantUtil.SERVICE_STATUS_FINISH -> {
                binding.addressStatus.text = "已完成"
                println("我来了....when ${ConstantUtil.SERVICE_STATUS_FINISH}")
                val img = ContextCompat.getDrawable(context, R.drawable.index_status_shape_4)
                binding.addressStatus.setCompoundDrawables(img, null, null, null)
                binding.addressStatus.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.item_status_4
                    )
                )
            }
        }
    }
}
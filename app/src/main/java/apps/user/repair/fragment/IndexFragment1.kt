package apps.user.repair.fragment

import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import apps.user.repair.R
import apps.user.repair.databinding.FragmentIndex1Binding
import apps.user.repair.dialog.RequestDialogFragment
import apps.user.repair.http.IndexViewModel
import apps.user.repair.model.AdBannerDto
import apps.user.repair.model.IndexItemTagDto
import apps.user.repair.uitl.ConstantUtil
import apps.user.repair.uitl.SPreUtil
import com.youth.banner.indicator.RectangleIndicator
import nearby.lib.base.bar.BarHelperConfig
import nearby.lib.base.exts.observeNonNull
import nearby.lib.mvvm.fragment.BaseAppBVMFragment
import nearby.lib.uikit.recyclerview.BaseRecyclerAdapter
import nearby.lib.uikit.recyclerview.SpaceItemDecoration
import nearby.lib.uikit.widgets.dpToPx

/**
 * @author: lr
 * @created on: 2023/12/18 9:13 PM
 * @description:
 */
class IndexFragment1 : BaseAppBVMFragment<FragmentIndex1Binding, IndexViewModel>() {
    override fun createViewModel(): IndexViewModel {
        return IndexViewModel()
    }

    override fun layoutRes(): Int {
        return R.layout.fragment_index_1
    }

    private var indexAdv: ArrayList<AdBannerDto> = arrayListOf()
    private var activityItems = mutableListOf<IndexItemTagDto>()
    private val indexTagAdapter by lazy { apps.user.repair.adapter.ItemIndexContentTagAdapter() }
    override fun initialize(savedInstanceState: Bundle?) {
        //0:水喉渠务，1:防漏防水，2:门窗，3:木工，4:废物处理，5:冷气，6:电灯，7:定期保养，8:其他
        activityItems.add(IndexItemTagDto("水喉渠務", R.drawable.item1, type = 0))
        activityItems.add(IndexItemTagDto("防漏防水", R.drawable.item2, type = 1))
        activityItems.add(IndexItemTagDto("門窗", R.drawable.item3, type = 2))
        activityItems.add(IndexItemTagDto("木工", R.drawable.item4, type = 3))
        activityItems.add(IndexItemTagDto("廢物處理", R.drawable.item1, type = 4))
        activityItems.add(IndexItemTagDto("冷氣", R.drawable.item2, type = 5))
        activityItems.add(IndexItemTagDto("電燈", R.drawable.item3, type = 6))
        activityItems.add(IndexItemTagDto("定期保養", R.drawable.item4, type = 7))
        activityItems.add(IndexItemTagDto("其他", R.drawable.item1, type = 8))
        indexTagAdapter.setItems(activityItems)
        binding.recycle.adapter = indexTagAdapter
        binding.recycle.layoutManager = GridLayoutManager(context, 3)
        binding.recycle.addItemDecoration(SpaceItemDecoration(0, 10.dpToPx, 10.dpToPx))
        binding.recycle.setHasFixedSize(true)
        binding.recycle.itemAnimator = null
        indexTagAdapter.setOnItemClickListener(listener = object :
            BaseRecyclerAdapter.OnItemClickListener<IndexItemTagDto> {
            override fun onItemClick(holder: Any, item: IndexItemTagDto, position: Int) {
                val request = RequestDialogFragment()
                val arguments = Bundle()
                arguments.putInt("type", item.type)
                request.arguments = arguments
                request.show(this@IndexFragment1)
            }
        })
        indexAdv.add(AdBannerDto(pictureUrl = "", pictureUrlInt = R.drawable.ad_banner))
        indexAdv.add(AdBannerDto(pictureUrl = "", pictureUrlInt = R.drawable.ad_banner))
        indexAdv.add(AdBannerDto(pictureUrl = "", pictureUrlInt = R.drawable.ad_banner))
        binding.adBanner.addBannerLifecycleObserver(this)
        binding.adBanner.setBannerRound(20f)
        binding.adBanner.indicator = RectangleIndicator(requireActivity())
        binding.adBanner.setAdapter(ImageAdapter(requireActivity(), indexAdv))
        binding.adBanner.setOnBannerListener { data, _ ->
        }
        binding.adBanner.setDatas(indexAdv)
        binding.srl.setColorSchemeColors(nearby.lib.base.R.color.dodgerblue)

        binding.srl.setOnRefreshListener {
            Handler().postDelayed({
                binding.srl.isRefreshing = false

            }, 2000)
        }
        //請求維修列表
        val id = SPreUtil[requireActivity(), "id", "1"]
        viewModel.inventoryId(id.toString())
        viewModel.serviceDtos.observeNonNull(this) {
            // 这里可以异步请求数据，刷新完成后调用 refreshLayout.finishRefresh()
            if (it.size == 0) {
                return@observeNonNull
            }
            binding.address.text = it[0].repairAddress
            binding.addressNo.text = it[0].describes

            when (it[0].state) {
                ConstantUtil.SERVICE_STATUS_QUOTE -> {
                    binding.addressStatus.text = "準備報價中"
                    println("我来了....when ${ConstantUtil.SERVICE_STATUS_QUOTE}")
                    val img = ContextCompat.getDrawable(requireActivity(), R.drawable.index_status_shape_1)
                    binding.addressStatus.setCompoundDrawables(img, null, null, null)
                    binding.addressStatus.setTextColor(
                        ContextCompat.getColor(
                            requireActivity(),
                            R.color.item_status_1
                        )
                    )
                }

                ConstantUtil.SERVICE_STATUS_QUOTED -> {
                    binding.addressStatus.text = "已报价，等待確認"
                    println("我来了....when ${ConstantUtil.SERVICE_STATUS_QUOTED}")
                    val img = ContextCompat.getDrawable(requireActivity(), R.drawable.index_status_shape_2)
                    binding.addressStatus.setCompoundDrawables(img, null, null, null)
                    binding.addressStatus.setTextColor(
                        ContextCompat.getColor(
                            requireActivity(),
                            R.color.item_status_2
                        )
                    )
                }

                ConstantUtil.SERVICE_STATUS_CONFIRM -> {
                    binding.addressStatus.text = "已确认，正在安排师傅"
                    println("我来了....when ${ConstantUtil.SERVICE_STATUS_CONFIRM}")
                    val img = ContextCompat.getDrawable(requireActivity(), R.drawable.index_status_shape_3)
                    binding.addressStatus.setCompoundDrawables(img, null, null, null)
                    binding.addressStatus.setTextColor(
                        ContextCompat.getColor(
                            requireActivity(),
                            R.color.item_status_3
                        )
                    )
                }

                ConstantUtil.SERVICE_STATUS_FINISH -> {
                    binding.addressStatus.text = "已完成"
                    println("我来了....when ${ConstantUtil.SERVICE_STATUS_FINISH}")
                    val img = ContextCompat.getDrawable(requireActivity(), R.drawable.index_status_shape_4)
                    binding.addressStatus.setCompoundDrawables(img, null, null, null)
                    binding.addressStatus.setTextColor(
                        ContextCompat.getColor(
                            requireActivity(),
                            R.color.item_status_4
                        )
                    )
                }
            }
        }
    }

    override fun initBarHelperConfig(): BarHelperConfig {
        return BarHelperConfig.builder().setBack(false)
            .setBgColor(nearby.lib.base.R.color.dodgerblue).setTitle(
                titleGravity = Gravity.LEFT,
                title = getString(R.string.select_services),
                titleColor = nearby.lib.base.R.color.white
            ).build()
    }
}
package apps.user.repair.fragment

import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import androidx.recyclerview.widget.GridLayoutManager
import apps.user.repair.R
import apps.user.repair.databinding.FragmentIndex1Binding
import apps.user.repair.http.IndexViewModel
import apps.user.repair.model.AdBannerDto
import apps.user.repair.model.IndexItemTagDto
import com.youth.banner.indicator.RectangleIndicator
import nearby.lib.base.bar.BarHelperConfig
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
        activityItems.add(IndexItemTagDto("水喉渠務", R.drawable.item1))
        activityItems.add(IndexItemTagDto("防漏防水", R.drawable.item2))
        activityItems.add(IndexItemTagDto("門窗", R.drawable.item3))
        activityItems.add(IndexItemTagDto("木工", R.drawable.item4))
        activityItems.add(IndexItemTagDto("廢物處理", R.drawable.item1))
        activityItems.add(IndexItemTagDto("冷氣", R.drawable.item2))
        activityItems.add(IndexItemTagDto("電燈", R.drawable.item3))
        activityItems.add(IndexItemTagDto("定期保養", R.drawable.item4))
        activityItems.add(IndexItemTagDto("其他", R.drawable.item1))
        indexTagAdapter.setItems(activityItems)
        binding.recycle.adapter = indexTagAdapter
        binding.recycle.layoutManager = GridLayoutManager(context, 3)
        binding.recycle.addItemDecoration(SpaceItemDecoration(0, 10.dpToPx, 10.dpToPx))
        binding.recycle.setHasFixedSize(true)
        binding.recycle.itemAnimator = null
        indexTagAdapter.setOnItemClickListener(listener = object :
            BaseRecyclerAdapter.OnItemClickListener<IndexItemTagDto> {
            override fun onItemClick(holder: Any, item: IndexItemTagDto, position: Int) {
                val request = apps.user.repair.dialog.RequestDialogFragment()
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
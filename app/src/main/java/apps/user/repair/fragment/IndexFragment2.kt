package apps.user.repair.fragment

import android.os.Bundle
import android.view.Gravity
import androidx.recyclerview.widget.LinearLayoutManager
import apps.user.repair.R
import apps.user.repair.databinding.FragmentIndex2Binding
import apps.user.repair.dialog.RequestStatusDialogFragment
import apps.user.repair.http.IndexViewModel
import apps.user.repair.model.ServiceDto
import apps.user.repair.uitl.ConstantUtil
import com.app.toast.ToastX
import com.app.toast.expand.dp
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
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
class IndexFragment2 :
    BaseAppBVMFragment<FragmentIndex2Binding, IndexViewModel>() {
    override fun createViewModel(): IndexViewModel {
        return IndexViewModel()
    }

    override fun layoutRes(): Int {
        return R.layout.fragment_index_2
    }

    private var activityItems = mutableListOf<ServiceDto>()
    private val indexTagAdapter by lazy { apps.user.repair.adapter.ItemServiceStatusAdapter() }
    private var layoutManager: LinearLayoutManager? = null
    override fun initialize(savedInstanceState: Bundle?) {
        //請求維修列表
        viewModel.inventory()
        viewModel.serviceDtos.observeNonNull(this) {
            if (it.size == 0) {
                return@observeNonNull
            }
            activityItems.addAll(it)
            indexTagAdapter.notifyDataSetChanged()
        }
        initRefresh()
    }

    private fun initRefresh() {
        activityItems.add(
            ServiceDto(
                "學校大門",
                "編號",
                "預備報價中",
                ConstantUtil.SERVICE_STATUS_QUOTE
            )
        )
        activityItems.add(
            ServiceDto(
                "學校大門",
                "編號",
                "已报价，等待確認",
                ConstantUtil.SERVICE_STATUS_QUOTED
            )
        )
        activityItems.add(
            ServiceDto(
                "學校大門",
                "編號",
                "已确认，正在安排师傅",
                ConstantUtil.SERVICE_STATUS_CONFIRM
            )
        )
        activityItems.add(
            ServiceDto(
                "學校大門",
                "編號",
                "已完成",
                ConstantUtil.SERVICE_STATUS_FINISH
            )
        )
        activityItems.add(
            ServiceDto(
                "學校大門",
                "編號",
                "預備報價中",
                ConstantUtil.SERVICE_STATUS_QUOTE
            )
        )
        activityItems.add(
            ServiceDto(
                "學校大門",
                "編號",
                "已报价，等待確認",
                ConstantUtil.SERVICE_STATUS_QUOTED
            )
        )
        activityItems.add(
            ServiceDto(
                "學校大門",
                "編號",
                "已确认，正在安排师傅",
                ConstantUtil.SERVICE_STATUS_CONFIRM
            )
        )
        activityItems.add(
            ServiceDto(
                "學校大門",
                "編號",
                "已完成",
                ConstantUtil.SERVICE_STATUS_FINISH
            )
        )
        activityItems.add(
            ServiceDto(
                "學校大門",
                "編號",
                "預備報價中",
                ConstantUtil.SERVICE_STATUS_QUOTE
            )
        )
        layoutManager = LinearLayoutManager(context)
        indexTagAdapter.setItems(activityItems)
        binding.recycle.adapter = indexTagAdapter
        binding.recycle.layoutManager = layoutManager
        binding.recycle.addItemDecoration(SpaceItemDecoration(12.dpToPx, 0.dpToPx, 0.dpToPx))
        binding.recycle.setHasFixedSize(true)
        binding.recycle.itemAnimator = null
        indexTagAdapter.setOnItemClickListener(listener = object :
            BaseRecyclerAdapter.OnItemClickListener<ServiceDto> {
            override fun onItemClick(holder: Any, item: ServiceDto, position: Int) {
                val request = RequestStatusDialogFragment()
                val bundle = Bundle()
                bundle.putInt("status", item.status)
                bundle.putString("address", item.schoolName)
                bundle.putString("status_text", item.statusText)
                request.arguments = bundle
                request.setGravity(Gravity.BOTTOM)
                request.show(this@IndexFragment2)
            }
        })

        binding.srl.setRefreshHeader(ClassicsHeader(requireActivity()))
        binding.srl.setRefreshFooter(ClassicsFooter(requireActivity()))
        //设置 Footer 为 球脉冲 样式
//        binding.srl.setRefreshFooter(BallPulseFooter(requireActivity()).setSpinnerStyle(SpinnerStyle.Scale));

        binding.srl.setOnRefreshListener(OnRefreshListener { refreshlayout ->
            refreshlayout.finishRefresh(2000 /*,false*/) //传入false表示刷新失败
        })
        binding.srl.setOnLoadMoreListener(OnLoadMoreListener { refreshlayout ->
            refreshlayout.finishLoadMore(2000 /*,false*/) //传入false表示加载失败
        })


        // 设置下拉刷新监听
        binding.srl.setOnRefreshListener { refreshLayout -> // 执行刷新操作
            // 这里可以异步请求数据，刷新完成后调用 refreshLayout.finishRefresh()
            binding.srl.finishRefresh()
        }

        // 设置上拉加载更多监听
        binding.srl.setOnLoadMoreListener { refreshLayout -> // 执行加载更多操作
            // 这里可以异步请求数据，加载完成后调用 refreshLayout.finishLoadMore()
            binding.srl.finishLoadMore()
        }
    }


    override fun initBarHelperConfig(): BarHelperConfig {
        return BarHelperConfig.builder().setBack(false)
            .setBgColor(nearby.lib.base.R.color.dodgerblue)
            .setTitle(
                title = getString(R.string.menu_02),
                titleColor = nearby.lib.base.R.color.white
            ).build()
    }
}
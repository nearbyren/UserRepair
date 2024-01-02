package apps.user.repair.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import apps.user.repair.R
import apps.user.repair.databinding.FragmentIndex3Binding
import apps.user.repair.http.IndexViewModel
import com.app.toast.ToastX
import com.app.toast.expand.dp
import nearby.lib.base.bar.BarHelperConfig
import nearby.lib.base.exts.observeNullable
import nearby.lib.mvvm.fragment.BaseAppBVMFragment

/**
 * @author: lr
 * @created on: 2023/12/18 9:13 PM
 * @description:
 */
class IndexFragment3 : BaseAppBVMFragment<FragmentIndex3Binding, IndexViewModel>() {
    override fun createViewModel(): IndexViewModel {
        return IndexViewModel()
    }

    override fun layoutRes(): Int {
        return R.layout.fragment_index_3
    }

    override fun initialize(savedInstanceState: Bundle?) {
        viewModel.noteDto.observeNullable(this) {
            if (!TextUtils.isEmpty(it.msg)) {
                toast(it.msg)
                return@observeNullable
            }
            toast("提交成功")
        }
        binding.button.setOnClickListener {
            val schoolName = binding.schoolName.text.toString()
            val name = binding.nameEt.text.toString()
            val telephone = binding.telephoneEt.text.toString()
            val queryContent = binding.queryContentEt.text.toString()
            if (TextUtils.isEmpty(schoolName)) {
                toast("請輸入學校名稱")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(name)) {
                toast("請輸入姓名")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(telephone)) {
                toast("請輸入電話")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(queryContent)) {
                toast("請輸入查詢內容")
                return@setOnClickListener
            }
            viewModel.note(schoolName, name, telephone, queryContent)
        }
    }

    override fun initBarHelperConfig(): BarHelperConfig {
        return BarHelperConfig.builder().setBack(false)
            .setBgColor(nearby.lib.base.R.color.dodgerblue)
            .setTitle(
                title = getString(R.string.menu_03),
                titleColor = nearby.lib.base.R.color.white
            ).build()
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

}
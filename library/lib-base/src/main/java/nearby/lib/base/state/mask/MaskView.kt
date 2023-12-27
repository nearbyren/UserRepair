package nearby.lib.base.state.mask

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import nearby.lib.base.state.StateBox
import nearby.lib.base.state.base.BaseStateView
import nearby.lib.base.state.base.ViewCache
import nearby.lib.base.state.util.StateBoxUtil

@SuppressLint("ViewConstructor")
class MaskView(view: View, reloadListener: StateBox.OnReloadListener?) : FrameLayout(view.context) {
    private var preCallback: Class<out BaseStateView>? = null
    private val reloadListener: StateBox.OnReloadListener? = reloadListener

    /**
     * 展示成功页面（被遮罩层页面）
     */
    fun setupSuccessLayout(state: BaseStateView) {
        if (StateBoxUtil.checkNotNull(state)) {
            state.setContext(context)
            val rootView: View? = state.getRootView()
            rootView?.visibility = INVISIBLE
            addView(
                rootView, ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            )
        }
    }

    fun show(state: Class<out BaseStateView?>, maskedView: MaskedView) {
        if (StateBoxUtil.checkNotNull(state)) {
            if (StateBoxUtil.isMainThread) {
                println("测试 isMainThread if " + Thread.currentThread().name)
                showWithMainThread(state, maskedView)
            } else {
                println("测试 isMainThread else " + Thread.currentThread().name)
                post { showWithMainThread(state, maskedView) }
            }
        }
    }

    private fun showWithMainThread(status: Class<out BaseStateView?>, maskedView: MaskedView) {
        if (preCallback == status) { //重复调用
            println("测试 preCallback")
            return
        }

        //销毁上一个页面
        val preBaseStateView: BaseStateView? = ViewCache[preCallback]
        println("测试 preBaseStateView $preBaseStateView  preCallback $preCallback")
        if (StateBoxUtil.checkNotNull(preBaseStateView)) {
            println("测试 onDetach")
            preBaseStateView?.onDetach()
        }
        if (childCount > 0) {
            println("测试 for $parent")
            for (i in 0 until childCount) {
                println("测试 for " + getChildAt(i))
            }
        }

        //清理容器页面元素
        if (childCount > 1) {
            println("测试 removeViewAt")
            removeViewAt(1)
        }
        val currentStateView: BaseStateView? = ViewCache[status]
        println("测试 currentStateView $currentStateView  status $status")
        if (status == MaskedView::class.java && StateBoxUtil.checkNotNull(maskedView)) {
            println("测试 if show " + maskedView.getRootView())
            //显示被遮罩层
            maskedView.show()
        } else if (StateBoxUtil.checkNotNull(currentStateView, maskedView)) {
            println("测试 else hide " + maskedView.getRootView())
            maskedView.hide()
            currentStateView?.setContext(context)
            currentStateView?.setOnReloadListener(reloadListener)
            val rootView: View? = currentStateView?.getRootView()
            println("测试 else $rootView")
            addRootView(rootView)
            currentStateView?.onAttach(context, rootView)
        }
        preCallback = status
    }

    private fun addRootView(rootView: View?) {
        if (StateBoxUtil.checkNotNull(rootView)) {
            println("测试 addRootView ${rootView?.parent}")
            val parentView = rootView?.parent
            val childIndex =
                if (parentView == null) -1 else (parentView as ViewGroup).indexOfChild(rootView)
            println("测试 addRootView $childIndex parentView = $parentView rootView = $rootView")
            if (childIndex >= 0) {
                (parentView as ViewGroup).removeViewAt(childIndex)
            }
            addView(rootView)
        }
    }
}

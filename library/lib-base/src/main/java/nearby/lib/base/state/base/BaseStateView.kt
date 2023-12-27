package nearby.lib.base.state.base

import android.content.Context
import android.view.View
import nearby.lib.base.state.StateBox
import nearby.lib.base.state.util.IoUtils
import java.io.*


abstract class BaseStateView : Serializable, Cloneable {
    private var rootView: View? = null
    private var context: Context? = null
    private var onReloadListener: StateBox.OnReloadListener? = null

    constructor() {}
    constructor(view: View?, context: Context?, onReloadListener: StateBox.OnReloadListener?) {
        rootView = view
        this.context = context
        this.onReloadListener = onReloadListener
    }

    fun setOnReloadListener(onReloadListener: StateBox.OnReloadListener?) {
        this.onReloadListener = onReloadListener
    }

    fun setContext(context: Context?) {
        this.context = context
    }

    fun getRootView(): View? {
        val resId = onCreateView()
        if (resId == 0 && rootView != null) {
            return rootView
        }
        if (onBuildView(context) != null) {
            rootView = onBuildView(context)
        }
        if (rootView == null) {
            rootView = View.inflate(context, onCreateView(), null)
        }
        onViewCreate(context, rootView)
        println("测试 onViewCreate $rootView")
        return rootView
    }

    fun getOnReloadListener(): StateBox.OnReloadListener? {
        return onReloadListener
    }

    protected fun onBuildView(context: Context?): View? {
        return null
    }

    fun obtainRootView(): View? {
        if (rootView == null) {
            rootView = View.inflate(context, onCreateView(), null)
        }
        return rootView
    }

    protected abstract fun onCreateView(): Int
    open fun onViewCreate(context: Context?, view: View?) {}
    open fun onAttach(context: Context?, view: View?) {}
    open fun onDetach() {}
    protected operator fun <T : View?> get(resId: Int): T {
        return obtainRootView()!!.findViewById(resId)
    }

    /**
     * 通过复制流的形式完成对象深拷贝
     */
    public override fun clone(): BaseStateView {
        val bao = ByteArrayOutputStream()
        var oos: ObjectOutputStream? = null
        var byteArrayInputStream: ByteArrayInputStream? = null
        var objectInputStream: ObjectInputStream? = null
        try {
            //将object对象写入到ByteArrayOutputStream
            oos = ObjectOutputStream(bao)
            oos.writeObject(this)

            //将ByteArrayOutputStream写入到object
            byteArrayInputStream = ByteArrayInputStream(bao.toByteArray())
            objectInputStream = ObjectInputStream(byteArrayInputStream)

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            IoUtils.close(oos, bao, objectInputStream, byteArrayInputStream)
        }
        return (objectInputStream?.readObject() as BaseStateView)
    }
}

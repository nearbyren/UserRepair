package nearby.lib.base.activity

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import nearby.lib.base.R
import nearby.lib.base.bar.BarHelperConfig
import nearby.lib.base.bar.ToolBarHelperUtil
import nearby.lib.base.bar.ToolBarHelperUtilBuilder
import nearby.lib.base.network.NetworkListenerHelper
import nearby.lib.base.network.NetworkStateView
import nearby.lib.base.network.NetworkStatus
import nearby.lib.base.network.StateAction
import nearby.lib.base.state.BoxManager
import nearby.lib.base.state.StateBox
import nearby.lib.base.uitl.AppManager
import nearby.lib.base.uitl.SystemUIUtils
import nearby.lib.base.uitl.ToastEvent
import nearby.lib.base.uitl.ToastUtils
import nearby.lib.router.Router

abstract class BaseActivity : AppCompatActivity(), ViewBehavior,
    NetworkListenerHelper.NetworkConnectedListener {

    lateinit var barHelper: ToolBarHelperUtilBuilder

    protected val simpleBaseName: String get() = javaClass.simpleName

    var stateManager: BoxManager? = null

    //    private var mStateView: StateView? = null
//    private var mAllStateView: StateView? = null
//     lateinit var splashScreen: SplashScreen
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        splashScreen = installSplashScreen()
        AppManager.getInstance().addActivity(this)
        SystemUIUtils.transparentStatusBar(this)
        if (!SystemUIUtils.setStatusBarDarkTheme(this, true)) {
            //如果不支持设置深色风格 为了兼容总不能让状态栏白白的看不清, 于是设置一个状态栏颜色为半透明,
            //这样半透明+白=灰, 状态栏的文字能看得清
            SystemUIUtils.setStatusBarColor(this, 0x55000000);
        }
        if (Router.isOpen()) Router.inject(this) else println("非组件化模式")
        initIntent()
        initContentView()
        initViewHeader()
        initState()
        initialize(savedInstanceState)
        NetworkListenerHelper.addListener(this)
    }

    override fun onDestroy() {
        AppManager.getInstance().finishActivity(this)
        super.onDestroy()
    }


    open fun initIntent() {}

    protected open fun initContentView() {
        setContentView(layoutRes())
    }

//    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
//        event?.let {
//            if (keyCode == KeyEvent.KEYCODE_BACK && it.getRepeatCount() == 0) {
//                return false;
//            }
//        }
//        return super.onKeyDown(keyCode, event)
//
//    }

    protected open fun initBarHelperConfig(): BarHelperConfig {
        return BarHelperConfig.builder().build()
    }

    private fun initState() {
        val res = getLoading()
        if (res > 0) {
            stateManager = StateBox.default.inject(this, res, object : StateBox.OnReloadListener {
                override fun onReload(v: View?) {
                    loadData(StateAction.ISRETRY)
                }
            })
        }
    }

    override fun onNetworkConnected(isConnected: Boolean, networkStatus: NetworkStatus) {
        val res = getLoading()
        if (res > 0) {
            val status = networkStatus.status
            val desc = networkStatus.desc
            if (isConnected) {
                stateManager?.hidden()
                loadData(StateAction.ISNETWROK)
            } else {
                stateManager?.show(NetworkStateView::class.java)
            }
        }
    }

    open fun getLoading(): Int {
        return -1
    }

    open fun loadData(action: StateAction) {

    }


    private fun initViewHeader() {
        //标题栏适配器
        barHelper = ToolBarHelperUtil.builder()

        //标题栏config
        val barHelperConfig = initBarHelperConfig()

        barHelperConfig!!.activity = this

        barHelperConfig.let {
            barHelper.setBarHelperConfig(barHelperConfig)
        }
        barHelper.build().init()

    }


    override fun getResources(): Resources {
        var resources = super.getResources()
        val newConfig = resources.configuration
        val displayMetrics = resources.displayMetrics
        if (resources != null && newConfig.fontScale != 1f) {
            newConfig.fontScale = 1f
            val configurationContext = createConfigurationContext(newConfig)
            resources = configurationContext.resources
            displayMetrics.scaledDensity = displayMetrics.density * newConfig.fontScale
        }
        return resources
    }

    @LayoutRes
    protected abstract fun layoutRes(): Int

    protected abstract fun initialize(savedInstanceState: Bundle?)


    protected fun showToast(text: String, showLong: Boolean = false) {
        showToast(ToastEvent(content = text, showLong = showLong))
    }

    protected fun showToast(@StringRes resId: Int, showLong: Boolean = false) {
        showToast(ToastEvent(contentResId = resId, showLong = showLong))
    }

    override fun showToast(event: ToastEvent) {
        if (event.content != null) {
            ToastUtils.showToast(event.content!!, event.showLong)
        } else if (event.contentResId != null) {
            ToastUtils.showToast(getString(event.contentResId!!), event.showLong)
        }
    }

    override fun navigate(page: Any) {
        startActivity(Intent(this, page as Class<*>))
        overridePendingTransition(R.anim.anim_no, R.anim.anim_no)
    }

    override fun navigateData(intent: Intent) {
        startActivity(intent)
        overridePendingTransition(R.anim.anim_no, R.anim.anim_no)
    }

    override fun backPress(arg: Any?) {
        onBackPressed()
    }

    override fun finishPage(arg: Any?) {
        finish()
        overridePendingTransition(R.anim.anim_no, R.anim.anim_no)
    }
}

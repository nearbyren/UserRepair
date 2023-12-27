package nearby.lib.mvvm.vm

import android.annotation.SuppressLint
import android.app.Application
import androidx.annotation.StringRes
import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import nearby.lib.base.activity.ViewBehavior
import nearby.lib.base.uitl.ToastEvent

/**
 * @author: lr
 * @created on: 2022/7/10 11:4ViewModelLifecycle9 上午
 * @description:
 */
abstract class BaseViewModel : ViewModel(), ViewModelLifecycle, ViewBehavior {
    /**
     * 创建ViewModel的工厂，以此方法创建的ViewModel，可在构造函数中传参
     */
    class ViewModelFactory(val viewModel: BaseViewModel) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return viewModel as T
        }
    }

    companion object {
        @JvmStatic
        fun <T : BaseViewModel> createViewModelFactory(viewModel: T): ViewModelProvider.Factory {
            return ViewModelFactory(viewModel)
        }
    }

    // loading视图显示Event
    var _loadingEvent = MutableLiveData<Boolean>()
        private set

    // 无数据视图显示Event
    var _emptyPageEvent = MutableLiveData<Boolean>()
        private set

    // 数据视图显示Event
    var _contentPageEvent = MutableLiveData<Boolean>()
        private set

    // 无网络视图显示Event
    var _networkPageEvent = MutableLiveData<Boolean>()
        private set

    // toast提示Event
    var _toastEvent = MutableLiveData<ToastEvent>()
        private set

    // 不带参数的页面跳转Event
    var _pageNavigationEvent = MutableLiveData<Any>()
        private set

    // 点击系统返回键Event
    var _backPressEvent = MutableLiveData<Any?>()
        private set

    // 关闭页面Event
    var _finishPageEvent = MutableLiveData<Any?>()
        private set

    @SuppressLint("StaticFieldLeak")
    lateinit var application: Application

    private lateinit var lifcycleOwner: LifecycleOwner

    /**
     * 在主线程中执行一个协程
     */
    protected fun launchOnUI(block: suspend CoroutineScope.() -> Unit): Job {
        return viewModelScope.launch(Dispatchers.Main) { block() }
    }

    /**
     * 在IO线程中执行一个协程
     */
    protected fun launchOnIO(block: suspend CoroutineScope.() -> Unit): Job {
        return viewModelScope.launch(Dispatchers.IO) { block() }
    }

    override fun onAny(owner: LifecycleOwner, event: Lifecycle.Event) {
        this.lifcycleOwner = owner
    }

    override fun onCreate() {

    }

    override fun onStart() {

    }

    override fun onResume() {

    }

    override fun onPause() {

    }

    override fun onStop() {

    }

    override fun onDestroy() {

    }

    override fun showLoadingView(isShow: Boolean) {
        _loadingEvent.postValue(isShow)
    }

    override fun showToast(event: ToastEvent) {
        _toastEvent.postValue(event)
    }

    protected fun showToast(text: String, showLong: Boolean = false) {
        showToast(ToastEvent(content = text, showLong = showLong))
    }

    protected fun showToast(@StringRes resId: Int, showLong: Boolean = false) {
        showToast(ToastEvent(contentResId = resId, showLong = showLong))
    }

    override fun navigate(page: Any) {
        _pageNavigationEvent.postValue(page)
    }

    override fun backPress(arg: Any?) {
        _backPressEvent.postValue(arg)
    }

    protected fun backPress() {
        backPress(null)
    }

    override fun finishPage(arg: Any?) {
        _finishPageEvent.postValue(arg)
    }

    protected fun finishPage() {
        finishPage(null)
    }
}
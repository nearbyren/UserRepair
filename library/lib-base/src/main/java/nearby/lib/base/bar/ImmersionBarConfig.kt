package nearby.lib.base.bar

import android.view.View
import android.view.WindowManager
import com.gyf.immersionbar.BarHide
import com.gyf.immersionbar.OnBarListener
import com.gyf.immersionbar.OnKeyboardListener
import com.gyf.immersionbar.OnNavigationBarListener

/**
 * @author:
 * @created on: 2022/7/22 19:39
 * @description:
 */
data class ImmersionBarConfig(
    //状态栏颜色，不写默认透明色
    var statusBarColor: Int = 0,
    //导航栏颜色，不写默认黑色
    var navigationBarColor: Int = 0,
    //状态栏透明度，不写默认0.0f
    var statusBarAlpha: Float = 0.0f,
    //导航栏透明度，不写默认0.0F
    var navigationBarAlpha: Float = 0.0f,
    //状态栏和导航栏透明度，不写默认0.0f
    var barAlpha: Float = 0.0f,
    //状态栏字体是深色，不写默认为亮色
    var statusBarDarkFont: Boolean = true,
    //修改flyme OS状态栏字体颜色
    var flymeOSStatusBarFontColor: Int = 0,
    //有导航栏的情况下，activity全屏显示，也就是activity最下面被导航栏覆盖，不写默认非全屏
    var fullScreen: Boolean = false,
    //隐藏状态栏或导航栏或两者，不写默认不隐藏
    var hideBar: BarHide = BarHide.FLAG_SHOW_BAR,
    //设置支持view变色，可以添加多个view，不指定颜色，默认和状态栏同色，还有两个重载方法
    var addViewSupportTransformColor: View? = null,
    //解决状态栏和布局重叠问题，任选其一
    var titleBar: View? = null,
    //解决状态栏和布局重叠问题，任选其一
    var titleBarMarginTop: View? = null,
    //解决状态栏和布局重叠问题，任选其一
    var statusBarView: View? = null,
    //解决状态栏和布局重叠问题，任选其一，默认为false，当为true时一定要指定statusBarColor()，不然状态栏为透明色，还有一些重载方法
    var fitsSystemWindows: Boolean = false,
    //支持ActionBar使用
    var supportActionBar: Boolean = false,
    //状态栏变色后的颜色
    var statusBarColorTransform: Int = 0,
    //导航栏变色后的颜色
    var navigationBarColorTransform: Int = 0,
    //状态栏和导航栏变色后的颜色
    var barColorTransform: Int = 0,
    //状态栏和导航栏变色后的颜色
    var removeSupportView: View? = null,
    //移除全部view支持
    var removeSupportAllView: Boolean = false,
    //是否可以修改导航栏颜色，默认为true
    var navigationBarEnable: Boolean = true,
    //是否可以修改安卓4.4和emui3.1手机导航栏颜色，默认为true
    var navigationBarWithKitkatEnable: Boolean = true,
    //给以上设置的参数打标记
    var addTag: String = "",
    //根据tag获得沉浸式参数
    var getTag: String = "",
    //重置所以沉浸式参数
    var reset: Boolean = false,
    //解决软键盘与底部输入框冲突问题，默认为false，还有一个重载方法，可以指定软键盘mode
    var keyboardEnable: Boolean = false,
    //单独指定软键盘模式
    var keyboardMode: Int = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE,
    //软键盘监听回调
    var onKeyboardListener: OnKeyboardListener? = null,
    var onBarListener: OnBarListener? = null,
    var onNavigationBarListener: OnNavigationBarListener? = null,
) {
    companion object {
        @JvmStatic
        fun builder() = ImmersionBarConfigBuilder()
    }
}

class ImmersionBarConfigBuilder {

    private val config: ImmersionBarConfig = ImmersionBarConfig()

    fun statusBarColor(statusBarColor: Int): ImmersionBarConfigBuilder {
        config.statusBarColor = statusBarColor
        return this
    }

    fun navigationBarColor(navigationBarColor: Int): ImmersionBarConfigBuilder {
        config.navigationBarColor = navigationBarColor
        return this
    }

    fun statusBarAlpha(statusBarAlpha: Float): ImmersionBarConfigBuilder {
        config.statusBarAlpha = statusBarAlpha
        return this
    }

    fun barAlpha(barAlpha: Float): ImmersionBarConfigBuilder {
        config.barAlpha = barAlpha
        return this
    }

    fun statusBarDarkFont(statusBarDarkFont: Boolean): ImmersionBarConfigBuilder {
        config.statusBarDarkFont = statusBarDarkFont
        return this
    }


    fun flymeOSStatusBarFontColor(flymeOSStatusBarFontColor: Int): ImmersionBarConfigBuilder {
        config.flymeOSStatusBarFontColor = flymeOSStatusBarFontColor
        return this
    }

    fun fullScreen(fullScreen: Boolean): ImmersionBarConfigBuilder {
        config.fullScreen = fullScreen
        return this
    }

    fun hideBar(hideBar: BarHide): ImmersionBarConfigBuilder {
        config.hideBar = hideBar
        return this
    }

    fun addViewSupportTransformColor(addViewSupportTransformColor: View): ImmersionBarConfigBuilder {
        config.addViewSupportTransformColor = addViewSupportTransformColor
        return this
    }

    fun titleBar(titleBar: View): ImmersionBarConfigBuilder {
        config.titleBar = titleBar
        return this
    }

    fun titleBarMarginTop(titleBarMarginTop: View): ImmersionBarConfigBuilder {
        config.titleBarMarginTop = titleBarMarginTop
        return this
    }

    fun statusBarView(statusBarView: View): ImmersionBarConfigBuilder {
        config.statusBarView = statusBarView
        return this
    }

    fun fitsSystemWindows(fitsSystemWindows: Boolean): ImmersionBarConfigBuilder {
        config.fitsSystemWindows = fitsSystemWindows
        return this
    }

    fun supportActionBar(supportActionBar: Boolean): ImmersionBarConfigBuilder {
        config.supportActionBar = supportActionBar
        return this
    }

    fun statusBarColorTransform(statusBarColor: Int): ImmersionBarConfigBuilder {
        config.statusBarColorTransform = statusBarColor
        return this
    }

    fun navigationBarColorTransform(navigationBarColorTransform: Int): ImmersionBarConfigBuilder {
        config.navigationBarColorTransform = navigationBarColorTransform
        return this
    }

    fun barColorTransform(barColorTransform: Int): ImmersionBarConfigBuilder {
        config.barColorTransform = barColorTransform
        return this
    }

    fun removeSupportView(removeSupportView: View): ImmersionBarConfigBuilder {
        config.removeSupportView = removeSupportView
        return this
    }

    fun removeSupportAllView(removeSupportAllView: Boolean): ImmersionBarConfigBuilder {
        config.removeSupportAllView = removeSupportAllView
        return this
    }

    fun navigationBarEnable(navigationBarEnable: Boolean): ImmersionBarConfigBuilder {
        config.navigationBarEnable = navigationBarEnable
        return this
    }

    fun navigationBarWithKitkatEnable(navigationBarWithKitkatEnable: Boolean): ImmersionBarConfigBuilder {
        config.navigationBarWithKitkatEnable = navigationBarWithKitkatEnable
        return this
    }


    fun addTag(addTag: String): ImmersionBarConfigBuilder {
        config.addTag = addTag
        return this
    }

    fun getTag(getTag: String): ImmersionBarConfigBuilder {
        config.getTag = getTag
        return this
    }

    fun reset(reset: Boolean): ImmersionBarConfigBuilder {
        config.reset = reset
        return this
    }

    fun keyboardEnable(keyboardEnable: Boolean): ImmersionBarConfigBuilder {
        config.keyboardEnable = keyboardEnable
        return this
    }

    fun keyboardMode(keyboardMode: Int): ImmersionBarConfigBuilder {
        config.keyboardMode = keyboardMode
        return this
    }

    fun onKeyboardListener(onKeyboardListener: OnKeyboardListener): ImmersionBarConfigBuilder {
        config.onKeyboardListener = onKeyboardListener
        return this
    }

    fun onKeyboardListener(onBarListener: OnBarListener): ImmersionBarConfigBuilder {
        config.onBarListener = onBarListener
        return this
    }

    fun onKeyboardListener(onNavigationBarListener: OnNavigationBarListener): ImmersionBarConfigBuilder {
        config.onNavigationBarListener = onNavigationBarListener
        return this
    }

    fun build(): ImmersionBarConfig = config

}
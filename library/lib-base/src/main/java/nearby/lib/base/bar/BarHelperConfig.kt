package nearby.lib.base.bar

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import nearby.lib.base.R
import nearby.lib.base.activity.BaseActivity

/**
 * @author:
 * @created on: 2022/7/22 20:24
 * @description:
 */
data class BarHelperConfig(
    var activity: BaseActivity? = null,
    var fragmentView: View? = null,
    var onBackListener: BarOnBackListener? = null,
    var onNextListener: BarOnNextListener? = null,
    var onEmptyListener: DataOnEmptyListener? = null,
    var onErrorListener: DataOnErrorListener? = null,
    var immersionBarConfig: ImmersionBarConfig? = null,
    var layout: Int = 0,
    var bgColor: Int = 0,
    var back: Boolean = false,
    var title: String? = null,
    var titleColor: Int = 0,
    var titleGravity: Int = Gravity.CENTER,
    var titleBold: Boolean = false,
    var rightText: String? = null,
    var rightTextColor: Int = 0,
    var parent: ViewGroup? = null,
    var content: View? = null,
    var iconLeft: Int = 0,
    var iconRight: Int = 0,
) {

    companion object {
        @JvmStatic
        fun builder() = BarHelperConfigBuilder()
    }

}

class BarHelperConfigBuilder() {
    private val config: BarHelperConfig = BarHelperConfig()


    fun setActivity(activity: BaseActivity): BarHelperConfigBuilder {
        config.activity = activity
        return this
    }

    fun setFragmentView(fragmentView: View): BarHelperConfigBuilder {
        config.fragmentView = fragmentView
        return this
    }

    fun setOnBackListener(onBackListener: BarOnBackListener): BarHelperConfigBuilder {
        config.onBackListener = onBackListener
        return this
    }

    fun setOnNextListener(onNextListener: BarOnNextListener): BarHelperConfigBuilder {
        config.onNextListener = onNextListener
        return this
    }

    fun setOnEmptyListener(onEmptyListener: DataOnEmptyListener): BarHelperConfigBuilder {
        config.onEmptyListener = onEmptyListener
        return this
    }

    fun setOnEmptyListener(onErrorListener: DataOnErrorListener): BarHelperConfigBuilder {
        config.onErrorListener = onErrorListener
        return this
    }

    fun setImmersionBarConfig(immersionBarConfig: ImmersionBarConfig): BarHelperConfigBuilder {
        config.immersionBarConfig = immersionBarConfig
        return this
    }

    fun setTitle(title: String, titleColor: Int = R.color.title_text_color, titleGravity: Int = Gravity.CENTER, titleBold: Boolean = true): BarHelperConfigBuilder {
        config.title = title
        config.titleColor = titleColor
        config.titleGravity = titleGravity
        config.titleBold = titleBold
        return this
    }


    fun setRightText(rightText: String, rightTextColor: Int = R.color.black): BarHelperConfigBuilder {
        config.rightText = rightText
        config.rightTextColor = rightTextColor
        return this
    }

    fun setParent(parent: ViewGroup): BarHelperConfigBuilder {
        config.parent = parent
        return this
    }

    fun setContent(content: View): BarHelperConfigBuilder {
        config.content = content
        return this
    }

    fun setBack(back: Boolean): BarHelperConfigBuilder {
        config.back = back
        return this
    }


    fun setBgColor(bgColor: Int): BarHelperConfigBuilder {
        config.bgColor = bgColor
        return this
    }


    fun setIconLeft(iconLeft: Int): BarHelperConfigBuilder {
        config.iconLeft = iconLeft
        return this
    }

    fun setIconRight(iconRight: Int): BarHelperConfigBuilder {
        config.iconRight = iconRight
        return this
    }


    fun build(): BarHelperConfig = config


}
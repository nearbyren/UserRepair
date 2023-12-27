package nearby.lib.uikit.round

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import nearby.lib.uikit.R


/**
 * @description: 圆角Button
 * @since: 1.0.0
 */
open class RoundButton : AppCompatButton, IRoundImpl {
    override var helper: RHelper = RHelper(this)

    private var normalTextColor = Color.BLACK
    private var disabledTextColor = normalTextColor


    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        initAttrs(attrs)
        buildRoundBackground()
        processTextColor()
    }

    @SuppressLint("CustomViewStyleable")
    private fun initAttrs(attrs: AttributeSet?) {
        attrs?.let {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.lib_uikit_RoundView)
            initRoundAttrs(ta)
            normalTextColor = ta.getColor(
                R.styleable.lib_uikit_RoundView_fu_disabledBgColor,
                normalTextColor
            )
            disabledTextColor = ta.getColor(
                R.styleable.lib_uikit_RoundView_fu_disabledBgColor,
                RHelper.INVALID_COLOR
            )
            if (disabledTextColor == RHelper.INVALID_COLOR) {
                disabledTextColor = normalTextColor
            }
            ta.recycle()
        }
    }

    /**
     * 处理文字颜色
     */
    fun processTextColor(enabled: Boolean? = null) {
        if (enabled ?: isEnabled) {
            setTextColor(normalTextColor)
        } else {
            setTextColor(disabledTextColor)
        }
    }

    fun setNormalTextColor(color: Int) {
        this.normalTextColor = color
        processTextColor()
    }

    fun setDisabledTextColor(color: Int) {
        this.disabledTextColor = color
        processTextColor()
    }
}
package nearby.lib.uikit.widgets

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import nearby.lib.uikit.R

/**
 * @description: 可设置不同字体、大小、颜色、间距的TextView
 */
class MultiTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var leftText: String? = null
    private var leftTextColor = Color.BLACK
    private var leftTextSize = 12f
    private var leftTextFontFamily: String? = null
    private var leftTextBold = false
    private var leftMarginHorizontal = 0

    private var middleText: String? = null
    private var middleTextColor = Color.BLACK
    private var middleTextSize = 12f
    private var middleTextFontFamily: String? = null
    private var middleTextBold = false
    private var middleMarginHorizontal = 0

    private var rightText: String? = null
    private var rightTextColor = Color.BLACK
    private var rightTextSize = 12f
    private var rightTextFontFamily: String? = null
    private var rightTextBold = false
    private var rightMarginHorizontal = 0
    private lateinit var view: View

    init {
        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.MultiTextView)

        leftText = typedArray.getString(R.styleable.MultiTextView_leftText)
        leftTextColor =
            typedArray.getColor(R.styleable.MultiTextView_leftTextColor, Color.BLACK)
        leftTextSize = typedArray.getDimension(R.styleable.MultiTextView_leftTextSize, 12f)
        leftTextFontFamily = typedArray.getString(R.styleable.MultiTextView_leftTextFontFamily)
        leftTextBold = typedArray.getBoolean(R.styleable.MultiTextView_leftTextBold, false)

        middleText = typedArray.getString(R.styleable.MultiTextView_middleText)
        middleTextColor =
            typedArray.getColor(R.styleable.MultiTextView_middleTextColor, Color.BLACK)
        middleTextSize = typedArray.getDimension(R.styleable.MultiTextView_middleTextSize, 12f)
        middleTextFontFamily =
            typedArray.getString(R.styleable.MultiTextView_middleTextFontFamily)
        middleTextBold = typedArray.getBoolean(R.styleable.MultiTextView_middleTextBold, false)

        rightText = typedArray.getString(R.styleable.MultiTextView_rightText)
        rightTextColor =
            typedArray.getColor(R.styleable.MultiTextView_rightTextColor, Color.BLACK)
        rightTextSize = typedArray.getDimension(R.styleable.MultiTextView_rightTextSize, 12f)
        rightTextFontFamily =
            typedArray.getString(R.styleable.MultiTextView_rightTextFontFamily)
        rightTextBold = typedArray.getBoolean(R.styleable.MultiTextView_rightTextBold, false)

        for (i in 0 until typedArray.indexCount) {
            when (val attr = typedArray.getIndex(i)) {
                R.styleable.MultiTextView_leftMarginHorizontal -> {
                    leftMarginHorizontal = typedArray.getDimensionPixelSize(attr, 0)
                }
                R.styleable.MultiTextView_middleMarginHorizontal -> {
                    middleMarginHorizontal = typedArray.getDimensionPixelSize(attr, 0)
                }
                R.styleable.MultiTextView_rightMarginHorizontal -> {
                    rightMarginHorizontal = typedArray.getDimensionPixelSize(attr, 0)
                }
            }
        }

        initData()

        typedArray.recycle()
    }

    private fun initData() {
        view = View.inflate(context, R.layout.multi_text_view, this)

        view.findViewById<TextView>(R.id.tv_left).run {
            leftText?.let { text = it }
            setTextColor(leftTextColor)
            setTextSize(TypedValue.COMPLEX_UNIT_PX, leftTextSize)
            typeface = if (leftTextBold) Typeface.defaultFromStyle(Typeface.BOLD)
            else Typeface.defaultFromStyle(Typeface.NORMAL)
            layoutParams = (layoutParams as LinearLayout.LayoutParams).apply {
                leftMargin = leftMarginHorizontal
                rightMargin = leftMarginHorizontal
            }
            leftTextFontFamily?.let {
                typeface = Typeface.createFromAsset(context.assets, "fonts/$it")
            }
        }

        view.findViewById<TextView>(R.id.tv_middle).run {
            middleText?.let { text = it }
            setTextColor(middleTextColor)
            setTextSize(TypedValue.COMPLEX_UNIT_PX, middleTextSize)
            typeface = if (middleTextBold) Typeface.defaultFromStyle(Typeface.BOLD)
            else Typeface.defaultFromStyle(Typeface.NORMAL)
            layoutParams = (layoutParams as LinearLayout.LayoutParams).apply {
                leftMargin = middleMarginHorizontal
                rightMargin = middleMarginHorizontal
            }
            middleTextFontFamily?.let {
                typeface = Typeface.createFromAsset(context.assets, "fonts/$it")
            }
        }

        view.findViewById<TextView>(R.id.tv_right).run {
            rightText?.let { text = it }
            setTextColor(rightTextColor)
            setTextSize(TypedValue.COMPLEX_UNIT_PX, rightTextSize)
            typeface = if (rightTextBold) Typeface.defaultFromStyle(Typeface.BOLD)
            else Typeface.defaultFromStyle(Typeface.NORMAL)
            layoutParams = (layoutParams as LinearLayout.LayoutParams).apply {
                leftMargin = rightMarginHorizontal
                rightMargin = rightMarginHorizontal
            }
            rightTextFontFamily?.let {
                typeface = Typeface.createFromAsset(context.assets, "fonts/$it")
            }
        }
    }
    fun setLeftTextSize(size: Float = 12f) {
        view.findViewById<TextView>(R.id.tv_left).run {
            setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
        }
    }
    fun setLeftText(text: CharSequence?, @ColorInt color: Int = leftTextColor) {
        text?.let { view.findViewById<TextView>(R.id.tv_left).text = text }
        view.findViewById<TextView>(R.id.tv_left).setTextColor(color)
    }

    fun getLeftText(): String {
        return view.findViewById<TextView>(R.id.tv_left).text.toString()
    }

    fun setMiddleText(text: CharSequence?, @ColorInt color: Int = middleTextColor) {
        text?.let { view.findViewById<TextView>(R.id.tv_middle).text = text }
        view.findViewById<TextView>(R.id.tv_middle).setTextColor(color)
    }

    fun getMiddleText(): String {
        return view.findViewById<TextView>(R.id.tv_middle).text.toString()
    }

    fun setRightText(text: CharSequence?, @ColorInt color: Int = rightTextColor) {
        text?.let { view.findViewById<TextView>(R.id.tv_right).text = text }
        view.findViewById<TextView>(R.id.tv_right).setTextColor(color)
    }

    fun getRightText(): String {
        return view.findViewById<TextView>(R.id.tv_right).text.toString()
    }

    fun setLeftTextColor(@ColorInt color: Int) {
        view.findViewById<TextView>(R.id.tv_left).setTextColor(color)
    }

    fun setMiddleTextColor(@ColorInt color: Int) {
        view.findViewById<TextView>(R.id.tv_middle).setTextColor(color)
    }

    fun setRightTextColor(@ColorInt color: Int) {
        view.findViewById<TextView>(R.id.tv_right).setTextColor(color)
    }

    fun setTextColor(@ColorInt color: Int) {
        view.findViewById<TextView>(R.id.tv_left).setTextColor(color)
        view.findViewById<TextView>(R.id.tv_middle).setTextColor(color)
        view.findViewById<TextView>(R.id.tv_right).setTextColor(color)
    }

    fun setLeftVisibility(visibility: Int) {
        setMyVisibility(view.findViewById<TextView>(R.id.tv_left), visibility)
    }

    fun setMiddleVisibility(visibility: Int) {
        setMyVisibility(view.findViewById<TextView>(R.id.tv_middle), visibility)
    }

    fun setRightVisibility(visibility: Int) {
        setMyVisibility(view.findViewById<TextView>(R.id.tv_right), visibility)
    }

    private fun setMyVisibility(view: TextView, visibility: Int) {
        when (visibility) {
            VISIBLE -> view.visibility = VISIBLE
            GONE -> view.visibility = GONE
            INVISIBLE -> view.visibility = INVISIBLE
        }
    }
}
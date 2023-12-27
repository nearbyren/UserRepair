package nearby.lib.uikit.widgets

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import nearby.lib.uikit.R

/**
 * 缩放ImageView 宽度填满View控件 高度根据比例缩放 若高度超出View控件 可以自行选择截掉顶部/底部
 */
class ScaleImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private var viewWidth: Int = 0
    private var viewHeight: Int = 0
    private var drawableWidth: Int = 0
    private var drawableHeight: Int = 0
    private var scale: Float = 1f
    private var mScaleAnchor: ScaleAnchor = ScaleAnchor.TOP

    enum class ScaleAnchor(value: Int) {
        TOP(0),
        BOTTOM(1),
    }

    private val mScaleAnchorArray = listOf(
        ScaleAnchor.TOP,
        ScaleAnchor.BOTTOM,
    )

    init {
        scaleType = ScaleType.MATRIX

        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.ScaleImageView)
        val index = typedArray.getInt(R.styleable.ScaleImageView_scaleAnchor, 0)
        setScaleAnchor(mScaleAnchorArray[index])

        typedArray.recycle()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        recomputeImgMatrix()
    }

    override fun setFrame(l: Int, t: Int, r: Int, b: Int): Boolean {
        recomputeImgMatrix()
        return super.setFrame(l, t, r, b)
    }

    private fun recomputeImgMatrix() {
        val matrix = imageMatrix

        viewWidth = width - paddingLeft - paddingRight
        viewHeight = height - paddingTop - paddingBottom
        drawableWidth = drawable.intrinsicWidth
        drawableHeight = drawable.intrinsicHeight

        scale = viewWidth.toFloat() / drawableWidth.toFloat()
        println("ScaleImageView scale=$scale viewWidth=$viewWidth " +
                "viewHeight=$viewHeight drawableWidth=$drawableWidth drawableHeight=$drawableHeight " +
                "xAnchor=${xAnchor()} yAnchor=${yAnchor()}")

        // x/y 轴锚点是缩放过程中位置不变的坐标
        matrix.setScale(scale, scale, xAnchor(), yAnchor())
        imageMatrix = matrix
    }

    fun setScaleAnchor(scaleAnchor: ScaleAnchor?) {
        if (scaleAnchor == null) {
            throw NullPointerException()
        }
        if (mScaleAnchor != scaleAnchor) {
            mScaleAnchor = scaleAnchor
            requestLayout()
            invalidate()
        }
    }

    /**
     * 获取x轴锚点 由于所有图片都是根据View控件宽度适配 所以x轴锚点始终是0
     */
    private fun xAnchor(): Float {
        return 0F
    }

    /**
     * 获取y轴锚点
     */
    private fun yAnchor(): Float {
        return when (mScaleAnchor) {
            ScaleAnchor.TOP -> 0F
            ScaleAnchor.BOTTOM -> {
                val tranY = viewHeight  - drawableHeight * scale
                tranY / (1 - scale)
            }
        }
    }

}
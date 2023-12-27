package nearby.lib.uikit.widgets

/**
 * @author: lr
 * @created on: 2022/11/6 11:22 上午
 * @description:
 */
import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import nearby.lib.uikit.widgets.dpToPx

class BannerRoundImageView @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : AppCompatImageView(context!!, attrs, defStyleAttr) {
    private var mPath: Path? = null
    private var mRectF: RectF? = null

    /*圆角的半径，依次为左上角xy半径，右上角，右下角，左下角*/
    private val rids =
        floatArrayOf(12f.dpToPx.toFloat(), 12f.dpToPx.toFloat(), 12f.dpToPx.toFloat(), 12f.dpToPx.toFloat(), 12f.dpToPx.toFloat(), 12f.dpToPx.toFloat(), 12f.dpToPx.toFloat(), 12f.dpToPx.toFloat())

    private fun init() {
        mPath = Path()
        mRectF = RectF()
    }



    /**
     * 画图
     * @param canvas
     */
    override fun onDraw(canvas: Canvas) {
        val w = this.width
        val h = this.height
        mRectF!![0f, 0f, w.toFloat()] = h.toFloat()
        mPath!!.addRoundRect(mRectF!!, rids, Path.Direction.CW)
        canvas.clipPath(mPath!!)
        super.onDraw(canvas)
    }

    init {
        init()
    }
}

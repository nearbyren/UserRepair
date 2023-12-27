package nearby.lib.base.loading

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet


/**
 * @author:
 * @created on: 2022/9/29 10:40
 * @description:
 */
class LVCircularZoom : LVBase {
    private var mPaint: Paint? = null
    private var mWidth = 0f
    private var mHigh = 0f
    private val mMaxRadius = 8f
    private val circularCount = 3
    private var mAnimatedValue = 1.0f
    private var mJumpValue = 0

    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mWidth = measuredWidth.toFloat()
        mHigh = measuredHeight.toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val circularX = mWidth / circularCount
        for (i in 0 until circularCount) {
            if (i == mJumpValue % circularCount) {
                mPaint?.color = Color.rgb(255, 255, 255)
                mPaint?.let {
                    canvas.drawCircle(i * circularX + circularX / 2f, mHigh / 2, mMaxRadius, it)
                }
            } else {
                mPaint?.color = Color.argb(52, 0, 0, 0)
                mPaint?.let {
                    canvas.drawCircle(i * circularX + circularX / 2f, mHigh / 2, mMaxRadius, it)
                }
            }
        }
    }

    private fun initPaint() {
        mPaint = Paint()
        mPaint!!.isAntiAlias = true
        mPaint!!.style = Paint.Style.FILL
        mPaint!!.color = Color.WHITE
    }

    fun setViewColor(color: Int) {
        mPaint!!.color = color
        postInvalidate()
    }

    override fun OnAnimationRepeat(animation: Animator?) {
        mJumpValue++
    }


    override fun OnStopAnim(): Int {
        mAnimatedValue = 0f
        mJumpValue = 0
        return 0
    }

    override fun SetAnimRepeatMode(): Int {
        return ValueAnimator.RESTART
    }

    override fun InitPaint() {
        initPaint()
    }

    override fun OnAnimationUpdate(valueAnimator: ValueAnimator?) {
        if (valueAnimator != null) {
            mAnimatedValue = valueAnimator.animatedValue as Float
        }
        if (mAnimatedValue < 0.2) {
            mAnimatedValue = 0.2f
        }
        invalidate()
    }

    override fun AinmIsRunning() {}
    override fun SetAnimRepeatCount(): Int {
        return ValueAnimator.INFINITE
    }
}
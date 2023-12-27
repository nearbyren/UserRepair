package nearby.lib.base.loading

import android.animation.Animator
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.content.Context
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator


/**
 * @author:
 * @created on: 2022/9/28 14:58
 * @description:
 */
abstract class LVBase(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : View(context, attrs, defStyleAttr) {
    constructor(context: Context?) : this(context, null) {}
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0) {}

    fun startAnim() {
        stopAnim()
        startViewAnim(0f, 1f, 500)
    }

    fun startAnim(time: Int) {
        stopAnim()
        startViewAnim(0f, 1f, time.toLong())
    }

    fun stopAnim() {
        if (valueAnimator != null) {
            clearAnimation()
            valueAnimator!!.repeatCount = 0
            valueAnimator!!.cancel()
            valueAnimator!!.end()
            if (OnStopAnim() == 0) {
                valueAnimator!!.repeatCount = 0
                valueAnimator!!.cancel()
                valueAnimator!!.end()
            }
        }
    }

    var valueAnimator: ValueAnimator? = null
    private fun startViewAnim(startF: Float, endF: Float, time: Long): ValueAnimator? {
        valueAnimator = ValueAnimator.ofFloat(startF, endF)
        valueAnimator!!.setDuration(time)
        valueAnimator!!.setInterpolator(LinearInterpolator())
        valueAnimator!!.setRepeatCount(SetAnimRepeatCount())
        if (ValueAnimator.RESTART == SetAnimRepeatMode()) {
            valueAnimator!!.setRepeatMode(ValueAnimator.RESTART)
        } else if (ValueAnimator.REVERSE == SetAnimRepeatMode()) {
            valueAnimator!!.setRepeatMode(ValueAnimator.REVERSE)
        }
        valueAnimator!!.addUpdateListener(AnimatorUpdateListener { valueAnimator -> OnAnimationUpdate(valueAnimator) })
//        valueAnimator!!.addListener(object : AnimatorListenerAdapter() {
//            override fun onAnimationEnd(animation: Animator?) {
//                super.onAnimationEnd(animation)
//            }
//
//            override fun onAnimationStart(animation: Animator?) {
//                super.onAnimationStart(animation)
//            }
//
//            override fun onAnimationRepeat(animation: Animator?) {
//                super.onAnimationRepeat(animation)
//                OnAnimationRepeat(animation)
//            }
//        })
        if (!valueAnimator!!.isRunning()) {
            AinmIsRunning()
            valueAnimator!!.start()
        }
        return valueAnimator
    }

    protected abstract fun InitPaint()
    protected abstract fun OnAnimationUpdate(valueAnimator: ValueAnimator?)
    protected abstract fun OnAnimationRepeat(animation: Animator?)
    protected abstract fun OnStopAnim(): Int
    protected abstract fun SetAnimRepeatMode(): Int
    protected abstract fun SetAnimRepeatCount(): Int
    protected abstract fun AinmIsRunning()
    fun dip2px(dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    fun getFontlength(paint: Paint, str: String): Float {
        val rect = Rect()
        paint.getTextBounds(str, 0, str.length, rect)
        return rect.width().toFloat()
    }

    fun getFontHeight(paint: Paint, str: String): Float {
        val rect = Rect()
        paint.getTextBounds(str, 0, str.length, rect)
        return rect.height().toFloat()
    }

    fun getFontHeight(paint: Paint): Float {
        val fm: Paint.FontMetrics = paint.getFontMetrics()
        return fm.descent - fm.ascent
    }

    init {
        InitPaint()
    }
}
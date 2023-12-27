package nearby.lib.uikit.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewGroup
import android.view.animation.BounceInterpolator
import me.panpf.sketch.SketchImageView
import nearby.lib.uikit.R

/**
 * @author:
 * @created on: 2022/8/5 13:51
 * @description:
 */
class MoveAdView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : SketchImageView(context, attrs, defStyleAttr) {
    private var mLastRawX = 0f
    private var mLastRawY = 0f
    private val TAG = "AttachButton"
    private var isDrug = false
    private var mRootMeasuredWidth = 0
    private var mRootMeasuredHeight = 0
    private var mRootTopY = 0
    private var customIsAttach = false
    private var customIsDrag = false

    /**
     * 初始化自定义属性
     */
    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        val mTypedAttay = context.obtainStyledAttributes(attrs, R.styleable.MoveView)
        customIsAttach = mTypedAttay.getBoolean(R.styleable.MoveView_customIsAttach, true)
        customIsDrag = mTypedAttay.getBoolean(R.styleable.MoveView_customIsDrag, true)
        mTypedAttay.recycle()
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        super.dispatchTouchEvent(event)
        return true
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        //判断是否需要滑动
        if (customIsDrag) {
            //当前手指的坐标
            val mRawX = ev.rawX
            val mRawY = ev.rawY
            when (ev.action) {
                MotionEvent.ACTION_DOWN -> {
                    isDrug = false
                    //记录按下的位置
                    mLastRawX = mRawX
                    mLastRawY = mRawY
                    val mViewGroup = parent as ViewGroup
                    if (mViewGroup != null) {
                        val location = IntArray(2)
                        mViewGroup.getLocationInWindow(location)
                        //获取父布局的高度
                        mRootMeasuredHeight = mViewGroup.measuredHeight
                        mRootMeasuredWidth = mViewGroup.measuredWidth
                        //获取父布局顶点的坐标
                        mRootTopY = location[1]
                    }
                }
                MotionEvent.ACTION_MOVE -> if (mRawX >= 0 && mRawX <= mRootMeasuredWidth && mRawY >= mRootTopY && mRawY <= mRootMeasuredHeight + mRootTopY) {
                    //手指X轴滑动距离
                    val differenceValueX = mRawX - mLastRawX
                    //手指Y轴滑动距离
                    val differenceValueY = mRawY - mLastRawY
                    //判断是否为拖动操作
                    if (!isDrug) {
                        isDrug =
                            if (Math.sqrt((differenceValueX * differenceValueX + differenceValueY * differenceValueY).toDouble()) < 2) {
                                false
                            } else {
                                true
                            }
                    }
                    //获取手指按下的距离与控件本身X轴的距离
                    val ownX = x
                    //获取手指按下的距离与控件本身Y轴的距离
                    val ownY = y
                    //理论中X轴拖动的距离
                    var endX = ownX + differenceValueX
                    //理论中Y轴拖动的距离
                    var endY = ownY + differenceValueY
                    //X轴可以拖动的最大距离
                    val maxX = (mRootMeasuredWidth - width).toFloat()
                    //Y轴可以拖动的最大距离
                    val maxY = (mRootMeasuredHeight - height).toFloat()
                    //X轴边界限制
                    endX = if (endX < 0) 0f else if (endX > maxX) maxX else endX
                    //Y轴边界限制
                    endY = if (endY < 0) 0f else if (endY > maxY) maxY else endY
                    //开始移动
                    x = endX
                    y = endY
                    //记录位置
                    mLastRawX = mRawX
                    mLastRawY = mRawY
                }
                MotionEvent.ACTION_UP ->                     //根据自定义属性判断是否需要贴边
                    if (customIsAttach) {
                        //判断是否为点击事件
                        if (isDrug) {
                            val center = (mRootMeasuredWidth / 2).toFloat()
                            //自动贴边
                            if (mLastRawX <= center) {
                                //向左贴边
                                this@MoveAdView.animate()
                                        .setInterpolator(BounceInterpolator())
                                        .setDuration(500)
                                        .x(0f)
                                        .start()
                            } else {
                                var x =( mRootMeasuredWidth - width).toFloat()
                                //向右贴边
                                this@MoveAdView.animate()
                                        .setInterpolator(BounceInterpolator())
                                        .setDuration(500)
                                        .x((x))
                                        .start()
                            }
                        }
                    }
            }
        }
        //是否拦截事件
        return if (isDrug) isDrug else super.onTouchEvent(ev)
    }

    init {
        initAttrs(context, attrs)
    }
}

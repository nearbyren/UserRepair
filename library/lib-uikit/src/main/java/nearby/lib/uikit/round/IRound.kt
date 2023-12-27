package nearby.lib.uikit.round

/**
 * @description: 圆角相关属性
 * @since: 1.0.0
 */
interface IRound {

    fun setTopLeftRadius(radius: Float)

    fun setTopRightRadius(radius: Float)

    fun setBottomLeftRadius(radius: Float)

    fun setBottomRightRadius(radius: Float)

    fun setRadius(radius: Float)

    /**
     * 传入长度为4的Float数组，同时设置不同的圆角，顺序是：topLeft, topRight, bottomRight, bottomLeft
     */
    fun setRadii(radii: Array<Float>)

    fun setNormalBgColor(color: Int)

    fun setNormalBorderColor(color: Int)

    fun setDisabledBgColor(color: Int)

    fun setDisabledBorderColor(color: Int)

    fun setPressedBgColor(color: Int)

    fun setPressedBorderColor(color: Int)

    fun setBorderWidth(size: Int)

    /**
     * 构建圆角背景
     */
    fun buildRoundBackground()
}
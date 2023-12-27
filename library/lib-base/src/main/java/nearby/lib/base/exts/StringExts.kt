package nearby.lib.base.exts

import nearby.lib.base.uitl.NumberUtils
import java.math.RoundingMode

/**
 * @description: String相关扩展
 * @since: 1.0.0
 */

fun String.plus(
    value: String?,
    precision: Int? = null,
    mode: RoundingMode = RoundingMode.FLOOR
): String {
    return NumberUtils.plus(v1 = this, v2 = value, precision = precision, mode = mode).toString()
}

fun String.minus(
    value: String?,
    precision: Int? = null,
    mode: RoundingMode = RoundingMode.FLOOR
): String {
    return NumberUtils.minus(v1 = this, v2 = value, precision = precision, mode = mode).toString()
}

fun String.multiply(
    value: String?,
    precision: Int? = null,
    mode: RoundingMode = RoundingMode.FLOOR
): String {
    return NumberUtils.multiply(v1 = this, v2 = value, precision = precision, mode = mode)
        .toString()
}

fun String.divide(
    value: String?,
    precision: Int? = null,
    mode: RoundingMode = RoundingMode.FLOOR
): String {
    return NumberUtils.divide(v1 = this, v2 = value, precision = precision, mode = mode).toString()
}


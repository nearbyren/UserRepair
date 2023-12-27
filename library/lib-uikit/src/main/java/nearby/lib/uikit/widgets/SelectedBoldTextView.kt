package nearby.lib.uikit.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

/**
 * @description: TextView选中时加粗
 */
class SelectedBoldTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (isSelected) setTypeface(Typeface.DEFAULT, Typeface.BOLD)
        else setTypeface(Typeface.DEFAULT, Typeface.NORMAL)
    }
}
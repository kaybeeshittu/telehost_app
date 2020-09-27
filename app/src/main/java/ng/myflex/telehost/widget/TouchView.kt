package ng.myflex.telehost.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.RelativeLayout


class TouchView : RelativeLayout {

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (!isEnabled) {
            return true
        }
        return super.dispatchTouchEvent(ev)
    }
}
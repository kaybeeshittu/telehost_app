package ng.myflex.telehost.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.RelativeLayout
import com.hbb20.CCPCountry
import com.hbb20.CountryCodePicker


class CountryCoordinator : RelativeLayout {

    private lateinit var picker: CountryCodePicker

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onViewAdded(child: View?) {
        super.onViewAdded(child)
        if (child is CountryCodePicker) {
            picker = child
        }
    }

    fun setOnSelectListener(listener: (code: CCPCountry) -> Unit) {
        listener(
            CCPCountry(
                picker.selectedCountryNameCode,
                picker.selectedCountryCode,
                picker.selectedCountryName, 0
            )
        )
        picker.setOnCountryChangeListener {
            listener(
                CCPCountry(
                    picker.selectedCountryNameCode,
                    picker.selectedCountryCode,
                    picker.selectedCountryName, 0
                )
            )
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (!isEnabled) {
            return true
        }
        return super.dispatchTouchEvent(ev)
    }
}
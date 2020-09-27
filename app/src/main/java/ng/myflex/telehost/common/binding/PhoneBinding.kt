package ng.myflex.telehost.common.binding

import androidx.databinding.BindingAdapter
import br.com.sapereaude.maskedEditText.MaskedEditText
import com.hbb20.CCPCountry
import com.hbb20.CountryCodePicker
import ng.myflex.telehost.widget.CountryCoordinator

class PhoneBinding {
    companion object Field {
        @BindingAdapter("country")
        @JvmStatic
        fun setMask(view: MaskedEditText, value: CCPCountry?) {
            val mask = "+${value?.phoneCode} ### ### ####"
            if (!view.mask!!.contentEquals(mask) && value != null) {
                view.mask = mask
            }
        }

        @BindingAdapter("android:enabled")
        @JvmStatic
        fun setEnabled(view: CountryCodePicker, value: Boolean) {
            if (value != view.isEnabled) {
                view.isEnabled = value
            }
        }

        @BindingAdapter("android:enabled")
        @JvmStatic
        fun setEnabled(view: CountryCoordinator, value: Boolean) {
            if (value != view.isEnabled) {
                view.isEnabled = value
            }
        }

        @BindingAdapter("onCountrySelected")
        @JvmStatic
        fun setOnCountrySelected(
            view: CountryCoordinator,
            value: (code: CCPCountry) -> Unit
        ) {
            view.setOnSelectListener {
                value(it)
            }
        }
    }
}
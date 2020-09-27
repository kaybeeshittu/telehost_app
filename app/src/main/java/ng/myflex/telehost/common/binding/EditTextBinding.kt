package ng.myflex.telehost.common.binding

import android.util.Patterns
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.databinding.BindingAdapter
import com.basgeekball.awesomevalidation.AwesomeValidation

class EditTextBinding {
    companion object Binding {
        @BindingAdapter("android:enabled")
        @JvmStatic
        fun setEnabled(view: EditText, value: Boolean) {
            if (value != view.isEnabled) {
                view.isEnabled = value
            }
        }

        @BindingAdapter(value = ["validate", "match", "error"], requireAll = false)
        @JvmStatic
        fun validate(view: EditText, validation: AwesomeValidation, match: String?, error: String) {
            var regex = match?.toRegex()
            when {
                view.inputType == EditorInfo.TYPE_CLASS_TEXT or
                        EditorInfo.TYPE_TEXT_VARIATION_EMAIL_ADDRESS -> {
                    regex = Patterns.EMAIL_ADDRESS.toRegex()
                    validation.addValidation(view, Patterns.EMAIL_ADDRESS, error)
                }
                view.inputType == EditorInfo.TYPE_CLASS_PHONE -> {
                    regex = Patterns.PHONE.toRegex()
                    validation.addValidation(view, Patterns.PHONE, error)
                }
                match == null -> {
                }
                else -> validation.addValidation(view, match, error)
            }
            view.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus && regex != null && !regex.matches(view.text)) {
                    view.error = error
                }
            }
        }
    }
}
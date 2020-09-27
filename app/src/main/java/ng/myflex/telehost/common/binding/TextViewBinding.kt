package ng.myflex.telehost.common.binding

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter

class TextViewBinding {
    companion object Binding {
        @BindingAdapter("app:display")
        @JvmStatic
        fun setEnabled(view: TextView, value: String?) {
            val state = if (value == null) View.GONE else View.VISIBLE
            if (state != view.visibility) {
                view.visibility = state
            }
        }
    }
}
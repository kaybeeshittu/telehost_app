package ng.myflex.telehost.util

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.graphics.PorterDuff
import android.os.Build
import android.view.View
import android.view.WindowManager
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.appcompat.widget.Toolbar
import ng.myflex.telehost.R

class Tools {
    companion object {
        fun isDarkTheme(context: Context): Boolean {
            val mode = context.resources.configuration.uiMode and android.content.res
                .Configuration.UI_MODE_NIGHT_MASK

            return mode == Configuration.UI_MODE_NIGHT_YES
        }

        fun setSmartSystemBar(activity: Activity?) {
            if (isDarkTheme(activity!!)) {
                setSystemBarColor(activity, R.color.colorBackground)
            } else {
                setSystemBarColor(activity, R.color.colorBackground)
                setSystemBarLight(activity)
            }
        }

        fun setSystemBarColor(activity: Activity, @ColorRes color: Int) {
            val window = activity.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = activity.resources.getColor(color)
        }

        fun setSystemBarLight(activity: Activity) {
            val view = activity.findViewById<View>(android.R.id.content)
            var flags = view.systemUiVisibility

            flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            view.systemUiVisibility = flags
        }

        fun changeOverflowMenuIconColor(
            toolbar: Toolbar,
            @ColorInt color: Int
        ) {
            try {
                val drawable = toolbar.overflowIcon
                drawable!!.mutate()
                drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
            } catch (e: Exception) {
            }
        }

        fun needRequestPermission(): Boolean {
            return Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1
        }
    }
}
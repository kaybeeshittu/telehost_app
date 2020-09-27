package ng.myflex.telehost.util

import android.content.Context
import android.view.View
import com.google.android.material.snackbar.Snackbar

fun Context.displayError(root: View, message: String) {
    val snack = Snackbar.make(root, message, Snackbar.LENGTH_LONG)
    snack.show()
}
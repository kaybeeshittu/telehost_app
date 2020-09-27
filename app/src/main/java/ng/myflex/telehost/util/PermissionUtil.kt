package ng.myflex.telehost.util

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager

class PermissionUtil {
    companion object {
        private val appPermissions = arrayOf(
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_SMS,
            Manifest.permission.SEND_SMS,
            Manifest.permission.RECEIVE_SMS
        )

        fun getDeniedPermissions(activity: Activity?): Array<String> {
            val deniedPermissions = mutableListOf<String>()
            for (permission in appPermissions) {
                val status = activity?.checkSelfPermission(permission)
                if (status != PackageManager.PERMISSION_GRANTED) {
                    deniedPermissions.add(permission)
                }
            }
            return deniedPermissions.toTypedArray()
        }
    }
}
package ng.myflex.telehost.util

import android.content.Context
import android.provider.Settings
import android.text.TextUtils
import android.view.accessibility.AccessibilityManager

class UssdUtil {
    companion object {
        fun isAccessiblityServicesEnable(context: Context): Boolean {
            val am = context.getSystemService(Context.ACCESSIBILITY_SERVICE)
            if (am is AccessibilityManager) {
                for (service in am.installedAccessibilityServiceList) {
                    if (service.id.contains(context.packageName)) {
                        return isAccessibilitySettingsOn(context, service.id)
                    }
                }
            }
            return false
        }

        private fun isAccessibilitySettingsOn(
            context: Context,
            service: String?
        ): Boolean {
            var accessibilityEnabled = 0
            try {
                accessibilityEnabled = Settings.Secure.getInt(
                    context.applicationContext.contentResolver,
                    Settings.Secure.ACCESSIBILITY_ENABLED
                )
            } catch (e: Settings.SettingNotFoundException) {
                //
            }
            if (accessibilityEnabled == 1) {
                val settingValue = Settings.Secure.getString(
                    context.applicationContext.contentResolver,
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
                )
                if (settingValue != null) {
                    val splitter = TextUtils.SimpleStringSplitter(':')
                    splitter.setString(settingValue)
                    while (splitter.hasNext()) {
                        val accessabilityService = splitter.next()
                        if (accessabilityService.equals(service, ignoreCase = true)) {
                            return true
                        }
                    }
                }
            }
            return false
        }
    }
}
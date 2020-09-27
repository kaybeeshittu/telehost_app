package ng.myflex.telehost.common

import android.content.Context
import ng.myflex.telehost.R

class Properties(private val context: Context) {
    fun getBaseUrl(): String = context.getString(R.string.base_url)

    fun getWebsocketUrl(): String = context.getString(R.string.websocker_url)

    fun getDatabase(): String = context.getString(R.string.database)

    fun getNotificationKey(): String = context.getString(R.string.notification_key)
}
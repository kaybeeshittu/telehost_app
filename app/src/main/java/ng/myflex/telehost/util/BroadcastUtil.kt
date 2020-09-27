package ng.myflex.telehost.util

import android.content.Context
import android.content.Intent
import ng.myflex.telehost.domain.Activity

class BroadcastUtil {
    companion object {
        fun activityChanged(context: Context) {
            val intent = Intent()

            intent.action = Activity::class.java.name

            context.sendBroadcast(intent)
        }
    }
}
package ng.myflex.telehost.adapter.implemetation

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.telephony.SmsManager
import android.telephony.SubscriptionManager
import androidx.core.app.ActivityCompat
import ng.myflex.telehost.adapter.SmsAdapter
import ng.myflex.telehost.domain.Activity
import ng.myflex.telehost.exception.NoCarrierFoundException
import ng.myflex.telehost.exception.SmsPermissionException
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Singleton
class SmsAdapterImplementation @Inject constructor(
    private val context: Context
) : SmsAdapter {
    companion object ServiceIntent {
        const val sent =
            "ng.myflex.telehost.adapter.implemetation.SimpleSmsAdapter.sentIntent"
        const val delivery =
            "ng.myflex.telehost.adapter.implemetation.SimpleSmsAdapter.deliveryIntent"
    }

    private var sentIntent: PendingIntent

    private var deliveryIntent: PendingIntent

    init {
        val code = 0
        val flag = 0

        sentIntent = PendingIntent.getBroadcast(context, code, Intent(sent), flag)
        deliveryIntent = PendingIntent.getBroadcast(context, code, Intent(delivery), flag)
    }

    private fun hasPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_PHONE_STATE
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun simCount(): Int = getSubscriptionManager().activeSubscriptionInfoCountMax

    @Synchronized
    override suspend fun sendMessage(activity: Activity) = suspendCoroutine<Boolean> {
        if (!hasPermission()) {
            throw SmsPermissionException()
        }
        context.registerReceiver(
            object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    when (resultCode) {
                        android.app.Activity.RESULT_OK -> it.resume(true)
                        else -> it.resume(false)
                    }
                    this@SmsAdapterImplementation.context.unregisterReceiver(this)
                }
            },
            IntentFilter(sent)
        )
        getSmsManager(activity.simPort).sendTextMessage(
            activity.number,
            null,
            activity.message,
            sentIntent,
            deliveryIntent
        )
    }

    @SuppressLint("MissingPermission")
    private fun getSmsManager(simPort: Int): SmsManager {
        val infoList = getSubscriptionManager().activeSubscriptionInfoList

        if (infoList.isEmpty()) {
            throw NoCarrierFoundException()
        }
        val port = simPort - 1
        val subscriptionInfo = if (port < infoList.size) infoList[port] else infoList[0]

        return SmsManager.getSmsManagerForSubscriptionId(subscriptionInfo.subscriptionId)
    }

    private fun getSubscriptionManager(): SubscriptionManager {
        return context.getSystemService(
            Context.TELEPHONY_SUBSCRIPTION_SERVICE
        ) as SubscriptionManager
    }
}
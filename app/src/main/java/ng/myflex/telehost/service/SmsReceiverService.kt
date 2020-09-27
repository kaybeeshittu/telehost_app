package ng.myflex.telehost.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage
import dagger.android.AndroidInjection
import ng.myflex.telehost.service.controller.SmsServiceController
import java.util.*
import javax.inject.Inject

class SmsReceiverService : BroadcastReceiver() {
    @Inject
    lateinit var controller: SmsServiceController

    override fun onReceive(context: Context?, intent: Intent?) {
        AndroidInjection.inject(this, context)
        if (intent?.action.equals("android.provider.Telephony.SMS_RECEIVED")) {
            handleMessage(intent)
        }
    }

    private fun handleMessage(intent: Intent?) {
        val bundle = intent?.extras ?: return

        val payload = bundle.get("pdus") as Array<*>
        for (i in payload.indices) {
            val message = SmsMessage.createFromPdu(payload[i] as ByteArray)

            message.originatingAddress?.let {
                controller.syncMessage(message.messageBody, it, Date().toString())
            }
        }
    }
}
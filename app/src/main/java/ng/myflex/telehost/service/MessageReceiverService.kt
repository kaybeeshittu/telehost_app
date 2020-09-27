package ng.myflex.telehost.service

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import dagger.android.AndroidInjection
import ng.myflex.telehost.config.Constant
import ng.myflex.telehost.service.controller.SmsServiceController
import ng.myflex.telehost.service.controller.UssdServiceController
import javax.inject.Inject

class MessageReceiverService : FirebaseMessagingService() {
    companion object {
        private val tag = MessageReceiverService::class.java.name
    }

    @Inject
    lateinit var sessionStorageService: SessionStorageService

    @Inject
    lateinit var smsServiceController: SmsServiceController

    @Inject
    lateinit var ussdServiceController: UssdServiceController

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        Log.d(tag, "FCM token generated! $token")

        sessionStorageService.save(Constant.fcmSession, token)
    }
}
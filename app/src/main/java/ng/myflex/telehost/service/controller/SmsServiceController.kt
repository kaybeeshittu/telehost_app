package ng.myflex.telehost.service.controller

import android.content.Context
import androidx.work.*
import kotlinx.coroutines.*
import ng.myflex.telehost.common.Dispatcher
import ng.myflex.telehost.config.Constant
import ng.myflex.telehost.domain.Activity
import ng.myflex.telehost.domain.enumeration.ActivityStatus
import ng.myflex.telehost.payload.MessagePayload
import ng.myflex.telehost.payload.SmsPayload
import ng.myflex.telehost.service.PlatformService
import ng.myflex.telehost.service.SmsService
import ng.myflex.telehost.service.worker.SmsBackupWorker
import ng.myflex.telehost.service.worker.SmsMessagingWorker
import ng.myflex.telehost.service.worker.SmsSyncWorker
import ng.myflex.telehost.util.BroadcastUtil
import ng.myflex.telehost.util.PayloadUtil
import javax.inject.Inject

class SmsServiceController @Inject constructor(
    private val context: Context,
    private val dispatcher: Dispatcher,
    private val platformService: PlatformService,
    private val smsService: SmsService
) {
    companion object {
        private val tag = SmsServiceController::class.java.name
    }

    private val constraints: Constraints by lazy {
        Constraints
            .Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
    }

    fun sendMessage(model: MessagePayload) = GlobalScope.launch(dispatcher.IO) {
        val data = Data.Builder()
        val activities = createActivities(model)

        BroadcastUtil.activityChanged(context)
        data.putLongArray(Constant.activityKey, activities.map { it.id!! }.toLongArray())

        enqueueActivities(data.build())
    }

    private suspend fun createActivities(model: MessagePayload): List<Activity> {
        val simPort = model.sim.toInt()

        val payload = SmsPayload(
            model.text!!,
            model.userkey,
            model.refCode,
            model.phoneNumber,
            model.updatedAt
        )
        return smsService.createActivities(payload, simPort)
    }

    private fun enqueueActivities(data: Data) {
        val sendTask = OneTimeWorkRequest.Builder(SmsMessagingWorker::class.java)
            .setInputData(data)
            .build()
        val syncTask = OneTimeWorkRequest.Builder(SmsSyncWorker::class.java)
            .setConstraints(constraints)
            .setInputData(data)
            .build()

        WorkManager.getInstance(context)
            .beginUniqueWork(tag, ExistingWorkPolicy.APPEND, sendTask)
            .then(syncTask)
            .enqueue()
    }

    fun syncMessage(text: String, from: String, date: String) = GlobalScope.launch(dispatcher.IO) {
        val data = Data.Builder()
        val activities = createActivityFromText(text, from, date)

        data.putLongArray(Constant.activityKey, activities.map { it.id!! }.toLongArray())

        val sendTask = OneTimeWorkRequest.Builder(SmsBackupWorker::class.java)
            .setConstraints(constraints)
            .setInputData(data.build())
            .build()

        WorkManager.getInstance(context)
            .beginWith(sendTask)
            .enqueue()
    }

    private suspend fun createActivityFromText(
        text: String,
        from: String,
        date: String
    ): List<Activity> {
        val sim = 1
        val accessCode = platformService.getAccessCode()

        val payload = SmsPayload(
            text,
            accessCode,
            PayloadUtil.getReferenceCode(),
            from,
            date
        )
        return smsService.createActivities(payload, sim, ActivityStatus.RECEIVED)
    }
}
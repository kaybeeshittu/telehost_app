package ng.myflex.telehost.service.controller

import android.content.Context
import android.util.Log
import androidx.work.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ng.myflex.telehost.common.Dispatcher
import ng.myflex.telehost.config.Constant
import ng.myflex.telehost.domain.Activity
import ng.myflex.telehost.payload.MessagePayload
import ng.myflex.telehost.payload.UssdPayload
import ng.myflex.telehost.service.UssdService
import ng.myflex.telehost.service.worker.UssdDialWorker
import ng.myflex.telehost.service.worker.UssdSyncWorker
import ng.myflex.telehost.util.BroadcastUtil
import javax.inject.Inject

class UssdServiceController @Inject constructor(
    private val context: Context,
    private val dispatcher: Dispatcher,
    private val ussdService: UssdService
) {
    companion object {
        private val tag = UssdServiceController::class.java.name
    }

    private val constraints: Constraints by lazy {
        Constraints
            .Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
    }

    fun dialUssd(model: MessagePayload) = GlobalScope.launch(dispatcher.IO) {
        Log.d(tag, "dialing ussd ${model.phoneNumber}")

        val data = Data.Builder()
        val activity = createActivities(model)

        BroadcastUtil.activityChanged(context)
        data.putLong(Constant.activityKey, activity.id!!)

        Log.d(tag, "enqueue activity ${activity.id}")
        enqueueActivities(data.build())
    }

    private suspend fun createActivities(model: MessagePayload): Activity {
        val simPort = try {
            model.sim.toInt()
        } catch (e: Exception) {
            1
        }
        val payload = UssdPayload(
            model.phoneNumber,
            model.userkey,
            model.refCode,
            model.params?.map { it.name },
            model.updatedAt
        )

        return ussdService.createActivity(payload, simPort)
    }

    private fun enqueueActivities(data: Data) {
        val sendTask = OneTimeWorkRequest.Builder(UssdDialWorker::class.java)
            .setInputData(data)
            .build()
        val syncTask = OneTimeWorkRequest.Builder(UssdSyncWorker::class.java)
            .setConstraints(constraints)
            .setInputData(data)
            .build()

        WorkManager.getInstance(context)
            .beginUniqueWork(tag, ExistingWorkPolicy.APPEND, sendTask)
            .then(syncTask)
            .enqueue()
    }
}
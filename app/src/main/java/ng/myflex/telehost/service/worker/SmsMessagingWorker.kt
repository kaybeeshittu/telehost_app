package ng.myflex.telehost.service.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import ng.myflex.telehost.common.Dispatcher
import ng.myflex.telehost.common.content.ActivityWorkerFactory
import ng.myflex.telehost.config.Constant
import ng.myflex.telehost.service.SmsService
import ng.myflex.telehost.util.BroadcastUtil
import kotlin.coroutines.CoroutineContext

class SmsMessagingWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val params: WorkerParameters,
    private val smsService: SmsService,
    private val dispatcher: Dispatcher
) : Worker(context, params), CoroutineScope {
    private var coroutineJob: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = dispatcher.IO + coroutineJob

    override fun doWork(): Result {
        return runBlocking {
            val aId = inputData.getLongArray(Constant.activityKey)
            smsService.sendPendingMessages(aId ?: longArrayOf())

            BroadcastUtil.activityChanged(context)
            Result.success()
        }
    }

    @AssistedInject.Factory
    interface Factory : ActivityWorkerFactory
}
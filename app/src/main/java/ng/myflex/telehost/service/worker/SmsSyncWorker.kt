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
import ng.myflex.telehost.util.ServiceResponse
import ng.myflex.telehost.util.execute
import kotlin.coroutines.CoroutineContext

class SmsSyncWorker @AssistedInject constructor(
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
            when (val response = execute {
                smsService.synchronizeSms(aId ?: longArrayOf())
            }) {
                is ServiceResponse.OnSuccess -> {
                    if (response.data) {
                        Result.success()
                    } else {
                        Result.retry()
                    }
                }
                is ServiceResponse.OnError -> Result.retry()
            }
        }
    }

    @AssistedInject.Factory
    interface Factory : ActivityWorkerFactory
}
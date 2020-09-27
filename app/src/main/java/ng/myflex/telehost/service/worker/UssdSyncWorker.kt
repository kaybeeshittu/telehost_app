package ng.myflex.telehost.service.worker

import android.content.Context
import android.util.Log
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
import ng.myflex.telehost.service.UssdService
import ng.myflex.telehost.util.ServiceResponse
import ng.myflex.telehost.util.execute
import kotlin.coroutines.CoroutineContext

class UssdSyncWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val params: WorkerParameters,
    private val ussdService: UssdService,
    private val dispatcher: Dispatcher
) : Worker(context, params), CoroutineScope {
    companion object {
        private val tag = UssdSyncWorker::class.java.name
    }

    private var coroutineJob: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = dispatcher.IO + coroutineJob

    override fun doWork(): Result {
        return runBlocking {
            val id = inputData.getLong(Constant.activityKey, 0)

            Log.d(tag, "starting sync activity $id")
            when (val response = execute { ussdService.synchronizeUssd(id) }) {
                is ServiceResponse.OnSuccess -> {
                    if (response.data) {
                        Log.d(tag, "successful sync activity $id")
                        Result.success()
                    } else {
                        Log.d(tag, "failed sync activity $id")
                        Result.retry()
                    }
                }
                is ServiceResponse.OnError -> {
                    Log.d(tag, "failed sync activity $id")
                    Result.retry()
                }
            }
        }
    }

    @AssistedInject.Factory
    interface Factory : ActivityWorkerFactory
}
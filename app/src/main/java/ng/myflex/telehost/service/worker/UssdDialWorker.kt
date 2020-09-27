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
import ng.myflex.telehost.domain.enumeration.ActivityStatus
import ng.myflex.telehost.service.UssdService
import ng.myflex.telehost.util.BroadcastUtil
import kotlin.coroutines.CoroutineContext

class UssdDialWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val params: WorkerParameters,
    private val ussdService: UssdService,
    private val dispatcher: Dispatcher
) : Worker(context, params), CoroutineScope {
    companion object {
        private val tag = UssdDialWorker::class.java.name
    }

    private var coroutineJob: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = dispatcher.IO + coroutineJob

    override fun doWork(): Result {
        return runBlocking {
            val aId = inputData.getLong(Constant.activityKey, 0)

            Log.d(tag, "starting dial activity $aId")
            if (aId == 0L) {
                Result.success()
            } else {
                val response = ussdService.dialUssd(aId)
                
                BroadcastUtil.activityChanged(context)

                Log.d(tag, "dialed activity $aId")
                // if response is null, there was nothing to dial in the first place
                // so lets exit the job by returning success
                if (response == null || (response.status == ActivityStatus.READY)) {
                    Log.d(tag, "successful dial for activity $aId")
                } else {
                    Log.d(tag, "failed dial for activity $aId")
                }
                Result.success()
            }
        }
    }

    @AssistedInject.Factory
    interface Factory : ActivityWorkerFactory
}
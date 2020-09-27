package ng.myflex.telehost.common.content

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters

interface ActivityWorkerFactory {
    fun create(context: Context, params: WorkerParameters): ListenableWorker
}
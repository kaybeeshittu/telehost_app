package ng.myflex.telehost.service

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.work.Configuration
import androidx.work.WorkManager
import androidx.work.WorkerFactory
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult
import ng.myflex.telehost.config.Constant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BootstrapService @Inject constructor(
    private val context: Context,
    private val workerFactory: WorkerFactory,
    private val sessionStorageService: SessionStorageService
) : OnCompleteListener<InstanceIdResult> {

    fun init() {
        FirebaseApp.initializeApp(context)
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener(this)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        WorkManager.initialize(
            context,
            Configuration.Builder()
                .setWorkerFactory(workerFactory)
                .build()
        )
    }

    override fun onComplete(task: Task<InstanceIdResult>) {
        if (!task.isSuccessful) {
            return
        }
        sessionStorageService.save(Constant.fcmSession, task.result?.token!!)
    }
}
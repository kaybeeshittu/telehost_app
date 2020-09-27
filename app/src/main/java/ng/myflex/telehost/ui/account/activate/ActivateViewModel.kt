package ng.myflex.telehost.ui.account.activate

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ng.myflex.telehost.common.Dispatcher
import ng.myflex.telehost.adapter.SmsAdapter
import ng.myflex.telehost.config.Constant
import ng.myflex.telehost.domain.Activity
import ng.myflex.telehost.domain.Duration
import ng.myflex.telehost.domain.enumeration.ActivityStatus
import ng.myflex.telehost.form.ActivationForm
import ng.myflex.telehost.repository.ActivityRepository
import ng.myflex.telehost.service.PlatformService
import ng.myflex.telehost.service.SessionStorageService
import ng.myflex.telehost.service.controller.SmsServiceController
import ng.myflex.telehost.service.controller.UssdServiceController
import ng.myflex.telehost.util.ServiceResponse
import ng.myflex.telehost.util.execute
import javax.inject.Inject

class ActivateViewModel @Inject constructor(
    private val context: Context,
    private val dispatcher: Dispatcher,
    private val smsAdapter: SmsAdapter,
    private val platformService: PlatformService,
    private val sessionStorageService: SessionStorageService,
    private val activityRepository: ActivityRepository
) : ViewModel() {
    private val jobLiveData: MutableLiveData<Int> = MutableLiveData()
    private val liveData: MutableLiveData<ServiceResponse<Boolean>> = MutableLiveData()

    val form: ActivationForm = ActivationForm()

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            watch()
        }
    }

    init {
        val filter = IntentFilter()

        filter.addAction(Activity::class.java.name)

        context.registerReceiver(receiver, filter)
    }

    fun onActivate(): LiveData<ServiceResponse<Boolean>> = liveData

    fun onJob(): LiveData<Int> = jobLiveData

    fun getDuration(): Array<Duration> = arrayOf(
        Duration("1 Day", "1"),
        Duration("1 Week", "7"),
        Duration("1 Month", "30")
    )

    fun getSlotCount(): Int = smsAdapter.simCount()

    fun getToken(): String? = sessionStorageService.getString(Constant.fcmSession)

    fun watch() {
        viewModelScope.launch {
            val count = activityRepository.sizeByStatus(ActivityStatus.PENDING)
            withContext(dispatcher.main) {
                jobLiveData.postValue(count)
            }
        }
    }

    fun onSubmit(duration: Duration, simPort: Int) {
        viewModelScope.launch(dispatcher.IO) {
            val response = execute {
                platformService.activateSim(
                    form.phoneField.phone.value!!,
                    duration.duration,
                    simPort.toString()
                )
            }
            withContext(dispatcher.main) {
                liveData.postValue(response)
            }
        }
    }

    fun clearTasks() {
        viewModelScope.launch {
            activityRepository.deleteByStatus(ActivityStatus.PENDING)
            WorkManager.getInstance(context)
                .cancelAllWorkByTag(SmsServiceController::class.java.name)
            WorkManager.getInstance(context)
                .cancelAllWorkByTag(UssdServiceController::class.java.name)
            watch()
        }
    }

    override fun onCleared() {
        context.unregisterReceiver(receiver)

        super.onCleared()
    }
}
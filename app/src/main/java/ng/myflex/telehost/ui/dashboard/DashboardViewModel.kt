package ng.myflex.telehost.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ng.myflex.telehost.common.Dispatcher
import ng.myflex.telehost.domain.Account
import ng.myflex.telehost.service.PlatformService
import ng.myflex.telehost.service.PrincipalService
import ng.myflex.telehost.service.SmsService
import ng.myflex.telehost.service.UssdService
import ng.myflex.telehost.util.ServiceResponse
import ng.myflex.telehost.util.execute
import javax.inject.Inject

class DashboardViewModel @Inject constructor(
    private val dispatcher: Dispatcher,
    private val principalService: PrincipalService,
    private val platformService: PlatformService,
    private val smsService: SmsService,
    private val ussdService: UssdService
) : ViewModel() {
    private val liveData: MutableLiveData<ServiceResponse<Account>> = MutableLiveData()

    val routeCount: MutableLiveData<Int> = MutableLiveData()

    val ussdCount: MutableLiveData<Int> = MutableLiveData()

    val smsCount: MutableLiveData<Int> = MutableLiveData()

    fun onResponse(): LiveData<ServiceResponse<Account>> = liveData

    fun initialize(refresh: Boolean = false) {
        viewModelScope.launch(dispatcher.IO) {
            val response = execute { principalService.identity(refresh) }

            var sms = smsService.getSmsCount()
            var ussd = ussdService.getUssdCount()
            var route = platformService.getDevices().size
            when (response) {
                is ServiceResponse.OnSuccess -> {
                    sms = response.data.sms
                    ussd = response.data.ussd
                    route = response.data.devices
                }
                is ServiceResponse.OnError -> {
                }
            }
            withContext(dispatcher.main) {
                routeCount.postValue(route)
                ussdCount.postValue(ussd)
                smsCount.postValue(sms)

                liveData.postValue(response)
            }
        }
    }
}
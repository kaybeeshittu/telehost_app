package ng.myflex.telehost.ui.sms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ng.myflex.telehost.common.Dispatcher
import ng.myflex.telehost.service.PlatformService
import javax.inject.Inject

class SmsViewModel @Inject constructor(
    private val dispatcher: Dispatcher,
    private val platformService: PlatformService
) : ViewModel() {
    fun sendMessage() {
        viewModelScope.launch(dispatcher.IO) {
            //            platformService.scheduleMessage(arrayListOf(), "")
        }
    }
}
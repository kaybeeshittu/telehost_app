package ng.myflex.telehost.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ng.myflex.telehost.common.Dispatcher
import ng.myflex.telehost.domain.Account
import ng.myflex.telehost.service.PrincipalService
import ng.myflex.telehost.util.ServiceResponse
import ng.myflex.telehost.util.execute
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val dispatcher: Dispatcher,
    private val principalService: PrincipalService
) : ViewModel() {
    private val liveData: MutableLiveData<ServiceResponse<Account>> = MutableLiveData()
    private val authLiveData: MutableLiveData<ServiceResponse<Unit>> = MutableLiveData()

    fun onInitialize(): LiveData<ServiceResponse<Account>> = liveData

    fun onLogout(): LiveData<ServiceResponse<Unit>> = authLiveData

    fun initialize() {
        viewModelScope.launch(dispatcher.IO) {
            val response = execute { principalService.identity(false) }
            withContext(dispatcher.main) {
                liveData.postValue(response)
            }
        }
    }

    fun logout() {
        viewModelScope.launch(dispatcher.IO) {
            val response = execute { principalService.logout() }
            withContext(dispatcher.main) {
                authLiveData.postValue(response)
            }
        }
    }
}
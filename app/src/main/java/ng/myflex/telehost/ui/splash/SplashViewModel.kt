package ng.myflex.telehost.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ng.myflex.telehost.common.Dispatcher
import ng.myflex.telehost.config.Constant
import ng.myflex.telehost.domain.Account
import ng.myflex.telehost.service.PrincipalService
import ng.myflex.telehost.service.SessionStorageService
import ng.myflex.telehost.util.ServiceResponse
import ng.myflex.telehost.util.execute
import javax.inject.Inject

class SplashViewModel @Inject constructor(
    private val dispatcher: Dispatcher,
    private val principalService: PrincipalService,
    private val sessionStorageService: SessionStorageService
) : ViewModel() {
    private val liveData: MutableLiveData<ServiceResponse<Account>> = MutableLiveData()

    fun onInitialize(): LiveData<ServiceResponse<Account>> = liveData

    fun updatePermission(permission: String, rationale: Boolean) {
        sessionStorageService.saveBoolean(permission, rationale)
    }

    fun initialize() {
        sessionStorageService.remove(Constant.currentFragment)
        viewModelScope.launch(dispatcher.IO) {
            val response = execute { principalService.identity(true) }
            withContext(dispatcher.main) {
                liveData.postValue(response)
            }
        }
    }
}
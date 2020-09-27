package ng.myflex.telehost.ui.account.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ng.myflex.telehost.common.Dispatcher
import ng.myflex.telehost.form.LoginForm
import ng.myflex.telehost.domain.Account
import ng.myflex.telehost.service.AuthenticationService
import ng.myflex.telehost.service.PrincipalService
import ng.myflex.telehost.util.ServiceResponse
import ng.myflex.telehost.util.execute
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val dispatcher: Dispatcher,
    private val authenticationService: AuthenticationService,
    private val principalService: PrincipalService
) : ViewModel() {
    private val liveData: MutableLiveData<ServiceResponse<Account>> = MutableLiveData()

    val form: LoginForm = LoginForm()

    fun onResponse(): LiveData<ServiceResponse<Account>> = liveData

    fun onSubmit() = viewModelScope.launch(dispatcher.IO) {
        var response = execute {
            authenticationService.login(
                form.email.value!!,
                form.password.value!!
            )
        }
        when (response) {
            is ServiceResponse.OnSuccess -> {
                response = execute {
                    principalService.identity(true)
                }
            }
        }
        withContext(dispatcher.main) {
            liveData.postValue(response)
        }
    }
}
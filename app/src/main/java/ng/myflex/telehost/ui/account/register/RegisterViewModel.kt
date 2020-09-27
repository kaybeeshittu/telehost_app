package ng.myflex.telehost.ui.account.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ng.myflex.telehost.common.Dispatcher
import ng.myflex.telehost.form.AccountForm
import ng.myflex.telehost.domain.Account
import ng.myflex.telehost.service.AccountService
import ng.myflex.telehost.util.ServiceResponse
import ng.myflex.telehost.util.execute
import javax.inject.Inject

class RegisterViewModel @Inject constructor(
    private val dispatcher: Dispatcher,
    private val accountService: AccountService,
    private val phoneNumber: PhoneNumberUtil
) : ViewModel() {
    private val liveData: MutableLiveData<ServiceResponse<Account>> = MutableLiveData()

    val form: AccountForm = AccountForm()

    fun onResponse(): LiveData<ServiceResponse<Account>> = liveData

    fun onSubmit() {
        val field = form.phoneField
        val phone = phoneNumber.parse(field.phone.value, field.country.value?.phoneCode)
        if (!phoneNumber.isValidNumber(phone)) {
            liveData.postValue(ServiceResponse.OnError(Throwable("Invalid phone number provided")))

            return
        }
        viewModelScope.launch(dispatcher.IO) {
            val payload = form.getPayload()
            val response = execute { accountService.createAccount(payload) }

            withContext(dispatcher.main) {
                liveData.postValue(response)
            }
        }
    }

}
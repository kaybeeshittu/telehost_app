package ng.myflex.telehost.form

import androidx.lifecycle.MutableLiveData
import com.hbb20.CCPCountry
import ng.myflex.telehost.form.field.PhoneField
import ng.myflex.telehost.payload.UserPayload

class AccountForm : Form() {
    val firstName = MutableLiveData<String>()

    val lastName = MutableLiveData<String>()

    val email = MutableLiveData<String>()

    val password = MutableLiveData<String>()

    val phoneField = PhoneField()

    private val phone: MutableLiveData<String> get() = phoneField.phone

    private val country: MutableLiveData<CCPCountry> get() = phoneField.country

    fun getPayload(): UserPayload = UserPayload(
        firstName.value!!,
        lastName.value!!,
        email.value!!,
        password.value!!,
        phone.value!!,
        country.value!!.name
    )
}
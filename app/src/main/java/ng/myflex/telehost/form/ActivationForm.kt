package ng.myflex.telehost.form

import androidx.lifecycle.MutableLiveData
import com.hbb20.CCPCountry
import ng.myflex.telehost.form.field.PhoneField

class ActivationForm : Form() {
    val simPort = MutableLiveData<String>()

    val duration = MutableLiveData<String>()

    val phoneField = PhoneField()

    private val phone: MutableLiveData<String> get() = phoneField.phone

    private val country: MutableLiveData<CCPCountry> get() = phoneField.country
}
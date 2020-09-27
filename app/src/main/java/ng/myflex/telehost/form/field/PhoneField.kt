package ng.myflex.telehost.form.field

import androidx.lifecycle.MutableLiveData
import com.hbb20.CCPCountry

class PhoneField {
    val country = MutableLiveData<CCPCountry>()

    val phone = MutableLiveData<String>()

    fun onCountrySelected(selectedCountry: Any): Any? {
        val ccpCountry = selectedCountry as CCPCountry
        country.value = ccpCountry

        return null
    }
}
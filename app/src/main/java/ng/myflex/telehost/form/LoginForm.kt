package ng.myflex.telehost.form

import androidx.lifecycle.MutableLiveData

class LoginForm : Form() {
    val email = MutableLiveData<String>()

    val password = MutableLiveData<String>()
}
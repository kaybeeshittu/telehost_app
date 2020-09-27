package ng.myflex.telehost.ui.account.login

import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.basgeekball.awesomevalidation.ValidationStyle
import kotlinx.android.synthetic.main.form_login.*
import kotlinx.android.synthetic.main.fragment_login.*
import ng.myflex.telehost.R
import ng.myflex.telehost.common.content.ApplicationFragment
import ng.myflex.telehost.databinding.FragmentLoginBinding
import ng.myflex.telehost.domain.Account
import ng.myflex.telehost.util.ServiceResponse
import ng.myflex.telehost.util.Tools
import retrofit2.HttpException
import javax.inject.Inject

class LoginFragment : ApplicationFragment() {
    @Inject
    internal lateinit var factory: ViewModelProvider.Factory

    lateinit var viewModel: LoginViewModel

    lateinit var binding: FragmentLoginBinding

    private val validation = AwesomeValidation(ValidationStyle.BASIC)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewModel = ViewModelProviders.of(this, factory).get(LoginViewModel::class.java)
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_login, container, false
        )
        binding.apply {
            loading = false
            viewModel = this@LoginFragment.viewModel
            validation = this@LoginFragment.validation
            lifecycleOwner = this@LoginFragment
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.onResponse().observe(this, Observer { onLogin(it) })

        initComponent()

        Tools.setSystemBarColor(activity!!, android.R.color.white)
        Tools.setSystemBarLight(activity!!)
    }

    private fun initComponent() {
        show_pass.setOnClickListener {
            it.isActivated = !it.isActivated
            if (it.isActivated) {
                et_password.transformationMethod = null
            } else {
                et_password.transformationMethod = PasswordTransformationMethod()
            }
            et_password.setSelection(et_password.text.length)
        }
        register.setOnClickListener {
            controller.navigate(R.id.action_loginFragment_to_registerFragment)
        }
        btn_login.setOnClickListener {
            if (validation.validate()) {
                showLoading(true)
                viewModel.onSubmit()
            }
        }
    }

    private fun showLoading(show: Boolean) {
        binding.loading = show

        btn_login.visibility = if (show) View.INVISIBLE else View.VISIBLE
        progress_bar.visibility = if (!show) View.INVISIBLE else View.VISIBLE
    }

    private fun showError(message: String) {
        binding.error = message
    }

    private fun onLogin(response: ServiceResponse<Account>) {
        showLoading(false)
        when (response) {
            is ServiceResponse.OnSuccess -> {
                controller.navigate(R.id.action_loginFragment_to_homeFragment)
            }
            is ServiceResponse.OnError -> {
                if (response.error is HttpException && response.error.code() == 403) {
                    showError("Invalid email or password provided!")
                } else {
                    showError(response.error.message!!)
                }
            }
        }
    }
}

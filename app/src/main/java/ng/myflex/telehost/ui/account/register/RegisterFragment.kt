package ng.myflex.telehost.ui.account.register

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
import kotlinx.android.synthetic.main.form_register.*
import kotlinx.android.synthetic.main.fragment_register.*

import ng.myflex.telehost.R
import ng.myflex.telehost.common.content.ApplicationFragment
import ng.myflex.telehost.databinding.FragmentRegisterBinding
import ng.myflex.telehost.domain.Account
import ng.myflex.telehost.util.ServiceResponse
import ng.myflex.telehost.util.Tools
import ng.myflex.telehost.util.displayError
import retrofit2.HttpException
import javax.inject.Inject

class RegisterFragment : ApplicationFragment() {
    @Inject
    internal lateinit var factory: ViewModelProvider.Factory

    lateinit var viewModel: RegisterViewModel

    lateinit var binding: FragmentRegisterBinding

    private val validation = AwesomeValidation(ValidationStyle.BASIC)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_register, container, false
        )
        viewModel = ViewModelProviders.of(this, factory).get(
            RegisterViewModel::class.java
        )
        binding.apply {
            loading = false
            viewModel = this@RegisterFragment.viewModel
            validation = this@RegisterFragment.validation
            lifecycleOwner = this@RegisterFragment
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.onResponse().observe(this, Observer { onRegister(it) })

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
        login.setOnClickListener {
            controller.navigate(R.id.action_registerFragment_to_loginFragment)
        }
        btn_register.setOnClickListener {
            if (validation.validate()) {
                showLoading(true)
                viewModel.onSubmit()
            }
        }
    }

    private fun showLoading(show: Boolean) {
        binding.loading = show

        btn_register.visibility = if (show) View.INVISIBLE else View.VISIBLE
        progress_bar.visibility = if (!show) View.INVISIBLE else View.VISIBLE
    }

    private fun showError(message: String) {
        binding.error = message
    }

    private fun onRegister(response: ServiceResponse<Account>) {
        showLoading(false)
        when (response) {
            is ServiceResponse.OnSuccess -> {
                controller.navigate(R.id.action_registerFragment_to_loginFragment)
            }
            is ServiceResponse.OnError -> {
                if (response.error is HttpException && response.error.code() == 500) {
                    showError("User already exist!")
                } else {
                    showError(response.error.message!!)
                }
            }
        }
    }
}

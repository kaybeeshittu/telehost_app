package ng.myflex.telehost.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.romellfudi.ussdlibrary.USSDController

import ng.myflex.telehost.R
import ng.myflex.telehost.common.content.TitledFragment
import ng.myflex.telehost.databinding.FragmentProfileBinding
import ng.myflex.telehost.domain.Account
import ng.myflex.telehost.util.ServiceResponse
import javax.inject.Inject

class ProfileFragment : TitledFragment() {
    @Inject
    internal lateinit var factory: ViewModelProvider.Factory

    private lateinit var viewModel: ProfileViewModel

    private lateinit var binding: FragmentProfileBinding

    private var isVerifyAccesibilityAccess: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewModel = ViewModelProviders.of(this, factory).get(ProfileViewModel::class.java)
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_profile, container, false
        )

        initializeComponent()

        viewModel.onInitialize().observe(this, Observer { onInitialized(it) })
        viewModel.onLogout().observe(this, Observer { onLoggedOut(it) })
        viewModel.initialize()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        if (isVerifyAccesibilityAccess) {
            try {
                controller.navigate(R.id.action_homeFragment_to_activateFragment)
            } catch (ex: Exception) {
            }
        }
        isVerifyAccesibilityAccess = false
    }

    private fun initializeComponent() {
        binding.loginLogout.setOnClickListener {
            viewModel.logout()
        }
        binding.activateDevice.setOnClickListener {
            isVerifyAccesibilityAccess = true
            if (USSDController.verifyAccesibilityAccess(requireActivity())) {
                controller.navigate(R.id.action_homeFragment_to_activateFragment)
            }
        }
    }

    override fun getTitle(): Int = R.string.menu_profile

    private fun onLoggedOut(response: ServiceResponse<Unit>) {
        when (response) {
            is ServiceResponse.OnSuccess -> {
                controller.navigate(R.id.action_homeFragment_to_loginFragment)
            }
            is ServiceResponse.OnError -> {
            }
        }
    }

    private fun onInitialized(response: ServiceResponse<Account>) {
        when (response) {
            is ServiceResponse.OnSuccess -> {
                binding.apply {
                    account = response.data
                }
            }
            is ServiceResponse.OnError -> {
            }
        }
    }
}

package ng.myflex.telehost.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

import ng.myflex.telehost.R
import ng.myflex.telehost.common.Properties
import ng.myflex.telehost.common.content.ApplicationFragment
import ng.myflex.telehost.domain.Account
import ng.myflex.telehost.util.PermissionUtil
import ng.myflex.telehost.util.ServiceResponse
import ng.myflex.telehost.util.Tools
import javax.inject.Inject

class SplashFragment : ApplicationFragment() {
    @Inject
    internal lateinit var factory: ViewModelProvider.Factory

    @Inject
    internal lateinit var properties: Properties

    private lateinit var viewModel: SplashViewModel

    private var initializedPermission: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, factory).get(SplashViewModel::class.java)
        viewModel.onInitialize().observe(this, Observer {
            onInitialized(it)
        })
        Tools.setSmartSystemBar(activity)
    }

    private fun onInitialized(response: ServiceResponse<Account>) {
        when (response) {
            is ServiceResponse.OnSuccess -> {
                controller.navigate(R.id.action_splashFragment_to_homeFragment)
            }
            is ServiceResponse.OnError -> {
                controller.navigate(R.id.action_splashFragment_to_loginFragment)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (Tools.needRequestPermission() && !initializedPermission) {
            requestPermissions()
        } else if (hasDeniedPermissions() && initializedPermission) {
            // TODO: navigate to request permission error page
            activity?.finish()
        } else {
            onReady()
        }
    }

    private fun requestPermissions() {
        val permissions = PermissionUtil.getDeniedPermissions(activity)
        if (hasDeniedPermissions()) {
            requestPermissions(permissions, 200)
        } else {
            onReady()
        }
    }

    private fun hasDeniedPermissions(): Boolean {
        return PermissionUtil.getDeniedPermissions(activity).isNotEmpty()
    }

    private fun onReady() {
        viewModel.initialize()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 200) {
            for (permission in permissions) {
                val rationale = shouldShowRequestPermissionRationale(permission)
                viewModel.updatePermission(permission, rationale)
            }
            initializedPermission = true
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }
}

package ng.myflex.telehost.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener

import ng.myflex.telehost.R
import ng.myflex.telehost.common.content.TitledFragment
import ng.myflex.telehost.databinding.FragmentDashboardBinding
import ng.myflex.telehost.domain.Account
import ng.myflex.telehost.util.ServiceResponse
import javax.inject.Inject

class DashboardFragment : TitledFragment() {
    @Inject
    internal lateinit var factory: ViewModelProvider.Factory

    lateinit var binding: FragmentDashboardBinding

    lateinit var viewModel: DashboardViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewModel = ViewModelProviders.of(this, factory).get(DashboardViewModel::class.java)
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_dashboard, container, false
        )

        initComponent()

        binding.apply {
            viewModel = this@DashboardFragment.viewModel
        }
        viewModel.onResponse().observe(this, Observer { onInitialized(it) })
        viewModel.initialize()

        return binding.root
    }

    private fun initComponent() {
        showLoader(true)
        binding.swipeRefresh.setOnRefreshListener(OnRefreshListener {
            viewModel.initialize(true)
        })
    }

    private fun showLoader(show: Boolean) {
        binding.swipeRefresh.post { binding.swipeRefresh.isRefreshing = show }
    }

    private fun onInitialized(response: ServiceResponse<Account>) {
        showLoader(false)
        when (response) {
            is ServiceResponse.OnSuccess -> {
                binding.apply {
                    account = response.data
                    viewModel = this@DashboardFragment.viewModel
                }
            }
            is ServiceResponse.OnError -> {
            }
        }
    }

    override fun getTitle(): Int = R.string.app_name
}

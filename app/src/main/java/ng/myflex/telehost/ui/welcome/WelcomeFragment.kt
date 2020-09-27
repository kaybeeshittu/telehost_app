package ng.myflex.telehost.ui.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_welcome.*

import ng.myflex.telehost.R
import ng.myflex.telehost.common.content.ApplicationFragment
import ng.myflex.telehost.util.Tools
import javax.inject.Inject

class WelcomeFragment : ApplicationFragment() {
    @Inject
    internal lateinit var factory: ViewModelProvider.Factory

    lateinit var viewModel: WelcomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_welcome, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this, factory).get(WelcomeViewModel::class.java)

        Tools.setSystemBarColor(activity!!, android.R.color.white)
        Tools.setSystemBarLight(activity!!)
        btn_skip.setOnClickListener {
            controller.navigate(R.id.action_welcomeFragment_to_loginFragment)
        }
    }
}

package ng.myflex.telehost.ui.sms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_sms.*

import ng.myflex.telehost.R
import ng.myflex.telehost.common.content.TitledFragment
import javax.inject.Inject

class SmsFragment : TitledFragment() {

    @Inject
    internal lateinit var factory: ViewModelProvider.Factory

    lateinit var viewModel: SmsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sms, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this, factory).get(SmsViewModel::class.java)
    }

    override fun getTitle(): Int = R.string.menu_sms
}

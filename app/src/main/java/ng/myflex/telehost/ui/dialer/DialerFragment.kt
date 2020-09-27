package ng.myflex.telehost.ui.dialer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import ng.myflex.telehost.R
import ng.myflex.telehost.common.content.TitledFragment

class DialerFragment : TitledFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dialer, container, false)
    }
    
    override fun getTitle(): Int = R.string.menu_dialer
}

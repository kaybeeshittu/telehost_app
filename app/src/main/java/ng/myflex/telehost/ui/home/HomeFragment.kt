package ng.myflex.telehost.ui.home

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.toolbar.*
import ng.myflex.telehost.R
import ng.myflex.telehost.common.content.ApplicationFragment
import ng.myflex.telehost.common.content.TitledFragment
import ng.myflex.telehost.databinding.FragmentHomeBinding
import ng.myflex.telehost.ui.dashboard.DashboardFragment
import ng.myflex.telehost.ui.dialer.DialerFragment
import ng.myflex.telehost.ui.profile.ProfileFragment
import ng.myflex.telehost.ui.sms.SmsFragment
import ng.myflex.telehost.util.Tools
import javax.inject.Inject

class HomeFragment : ApplicationFragment(), DrawerSelectListener {
    @Inject
    internal lateinit var factory: ViewModelProvider.Factory

    lateinit var viewModel: HomeViewModel

    lateinit var bind: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewModel = ViewModelProviders.of(this, factory).get(HomeViewModel::class.java)
        bind = DataBindingUtil.inflate(
            inflater, R.layout.fragment_home, container, false
        )
        bind.apply {
            view = this@HomeFragment
            viewModel = this@HomeFragment.viewModel
        }
        return bind.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initToolbar()
        initDrawerMenu(savedInstanceState)
    }

    private fun initToolbar() {
        val compactActivity = (activity as AppCompatActivity)
        toolbar.setNavigationIcon(R.drawable.ic_menu)
        toolbar.navigationIcon?.setColorFilter(
            resources.getColor(R.color.colorTextAction),
            PorterDuff.Mode.SRC_ATOP
        )
        compactActivity.setSupportActionBar(toolbar)
        compactActivity.supportActionBar?.apply {
            setTitle(R.string.app_name)
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
        }
        Tools.changeOverflowMenuIconColor(toolbar, resources.getColor(R.color.colorTextAction))
        Tools.setSmartSystemBar(compactActivity)
    }

    private fun initDrawerMenu(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            val view = View(context)
            val fragmentId = viewModel.currentFragmentId()

            view.id = if (fragmentId == 0) {
                R.id.nav_menu_home
            } else fragmentId

            onSelect(view)
        }
    }

    override fun onSelect(view: View) {
        when (view.id) {
            R.id.nav_menu_home -> displayFragment(DashboardFragment())
            R.id.nav_menu_account -> displayFragment(ProfileFragment())
//            R.id.nav_menu_sms -> displayFragment(SmsFragment())
            R.id.nav_menu_profile -> displayFragment(ProfileFragment())
        }
        viewModel.pageState(view.id)
        drawer.closeDrawer(GravityCompat.START)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val menuId = item.itemId
        if (menuId == android.R.id.home) {
            if (!drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.openDrawer(GravityCompat.START)
            } else {
                drawer.openDrawer(GravityCompat.END)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun displayFragment(fragment: TitledFragment) {
        (activity as AppCompatActivity).supportActionBar?.apply {
            setTitle(fragment.getTitle())
        }
        fragmentManager?.beginTransaction()?.replace(
            R.id.frame_container,
            fragment
        )?.commit()
    }
}

package ng.myflex.telehost.common.content

import android.os.Bundle
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.Navigation
import dagger.android.support.DaggerFragment
import ng.myflex.telehost.R

abstract class ApplicationFragment : DaggerFragment() {
    private lateinit var navController: NavController

    protected val controller: NavController get() = navController

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        navController = Navigation.findNavController(activity!!, R.id.host_fragment)

        setHasOptionsMenu(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home && !isHome()) {
            activity?.onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun isHome(): Boolean {
        return fragmentManager!!.backStackEntryCount == 0
    }
}
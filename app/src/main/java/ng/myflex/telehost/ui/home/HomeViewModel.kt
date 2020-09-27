package ng.myflex.telehost.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ng.myflex.telehost.config.Constant
import ng.myflex.telehost.domain.Account
import ng.myflex.telehost.service.PrincipalService
import ng.myflex.telehost.service.SessionStorageService
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val principalService: PrincipalService,
    private val sessionStorageService: SessionStorageService
) : ViewModel() {
    val account: LiveData<Account?> = principalService.getStream()

    fun pageState(resId: Int) {
        sessionStorageService.saveInt(Constant.currentFragment, resId)
    }

    fun currentFragmentId(): Int {
        return sessionStorageService.getInt(Constant.currentFragment)
    }
}
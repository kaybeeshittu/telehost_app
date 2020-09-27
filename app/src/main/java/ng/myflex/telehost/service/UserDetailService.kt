package ng.myflex.telehost.service

import ng.myflex.telehost.config.Constant
import ng.myflex.telehost.domain.Account
import ng.myflex.telehost.model.AccountModel
import ng.myflex.telehost.repository.AccountRepository
import ng.myflex.telehost.repository.DeviceRepository
import ng.myflex.telehost.util.toAccount
import ng.myflex.telehost.util.toDevices
import javax.inject.Inject

class UserDetailService @Inject constructor(
    private val accountRepository: AccountRepository,
    private val deviceRepository: DeviceRepository,
    private val sessionStorageService: SessionStorageService
) {
    suspend fun get(): Account? = accountRepository.get()

    suspend fun getKey(): String = accountRepository.get().key

    suspend fun setAuthenticatedUser(model: AccountModel): Account {
        val account = model.toAccount()
        val devices = model.toDevices()

        accountRepository.clear()
        accountRepository.save(account)

        deviceRepository.clear()
        deviceRepository.saveAll(devices)

        return account
    }

    suspend fun clear() {
        accountRepository.clear()
        deviceRepository.clear()

        sessionStorageService.remove(Constant.currentFragment)
        sessionStorageService.remove(Constant.authSession)
    }
}
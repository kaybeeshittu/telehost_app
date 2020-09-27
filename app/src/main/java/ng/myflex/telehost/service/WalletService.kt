package ng.myflex.telehost.service

import ng.myflex.telehost.resource.WalletResource
import javax.inject.Inject

class WalletService @Inject constructor(private val walletResource: WalletResource) {
    suspend fun get(refresh: Boolean = false) {
        if (!refresh) {

        }
        val wallet = walletResource.get()
    }
}
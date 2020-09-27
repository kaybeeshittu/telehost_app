package ng.myflex.telehost.util

import ng.myflex.telehost.model.AccountModel
import ng.myflex.telehost.model.ProfileModel
import ng.myflex.telehost.model.WalletModel
import ng.myflex.telehost.resource.AccountResource
import org.mockito.Mockito
import java.util.*

class AccountUtil {
    companion object {
        const val firstName: String = "AAAAAAAA"
        const val lastName: String = "AAAAAAAA"
        const val email: String = "AAAAAAAA"
        const val country: String = "AAAAAAAA"
        const val apiKey: String = "AAAAAAAA"
        const val webHook: String = "AAAAAAAA"

        fun generateModel(): AccountModel {
            val count = 0.0
            val wallet = WalletModel(
                Random().nextLong(),
                Random().nextDouble(),
                Random().nextDouble().toString(),
                Date().toString(),
                Date().toString()
            )
            val profile = ProfileModel(
                Random().nextLong(),
                apiKey,
                webHook,
                firstName,
                lastName,
                email,
                Random().nextDouble().toString(),
                country,
                null,
                0,
                1,
                Date().toString(),
                Date().toString()
            )
            return AccountModel(profile, wallet, arrayListOf(), count, count, count)
        }

        suspend fun mockAccount(accountResource: AccountResource) {
            Mockito.`when`(accountResource.getProfile()).thenReturn(generateModel())
        }
    }
}
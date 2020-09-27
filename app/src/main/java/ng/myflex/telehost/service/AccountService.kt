package ng.myflex.telehost.service

import ng.myflex.telehost.domain.Account
import ng.myflex.telehost.model.AccountModel
import ng.myflex.telehost.payload.UserPayload
import ng.myflex.telehost.resource.AccountResource
import ng.myflex.telehost.util.toAccount
import javax.inject.Inject

class AccountService @Inject constructor(
    private val accountResource: AccountResource
) {
    suspend fun get(): AccountModel {
        return accountResource.getProfile()
    }

    suspend fun createAccount(payload: UserPayload): Account {
        val account = accountResource.create(payload)

        return account.toAccount()
    }
}
package ng.myflex.telehost.service

import ng.myflex.telehost.domain.Account
import ng.myflex.telehost.payload.LoginPayload
import ng.myflex.telehost.resource.AuthenticationResource
import ng.myflex.telehost.util.toAccount
import javax.inject.Inject

class AuthenticationService @Inject constructor(
    private val authenticationResource: AuthenticationResource,
    private val principalService: PrincipalService
) {
    suspend fun login(email: String, password: String): Account {
        val payload = LoginPayload(email, password)

        val model = authenticationResource.login(payload)
        val account = model.user.toAccount()

        principalService.authenticate(account)

        return account
    }
}
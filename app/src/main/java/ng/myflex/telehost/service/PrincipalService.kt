package ng.myflex.telehost.service

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ng.myflex.telehost.domain.Account
import ng.myflex.telehost.util.ServiceResponse
import ng.myflex.telehost.util.execute
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PrincipalService @Inject constructor(
    private val accountService: AccountService,
    private val userDetailService: UserDetailService
) {
    private var identity: Account? = null
    private var authenticated: Boolean = false

    private var authenticationListener: MutableLiveData<Account?> = MutableLiveData()

    fun authenticate(account: Account?) {
        identity = account
        authenticated = account != null

        authenticationListener.postValue(identity)
    }

    suspend fun identity(refresh: Boolean = false): Account {
        if (refresh) {
            identity = null
        }
        if (identity != null) {
            return identity as Account
        }
        return when (val response = execute { accountService.get() }) {
            is ServiceResponse.OnSuccess -> {
                val account = userDetailService.setAuthenticatedUser(response.data)

                authenticate(account)

                account
            }
            is ServiceResponse.OnError -> {
                if (response.error is HttpException
                    && response.error.code() == 401
                ) {
                    authenticate(null)

                    throw response.error
                }
                val account = userDetailService.get() ?: throw response.error

                authenticate(account)

                account
            }
        }
    }

    fun isAuthenticated(): Boolean = authenticated

    fun getStream(): LiveData<Account?> = authenticationListener

    suspend fun logout() {
        userDetailService.clear()

        authenticate(null)
    }
}
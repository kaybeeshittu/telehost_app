package ng.myflex.telehost.resource

import ng.myflex.telehost.model.AccountModel
import ng.myflex.telehost.model.DashboardModel
import ng.myflex.telehost.model.UserModel
import ng.myflex.telehost.payload.UserPayload
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AccountResource {
    @GET("auth/profile")
    suspend fun getProfile(): AccountModel

    @GET("dashboard")
    suspend fun getDashboard(): DashboardModel

    @POST("auth/signup")
    suspend fun create(@Body credentials: UserPayload): UserModel
}
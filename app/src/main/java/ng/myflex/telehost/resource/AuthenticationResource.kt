package ng.myflex.telehost.resource

import ng.myflex.telehost.model.LoginModel
import ng.myflex.telehost.payload.LoginPayload
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthenticationResource {
    @POST("auth/login")
    suspend fun login(@Body credentials: LoginPayload): LoginModel
}
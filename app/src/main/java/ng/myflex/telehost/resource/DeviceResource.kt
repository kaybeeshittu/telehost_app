package ng.myflex.telehost.resource

import ng.myflex.telehost.model.AccountModel
import ng.myflex.telehost.model.ResponseModel
import ng.myflex.telehost.payload.ActivationPayload
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface DeviceResource {
    @POST("activate-device")
    suspend fun activateDevice(
        @Header("Authorization") key: String,
        @Body payload: ActivationPayload
    ): ResponseModel<Any?>

    @GET("auth/profile")
    suspend fun getDevices(): AccountModel
}
package ng.myflex.telehost.resource

import ng.myflex.telehost.model.BaseResponseModel
import ng.myflex.telehost.model.ResponseModel
import ng.myflex.telehost.payload.StatusPayload
import ng.myflex.telehost.payload.UssdEventPayload
import ng.myflex.telehost.payload.UssdPayload
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface UssdResource {
    @POST("post-ussd")
    suspend fun schedule(
        @Header("Authorization") key: String,
        @Body payload: UssdPayload
    ): BaseResponseModel<Any?, Any?>

    @POST("send-ussd-response")
    suspend fun update(
        @Header("Authorization") key: String,
        @Body payload: StatusPayload
    ): ResponseModel<Any?>

    @POST("bulkstatus/ussd")
    suspend fun updateAll(
        @Header("Authorization") key: String,
        @Body payload: List<StatusPayload>
    ): ResponseModel<Any?>

    @POST("schedule-ussd-response")
    suspend fun dispatch(@Body payload: UssdEventPayload): Any
}
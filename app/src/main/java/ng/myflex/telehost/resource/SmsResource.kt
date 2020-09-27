package ng.myflex.telehost.resource

import ng.myflex.telehost.model.ResponseModel
import ng.myflex.telehost.payload.SmsBackupPayload
import ng.myflex.telehost.payload.SmsPayload
import ng.myflex.telehost.payload.SmsEventPayload
import ng.myflex.telehost.payload.StatusPayload
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface SmsResource {
    @POST("post-sms")
    suspend fun schedule(
        @Header("Authorization") key: String,
        @Body payload: SmsPayload
    ): ResponseModel<Any>

    @POST("status/sms")
    suspend fun update(
        @Header("Authorization") key: String,
        @Body payload: StatusPayload
    ): Any

    @POST("bulkstatus/sms")
    suspend fun updateAll(
        @Header("Authorization") key: String,
        @Body payload: List<StatusPayload>
    ): ResponseModel<Any?>

    @POST("schedule-sms-response")
    suspend fun dispatch(@Body payload: SmsEventPayload): Any

    @POST("bulk/sms")
    suspend fun backup(
        @Header("Authorization") key: String,
        @Body payload: List<SmsBackupPayload>
    ): ResponseModel<Any?>
}
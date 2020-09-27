package ng.myflex.telehost.resource

import ng.myflex.telehost.model.AirtimeModel
import ng.myflex.telehost.model.WalletModel
import ng.myflex.telehost.payload.AirtimePayload
import retrofit2.http.*

interface WalletResource {
    @GET("wallet")
    suspend fun get(): WalletModel

    @FormUrlEncoded
    @POST("buy-airtime-app")
    suspend fun purchaseAirtime(@Body payload: AirtimePayload): AirtimeModel
}
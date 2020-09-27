package ng.myflex.telehost.resource

import ng.myflex.telehost.model.CountriesModel
import ng.myflex.telehost.model.ProvidersModel
import retrofit2.http.GET

interface ConfigResource {
    @GET("get-networks")
    suspend fun getNetworks(): ProvidersModel
    
    @GET("auth/countries")
    suspend fun getCountries(): CountriesModel
}
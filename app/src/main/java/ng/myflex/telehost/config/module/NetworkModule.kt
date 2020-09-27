package ng.myflex.telehost.config.module

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import ng.myflex.telehost.common.Properties
import ng.myflex.telehost.config.Constant.Companion.connectionTimeout
import ng.myflex.telehost.config.interceptor.ErrorInterceptor
import ng.myflex.telehost.config.interceptor.JwtInterceptor
import ng.myflex.telehost.service.SessionStorageService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkModule {
    @Singleton
    @Provides
    fun providesLoggingInterceptor(): HttpLoggingInterceptor {
        val loggerInterceptor = HttpLoggingInterceptor()
        loggerInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return loggerInterceptor
    }

    @Provides
    @Singleton
    fun providesErrorInterceptor(
        storage: SessionStorageService
    ): ErrorInterceptor = ErrorInterceptor(storage)

    @Provides
    @Singleton
    fun providesJwtInterceptor(storage: SessionStorageService, gson: Gson): JwtInterceptor {
        return JwtInterceptor(storage, gson)
    }

    @Provides
    @Singleton
    fun providesOkHttpClient(
        interceptor: HttpLoggingInterceptor,
        errorInterceptor: ErrorInterceptor,
        jwtInterceptor: JwtInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(errorInterceptor)
            .addInterceptor(jwtInterceptor)
            .connectTimeout(connectionTimeout, TimeUnit.SECONDS)
            .readTimeout(connectionTimeout, TimeUnit.SECONDS)
            .writeTimeout(connectionTimeout, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun providesRetrofit(gson: Gson, httpClient: OkHttpClient, props: Properties): Retrofit {
        return Retrofit.Builder()
            .baseUrl(props.getBaseUrl())
            .client(httpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
}
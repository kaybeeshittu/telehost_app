package ng.myflex.telehost.config.module

import dagger.Module
import dagger.Provides
import ng.myflex.telehost.resource.*
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class ResourceModule {
    @Singleton
    @Provides
    fun providesAccountResource(retrofit: Retrofit): AccountResource {
        return retrofit.create(AccountResource::class.java)
    }

    @Singleton
    @Provides
    fun providesAuthenticationResource(retrofit: Retrofit): AuthenticationResource {
        return retrofit.create(AuthenticationResource::class.java)
    }

    @Singleton
    @Provides
    fun providesConfigResource(retrofit: Retrofit): ConfigResource {
        return retrofit.create(ConfigResource::class.java)
    }

    @Singleton
    @Provides
    fun providesDeviceResource(retrofit: Retrofit): DeviceResource {
        return retrofit.create(DeviceResource::class.java)
    }

    @Singleton
    @Provides
    fun providesSmsResource(retrofit: Retrofit): SmsResource {
        return retrofit.create(SmsResource::class.java)
    }

    @Singleton
    @Provides
    fun providesUssdResource(retrofit: Retrofit): UssdResource {
        return retrofit.create(UssdResource::class.java)
    }

    @Singleton
    @Provides
    fun providesWalletResource(retrofit: Retrofit): WalletResource {
        return retrofit.create(WalletResource::class.java)
    }
}
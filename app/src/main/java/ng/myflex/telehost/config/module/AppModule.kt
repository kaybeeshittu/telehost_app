package ng.myflex.telehost.config.module

import android.content.Context
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import ng.myflex.telehost.common.Dispatcher
import ng.myflex.telehost.common.Properties
import javax.inject.Singleton

@Module
class AppModule {
    @Singleton
    @Provides
    fun providesProperties(context: Context): Properties = Properties(context)

    @Singleton
    @Provides
    fun providesGson(): Gson {
        return GsonBuilder().setLenient().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create()
    }

    @Singleton
    @Provides
    fun providesCoroutineContext(): Dispatcher = Dispatcher()

    @Singleton
    @Provides
    fun providesPhoneNumber(context: Context): PhoneNumberUtil {
        return PhoneNumberUtil.createInstance(context)
    }
}
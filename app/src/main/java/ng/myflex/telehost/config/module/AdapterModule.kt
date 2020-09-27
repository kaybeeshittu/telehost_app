package ng.myflex.telehost.config.module

import dagger.Binds
import dagger.Module
import ng.myflex.telehost.adapter.SmsAdapter
import ng.myflex.telehost.adapter.implemetation.SmsAdapterImplementation
import javax.inject.Singleton
import ng.myflex.telehost.adapter.UssdAdapter
import ng.myflex.telehost.adapter.implemetation.UssdAdapterImplementation


@Module
abstract class AdapterModule {
    @Binds
    @Singleton
    abstract fun bindSmsAdapter(smsAdapter: SmsAdapterImplementation): SmsAdapter

    @Binds
    @Singleton
    abstract fun bindUssdAdapter(ussdAdapter: UssdAdapterImplementation): UssdAdapter
}
package ng.myflex.telehost.config.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ng.myflex.telehost.service.MessageReceiverService
import ng.myflex.telehost.service.MessengerService


@Module
abstract class ServiceModule {
    @ContributesAndroidInjector
    abstract fun bindMessengerService(): MessengerService

    @ContributesAndroidInjector
    abstract fun bindMessageReceiverService(): MessageReceiverService
}
package ng.myflex.telehost.config.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ng.myflex.telehost.service.SmsReceiverService

@Module
abstract class BroadcastReceiverModule {
    @ContributesAndroidInjector
    abstract fun bindSmsReceiverService(): SmsReceiverService
}
package ng.myflex.telehost.ui.sms

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ng.myflex.telehost.common.scope.FragmentScope

@Module
abstract class SmsProvider {
    @FragmentScope
    @ContributesAndroidInjector(modules = [SmsModule::class])
    abstract fun providesFragment(): SmsFragment
}
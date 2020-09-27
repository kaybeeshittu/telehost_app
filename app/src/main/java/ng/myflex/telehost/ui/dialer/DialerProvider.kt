package ng.myflex.telehost.ui.dialer

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ng.myflex.telehost.common.scope.FragmentScope

@Module
abstract class DialerProvider {
    @FragmentScope
    @ContributesAndroidInjector(modules = [DialerModule::class])
    abstract fun providesFragment(): DialerFragment
}
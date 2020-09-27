package ng.myflex.telehost.ui.welcome

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ng.myflex.telehost.common.scope.FragmentScope

@Module
abstract class WelcomeProvider {
    @FragmentScope
    @ContributesAndroidInjector(modules = [WelcomeModule::class])
    abstract fun providesWelcomeFragment(): WelcomeFragment
}
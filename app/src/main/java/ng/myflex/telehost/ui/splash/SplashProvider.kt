package ng.myflex.telehost.ui.splash

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ng.myflex.telehost.common.scope.FragmentScope

@Module
abstract class SplashProvider {
    @FragmentScope
    @ContributesAndroidInjector(modules = [SplashModule::class])
    abstract fun providesSplashFragment(): SplashFragment
}
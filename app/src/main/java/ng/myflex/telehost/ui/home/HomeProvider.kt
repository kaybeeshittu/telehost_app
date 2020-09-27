package ng.myflex.telehost.ui.home

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ng.myflex.telehost.common.scope.FragmentScope

@Module
abstract class HomeProvider {
    @FragmentScope
    @ContributesAndroidInjector(modules = [HomeModule::class])
    abstract fun providesHomeFragment(): HomeFragment
}
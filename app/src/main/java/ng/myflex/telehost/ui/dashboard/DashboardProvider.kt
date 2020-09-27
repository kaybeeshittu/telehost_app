package ng.myflex.telehost.ui.dashboard

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ng.myflex.telehost.common.scope.FragmentScope

@Module
abstract class DashboardProvider {
    @FragmentScope
    @ContributesAndroidInjector(modules = [DashboardModule::class])
    abstract fun providesFragment(): DashboardFragment
}
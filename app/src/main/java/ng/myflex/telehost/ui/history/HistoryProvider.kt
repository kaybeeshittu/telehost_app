package ng.myflex.telehost.ui.history

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ng.myflex.telehost.common.scope.FragmentScope

@Module
abstract class HistoryProvider {
    @FragmentScope
    @ContributesAndroidInjector(modules = [HistoryModule::class])
    abstract fun providesFragment(): HistoryFragment
}
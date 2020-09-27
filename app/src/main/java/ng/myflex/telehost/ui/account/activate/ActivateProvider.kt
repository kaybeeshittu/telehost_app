package ng.myflex.telehost.ui.account.activate

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ng.myflex.telehost.common.scope.FragmentScope

@Module
abstract class ActivateProvider {
    @FragmentScope
    @ContributesAndroidInjector(modules = [ActivateModule::class])
    abstract fun providesActivateFragment(): ActivateFragment
}
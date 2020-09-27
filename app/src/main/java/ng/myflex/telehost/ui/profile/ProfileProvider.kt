package ng.myflex.telehost.ui.profile

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ng.myflex.telehost.common.scope.FragmentScope

@Module
abstract class ProfileProvider {
    @FragmentScope
    @ContributesAndroidInjector(modules = [ProfileModule::class])
    abstract fun providesFragment(): ProfileFragment
}
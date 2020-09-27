package ng.myflex.telehost.ui.account.login

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ng.myflex.telehost.common.scope.FragmentScope

@Module
abstract class LoginProvider {
    @FragmentScope
    @ContributesAndroidInjector(modules = [LoginModule::class])
    abstract fun providesLoginFragment(): LoginFragment
}
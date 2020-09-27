package ng.myflex.telehost.ui.account.register

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ng.myflex.telehost.common.scope.FragmentScope

@Module
abstract class RegisterProvider {
    @FragmentScope
    @ContributesAndroidInjector(modules = [RegisterModule::class])
    abstract fun providesRegisterFragment(): RegisterFragment
}
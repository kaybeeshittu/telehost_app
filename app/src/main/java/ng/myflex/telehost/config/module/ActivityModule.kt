package ng.myflex.telehost.config.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ng.myflex.telehost.common.scope.ActivityScope
import ng.myflex.telehost.ui.MainActivity
import ng.myflex.telehost.ui.MainModule

@Module
abstract class ActivityModule {
    @ActivityScope
    @ContributesAndroidInjector(
        modules = [
            MainModule::class
        ]
    )
    abstract fun bindMainActivity(): MainActivity
}
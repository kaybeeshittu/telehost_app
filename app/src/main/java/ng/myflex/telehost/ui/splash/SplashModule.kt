package ng.myflex.telehost.ui.splash

import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import ng.myflex.telehost.common.scope.FragmentScope
import ng.myflex.telehost.util.ViewModelUtil

@Module
class SplashModule {
    @Provides
    @FragmentScope
    fun providesSplashViewModel(viewModel: SplashViewModel): ViewModelProvider.Factory {
        return ViewModelUtil.create(viewModel)
    }
}
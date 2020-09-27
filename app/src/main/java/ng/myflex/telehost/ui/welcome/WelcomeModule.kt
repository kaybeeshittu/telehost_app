package ng.myflex.telehost.ui.welcome

import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import ng.myflex.telehost.common.scope.FragmentScope
import ng.myflex.telehost.util.ViewModelUtil

@Module
class WelcomeModule {
    @Provides
    @FragmentScope
    fun providesWelcomeViewModel(viewModel: WelcomeViewModel): ViewModelProvider.Factory {
        return ViewModelUtil.create(viewModel)
    }
}
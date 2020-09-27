package ng.myflex.telehost.ui.home

import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import ng.myflex.telehost.common.scope.FragmentScope
import ng.myflex.telehost.util.ViewModelUtil

@Module
class HomeModule {
    @Provides
    @FragmentScope
    fun providesHomeViewModel(viewModel: HomeViewModel): ViewModelProvider.Factory {
        return ViewModelUtil.create(viewModel)
    }
}
package ng.myflex.telehost.ui.profile

import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import ng.myflex.telehost.common.scope.FragmentScope
import ng.myflex.telehost.util.ViewModelUtil

@Module
class ProfileModule {
    @Provides
    @FragmentScope
    fun providesViewModel(viewModel: ProfileViewModel): ViewModelProvider.Factory {
        return ViewModelUtil.create(viewModel)
    }
}
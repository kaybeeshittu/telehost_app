package ng.myflex.telehost.ui.account.activate

import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import ng.myflex.telehost.common.scope.FragmentScope
import ng.myflex.telehost.util.ViewModelUtil

@Module
class ActivateModule {
    @Provides
    @FragmentScope
    fun providesActivateViewModel(viewModel: ActivateViewModel): ViewModelProvider.Factory {
        return ViewModelUtil.create(viewModel)
    }
}
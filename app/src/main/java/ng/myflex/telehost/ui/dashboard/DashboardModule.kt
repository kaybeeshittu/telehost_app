package ng.myflex.telehost.ui.dashboard

import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import ng.myflex.telehost.common.scope.FragmentScope
import ng.myflex.telehost.util.ViewModelUtil

@Module
class DashboardModule {
    @Provides
    @FragmentScope
    fun providesViewModel(viewModel: DashboardViewModel): ViewModelProvider.Factory {
        return ViewModelUtil.create(viewModel)
    }
}
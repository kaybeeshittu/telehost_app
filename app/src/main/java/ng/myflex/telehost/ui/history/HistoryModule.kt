package ng.myflex.telehost.ui.history

import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import ng.myflex.telehost.common.scope.FragmentScope
import ng.myflex.telehost.util.ViewModelUtil

@Module
class HistoryModule {
    @Provides
    @FragmentScope
    fun providesViewModel(viewModel: HistoryViewModel): ViewModelProvider.Factory {
        return ViewModelUtil.create(viewModel)
    }
}
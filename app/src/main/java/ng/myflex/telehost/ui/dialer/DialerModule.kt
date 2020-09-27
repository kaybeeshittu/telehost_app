package ng.myflex.telehost.ui.dialer

import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import ng.myflex.telehost.common.scope.FragmentScope
import ng.myflex.telehost.util.ViewModelUtil

@Module
class DialerModule {
    @Provides
    @FragmentScope
    fun providesViewModel(viewModel: DialerViewModel): ViewModelProvider.Factory {
        return ViewModelUtil.create(viewModel)
    }
}
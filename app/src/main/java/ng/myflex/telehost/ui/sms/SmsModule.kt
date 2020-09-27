package ng.myflex.telehost.ui.sms

import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import ng.myflex.telehost.common.scope.FragmentScope
import ng.myflex.telehost.util.ViewModelUtil

@Module
class SmsModule {
    @Provides
    @FragmentScope
    fun providesViewModel(viewModel: SmsViewModel): ViewModelProvider.Factory {
        return ViewModelUtil.create(viewModel)
    }
}
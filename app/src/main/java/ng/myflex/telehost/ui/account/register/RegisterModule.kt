package ng.myflex.telehost.ui.account.register

import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import ng.myflex.telehost.common.scope.FragmentScope
import ng.myflex.telehost.util.ViewModelUtil

@Module
class RegisterModule {
    @Provides
    @FragmentScope
    fun providesRegisterViewModel(viewModel: RegisterViewModel): ViewModelProvider.Factory {
        return ViewModelUtil.create(viewModel)
    }
}
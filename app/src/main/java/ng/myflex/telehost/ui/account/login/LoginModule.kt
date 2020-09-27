package ng.myflex.telehost.ui.account.login

import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import ng.myflex.telehost.common.scope.FragmentScope
import ng.myflex.telehost.util.ViewModelUtil

@Module
class LoginModule {
    @Provides
    @FragmentScope
    fun providesLoginViewModel(viewModel: LoginViewModel): ViewModelProvider.Factory {
        return ViewModelUtil.create(viewModel)
    }
}
package ng.myflex.telehost.ui.account

import dagger.Module
import ng.myflex.telehost.ui.account.activate.ActivateProvider
import ng.myflex.telehost.ui.account.login.LoginProvider
import ng.myflex.telehost.ui.account.register.RegisterProvider

@Module(
    includes = [
        ActivateProvider::class,
        LoginProvider::class,
        RegisterProvider::class
    ]
)
class AccountModule
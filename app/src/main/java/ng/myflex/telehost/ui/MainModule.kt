package ng.myflex.telehost.ui

import dagger.Module
import ng.myflex.telehost.ui.account.AccountModule
import ng.myflex.telehost.ui.dashboard.DashboardProvider
import ng.myflex.telehost.ui.dialer.DialerProvider
import ng.myflex.telehost.ui.history.HistoryProvider
import ng.myflex.telehost.ui.home.HomeProvider
import ng.myflex.telehost.ui.profile.ProfileProvider
import ng.myflex.telehost.ui.sms.SmsProvider
import ng.myflex.telehost.ui.splash.SplashProvider
import ng.myflex.telehost.ui.welcome.WelcomeProvider

@Module(
    includes = [
        SplashProvider::class,
        WelcomeProvider::class,
        HomeProvider::class,
        AccountModule::class,
        DashboardProvider::class,
        DialerProvider::class,
        HistoryProvider::class,
        ProfileProvider::class,
        SmsProvider::class
    ]
)
class MainModule
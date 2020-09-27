package ng.myflex.telehost

import android.app.Activity
import android.app.Application
import android.app.Service
import android.content.BroadcastReceiver
import dagger.android.*
import io.reactivex.plugins.RxJavaPlugins
import ng.myflex.telehost.config.DaggerComponent
import ng.myflex.telehost.service.BootstrapService
import javax.inject.Inject

class Telehost : Application(), HasActivityInjector, HasServiceInjector,
    HasBroadcastReceiverInjector {
    @Inject
    lateinit var injector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var serviceInjector: DispatchingAndroidInjector<Service>

    @Inject
    lateinit var broadcastInjector: DispatchingAndroidInjector<BroadcastReceiver>

    @Inject
    lateinit var bootstrapService: BootstrapService

    override fun onCreate() {
        super.onCreate()
        RxJavaPlugins.setErrorHandler { }
        DaggerComponent.builder().context(this).build().inject(this)

        bootstrapService.init()
    }

    override fun activityInjector(): AndroidInjector<Activity> = injector

    override fun serviceInjector(): AndroidInjector<Service> = serviceInjector

    override fun broadcastReceiverInjector(): AndroidInjector<BroadcastReceiver> {
        return broadcastInjector
    }
}
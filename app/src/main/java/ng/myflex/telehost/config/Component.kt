package ng.myflex.telehost.config

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import ng.myflex.telehost.Telehost
import ng.myflex.telehost.config.module.*
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        AppModule::class,
        NetworkModule::class,
        ResourceModule::class,
        DatabaseModule::class,
        ServiceModule::class,
        AssistedModule::class,
        AdapterModule::class,
        WorkerModule::class,
        BroadcastReceiverModule::class,
        ActivityModule::class
    ]
)
interface Component : AndroidInjector<Telehost> {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(context: Context): Builder

        fun build(): ng.myflex.telehost.config.Component
    }
}

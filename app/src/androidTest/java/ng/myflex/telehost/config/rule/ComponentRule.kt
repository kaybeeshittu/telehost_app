package ng.myflex.telehost.config.rule

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.Gson
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import ng.myflex.telehost.common.Properties
import ng.myflex.telehost.common.content.InjectableWorkerFactory
import ng.myflex.telehost.config.module.*
import ng.myflex.telehost.repository.*
import ng.myflex.telehost.resource.*
import ng.myflex.telehost.service.AccountService
import ng.myflex.telehost.service.AuthenticationService
import ng.myflex.telehost.service.PrincipalService
import ng.myflex.telehost.service.SessionStorageService
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import javax.inject.Inject
import javax.inject.Singleton

open class ComponentRule : TestRule {
    lateinit var context: Context

    lateinit var component: ComponentRuleTestComponent

    override fun apply(base: Statement?, description: Description?): Statement {
        return object : Statement() {
            override fun evaluate() {
                initialize()

                base?.evaluate()
            }
        }
    }

    protected fun initialize() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        component = DaggerComponentRule_ComponentRuleTestComponent
            .builder()
            .context(context)
            .build()

        component.inject(this@ComponentRule)
    }

    @Singleton
    @Component(
        modules = [
            AndroidInjectionModule::class,
            AppModule::class,
            NetworkModule::class,
            ResourceModule::class,
            TestDatabaseModule::class,
            ServiceModule::class,
            AssistedModule::class,
            AdapterModule::class,
            WorkerModule::class,
            BroadcastReceiverModule::class,
            ActivityModule::class
        ]
    )
    interface ComponentRuleTestComponent : AndroidInjector<ComponentRule> {
        @Component.Builder
        interface Builder {
            @BindsInstance
            fun context(context: Context): Builder

            fun build(): ComponentRuleTestComponent
        }

        fun context(): Context
        fun properties(): Properties

        fun gson(): Gson

        fun configResource(): ConfigResource
        fun accountResource(): AccountResource
        fun authenticationResource(): AuthenticationResource

        fun deviceResource(): DeviceResource
        fun smsResource(): SmsResource
        fun ussdResource(): UssdResource
        fun walletResource(): WalletResource

        fun accountService(): AccountService
        fun principalService(): PrincipalService
        fun sessionStorageService(): SessionStorageService
        fun authenticationService(): AuthenticationService

        fun accountRepository(): AccountRepository
        fun activityRepository(): ActivityRepository
        fun deviceRepository(): DeviceRepository
        fun transactionRepository(): TransactionRepository
        fun walletRepository(): WalletRepository

        fun injectableWorkerFactory(): InjectableWorkerFactory
    }
}
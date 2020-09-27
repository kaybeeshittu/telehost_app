package ng.myflex.telehost.config.module

import androidx.work.WorkerFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ng.myflex.telehost.common.content.ActivityWorkerFactory
import ng.myflex.telehost.common.content.InjectableWorkerFactory
import ng.myflex.telehost.common.key.WorkerKey
import ng.myflex.telehost.service.worker.*
import javax.inject.Singleton

@Module
interface WorkerModule {
    @Binds
    @Singleton
    fun bindWorkerFactory(factory: InjectableWorkerFactory): WorkerFactory

    @Binds
    @IntoMap
    @WorkerKey(UssdDialWorker::class)
    fun bindUssdDialWorker(factory: UssdDialWorker.Factory): ActivityWorkerFactory

    @Binds
    @IntoMap
    @WorkerKey(UssdSyncWorker::class)
    fun bindUssdSyncWorker(factory: UssdSyncWorker.Factory): ActivityWorkerFactory

    @Binds
    @IntoMap
    @WorkerKey(SmsMessagingWorker::class)
    fun bindSmsMessagingWorker(factory: SmsMessagingWorker.Factory): ActivityWorkerFactory

    @Binds
    @IntoMap
    @WorkerKey(SmsSyncWorker::class)
    fun bindSmsSyncWorker(factory: SmsSyncWorker.Factory): ActivityWorkerFactory

    @Binds
    @IntoMap
    @WorkerKey(SmsBackupWorker::class)
    fun bindSmsBackupWorker(factory: SmsBackupWorker.Factory): ActivityWorkerFactory
}
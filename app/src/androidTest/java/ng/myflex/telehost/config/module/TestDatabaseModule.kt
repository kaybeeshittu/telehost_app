package ng.myflex.telehost.config.module

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ng.myflex.telehost.config.Database
import ng.myflex.telehost.repository.*
import javax.inject.Singleton

@Module
class TestDatabaseModule {
    @Provides
    @Singleton
    fun providesDatabase(context: Context): Database {
        return Room.inMemoryDatabaseBuilder(context, Database::class.java).build()
    }

    @Provides
    @Singleton
    fun providesAccountRepository(database: Database): AccountRepository {
        return database.providesAccountRepository()
    }

    @Provides
    @Singleton
    fun providesActivityRepository(database: Database): ActivityRepository {
        return database.providesActivityRepository()
    }

    @Provides
    @Singleton
    fun providesDeviceRepository(database: Database): DeviceRepository {
        return database.providesDeviceRepository()
    }

    @Provides
    @Singleton
    fun providesTransactionRepository(database: Database): TransactionRepository {
        return database.providesTransactionRepository()
    }

    @Provides
    @Singleton
    fun providesWalletRepository(database: Database): WalletRepository {
        return database.providesWalletRepository()
    }
}
package ng.myflex.telehost.config

import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ng.myflex.telehost.domain.*
import ng.myflex.telehost.domain.converter.TypeConverter
import ng.myflex.telehost.repository.*

@androidx.room.Database(
    entities = [
        Account::class,
        Activity::class,
        Device::class,
        UserTransaction::class,
        Wallet::class
    ], version = Database.VERSION
)
@TypeConverters(TypeConverter::class)
abstract class Database : RoomDatabase() {
    companion object {
        const val VERSION = 1
    }

    abstract fun providesAccountRepository(): AccountRepository

    abstract fun providesActivityRepository(): ActivityRepository

    abstract fun providesDeviceRepository(): DeviceRepository

    abstract fun providesTransactionRepository(): TransactionRepository

    abstract fun providesWalletRepository(): WalletRepository
}
package ng.myflex.telehost.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ng.myflex.telehost.domain.Account

@Dao
interface AccountRepository {
    @Query("select * from Account order by id desc limit 1")
    suspend fun get(): Account

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(account: Account)

    @Query("DELETE FROM Account")
    suspend fun clear()
}
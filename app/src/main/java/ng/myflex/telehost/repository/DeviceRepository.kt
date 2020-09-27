package ng.myflex.telehost.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ng.myflex.telehost.domain.Device

@Dao
interface DeviceRepository {
    @Query("select * from Device where dId = :id")
    suspend fun get(id: Long): Device

    @Query("select * from Device where token = :token")
    suspend fun get(token: String): Device?

    @Query("select * from Device")
    suspend fun getAll(): List<Device>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAll(device: List<Device>)

    @Query("DELETE FROM Device")
    suspend fun clear()
}
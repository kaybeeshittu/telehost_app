package ng.myflex.telehost.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ng.myflex.telehost.domain.Activity
import ng.myflex.telehost.domain.enumeration.ActivityStatus
import ng.myflex.telehost.domain.enumeration.ActivityType

@Dao
interface ActivityRepository {
    @Query("select * from Activity where Activity.status = :status")
    suspend fun get(status: ActivityStatus): List<Activity>

    @Query("select * from Activity where Activity.type = :type and Activity.status = :status and Activity.id = :aId")
    suspend fun get(type: ActivityType, status: ActivityStatus, aId: Long): Activity?

    @Query("select * from Activity where Activity.type = :type and Activity.status = :status and Activity.id in(:aId)")
    suspend fun get(type: ActivityType, status: ActivityStatus, aId: LongArray): List<Activity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun save(activity: Activity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveAll(activities: List<Activity>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(activity: Activity): Long

    @Query("update Activity set status = :status where Activity.id in(:aId)")
    suspend fun update(status: ActivityStatus, aId: List<Long>)

    @Query("select count(*) from Activity")
    suspend fun size(): Int

    @Query("select count(*) from Activity where Activity.status = :status")
    suspend fun sizeByStatus(status: ActivityStatus): Int

    @Query("select count(*) from Activity where Activity.status = :status and Activity.type = :type")
    suspend fun sizeByTypeAndStatus(type: ActivityType, status: ActivityStatus): Int

    @Query("delete from Activity where Activity.status = :status")
    suspend fun deleteByStatus(status: ActivityStatus)
}
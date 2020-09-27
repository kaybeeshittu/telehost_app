package ng.myflex.telehost.domain

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import ng.myflex.telehost.domain.enumeration.ActivityStatus
import ng.myflex.telehost.domain.enumeration.ActivityType

@Entity(indices = [Index(value = ["refCode"], unique = true)])
data class Activity(
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    val number: String,
    val message: String,
    val response: String?,
    val accessCode: String?,
    val refCode: String,
    val simPort: Int,
    val type: ActivityType,
    val param: List<String>?,
    val status: ActivityStatus,
    val stamp: String
)


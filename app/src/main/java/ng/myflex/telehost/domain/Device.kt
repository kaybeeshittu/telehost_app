package ng.myflex.telehost.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Device(
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    val dId: Long,
    val token: String,
    val name: String,
    val duration: String,
    val port: String,
    val accessCode: String,
    val status: Int,
    val subscribedAt: String?,
    val validity: Int
)
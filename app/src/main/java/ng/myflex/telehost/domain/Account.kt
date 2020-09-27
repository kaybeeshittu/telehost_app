package ng.myflex.telehost.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Account(
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    val uId: Long,
    val key: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String,
    val verified: Int,
    val customerId: Double?,
    val country: String,
    val balance: Double,
    val ussd: Int,
    val sms: Int,
    val devices: Int
)

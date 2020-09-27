package ng.myflex.telehost.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserTransaction(
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    val amount: Double,
    val status: Int,
    val time: String
)
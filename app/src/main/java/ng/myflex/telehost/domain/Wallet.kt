package ng.myflex.telehost.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Wallet(
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    val status: Int
)
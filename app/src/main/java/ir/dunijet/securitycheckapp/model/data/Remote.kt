package ir.dunijet.securitycheckapp.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("remote_table")
data class Remote(

    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,

    val remoteId: String,
    val remoteName: String,
    val remoteStatus: Boolean,
    val remoteIsAdmin: Boolean
)
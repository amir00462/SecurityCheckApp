package ir.dunijet.securitycheckapp.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("log_table")
data class Log(

    @PrimaryKey(autoGenerate = true)
    val id:Int? = null,

    val text: String,
    val timeInMillies: Long = System.currentTimeMillis()
)
package ir.dunijet.securitycheckapp.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("output_name_table")
data class OutputName(

    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,

    val title :String ,
    val icon :Int

    )
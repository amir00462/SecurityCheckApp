package ir.dunijet.securitycheckapp.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("member_table")
data class Member(

    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,

    val typeIsModir: Boolean,

    val memberId: String,
    val memberNumber: String,

    )
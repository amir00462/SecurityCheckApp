package ir.dunijet.securitycheckapp.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import ir.dunijet.securitycheckapp.util.OutputType
import ir.dunijet.securitycheckapp.util.ZoneNooe
import ir.dunijet.securitycheckapp.util.ZoneType

@Entity("output_table")
data class Output(

    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,

    var title: String,
    var icon: Int,
    val outputId: String,
    var outputType: OutputType,
    var outputLahzeiiZaman :Float,

    var isEnabledInHome :Boolean,
    var lastUpdatedIsEnabledInHome :String

    )
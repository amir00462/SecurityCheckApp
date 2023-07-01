package ir.dunijet.securitycheckapp.model.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ir.dunijet.securitycheckapp.model.data.Log
import ir.dunijet.securitycheckapp.model.data.OutputName

@Dao
interface OutputNameDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(outputName: OutputName)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(outputNames: List<OutputName>)

    @Query("SELECT * FROM output_name_table")
    suspend fun getAll(): List<OutputName>

}
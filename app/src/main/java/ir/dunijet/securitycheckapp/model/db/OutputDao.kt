package ir.dunijet.securitycheckapp.model.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ir.dunijet.securitycheckapp.model.data.Output
import ir.dunijet.securitycheckapp.util.ZoneNooe
import ir.dunijet.securitycheckapp.model.data.Zone

@Dao
interface OutputDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(output: Output)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(outputs: List<Output>)

    @Query("SELECT * FROM output_table")
    suspend fun getAll(): List<Output>

}
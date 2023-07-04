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

    @Query("UPDATE output_table SET isEnabledInHome = :newEnabled AND lastUpdatedIsEnabledInHome = :lastUpdatedMillies WHERE outputId = :thisOutputId")
    suspend fun editOutputEnability(thisOutputId :String , newEnabled :Boolean , lastUpdatedMillies :String)

    @Query("DELETE FROM output_table WHERE outputId = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM output_table")
    suspend fun clear()

}
package ir.dunijet.securitycheckapp.model.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ir.dunijet.securitycheckapp.model.data.Remote

@Dao
interface RemoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(remote: Remote)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(remotes: List<Remote>)

    @Query("SELECT * FROM remote_table")
    suspend fun getAll(): List<Remote>

    @Query("UPDATE remote_table SET remoteName = :newName AND remoteStatus = :newStatus WHERE remoteName = :oldName")
    suspend fun editByName(oldName: String, newName: String, newStatus: Boolean)

    @Query("DELETE FROM remote_table WHERE remoteName = :name")
    suspend fun deleteByName(name: String)

    @Query("DELETE FROM remote_table")
    suspend fun clear()

}
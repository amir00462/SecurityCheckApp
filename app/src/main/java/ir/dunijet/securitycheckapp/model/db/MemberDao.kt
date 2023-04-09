package ir.dunijet.securitycheckapp.model.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ir.dunijet.securitycheckapp.model.data.Member

@Dao
interface MemberDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(member: Member)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(members: List<Member>)

    @Query("SELECT * FROM member_table")
    suspend fun getAll(): List<Member>

    @Query("UPDATE member_table SET memberNumber = :newNumber WHERE memberNumber = :oldNumber")
    suspend fun editByNumber(oldNumber :String , newNumber :String)

    @Query("DELETE FROM member_table WHERE memberNumber = :number")
    suspend fun deleteByNumber(number :String)

}
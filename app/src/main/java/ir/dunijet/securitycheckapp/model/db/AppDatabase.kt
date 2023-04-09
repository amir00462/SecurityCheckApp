package ir.dunijet.securitycheckapp.model.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ir.dunijet.securitycheckapp.model.data.Log
import ir.dunijet.securitycheckapp.model.data.Member
import ir.dunijet.securitycheckapp.model.data.Remote
import ir.dunijet.securitycheckapp.model.data.Zone

@Database(entities = [Log::class , Member::class , Remote::class , Zone::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun logDao(): LogDao
    abstract fun memberDao(): MemberDao
    abstract fun remoteDao(): RemoteDao
    abstract fun zoneDao(): ZoneDao
}
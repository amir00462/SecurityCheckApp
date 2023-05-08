package ir.dunijet.securitycheckapp.model.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ir.dunijet.securitycheckapp.model.data.*

@Database(entities = [Log::class , Member::class , Remote::class , Zone::class , Output::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun logDao(): LogDao
    abstract fun outputDao(): OutputDao
    abstract fun memberDao(): MemberDao
    abstract fun remoteDao(): RemoteDao
    abstract fun zoneDao(): ZoneDao
}
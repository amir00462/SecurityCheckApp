package ir.dunijet.securitycheckapp.service.local

import android.content.SharedPreferences
import ir.dunijet.securitycheckapp.model.data.*
import ir.dunijet.securitycheckapp.model.db.*
import ir.dunijet.securitycheckapp.util.ZoneNooe
import kotlinx.coroutines.*

@OptIn(DelicateCoroutinesApi::class)
class LocalRepository(
    private val sharedPref: SharedPreferences,
    private val logDao: LogDao,
    private val memberDao: MemberDao,
    private val remoteDao: RemoteDao,
    private val zoneDao: ZoneDao,
    private val outputDao: OutputDao
) {

    // shared pref
    fun clearLocal() {
        sharedPref.edit().clear().apply()
    }
    fun writeToLocal(key: String, value: String) {
        sharedPref.edit().putString(key, value).apply()
    }
    fun readFromLocal(key: String): String {
        return sharedPref.getString(key, "null")!!
    }

    // logs table
    suspend fun writeLog(newLog: Log) {
        logDao.insert(newLog)
    }
    suspend fun writeLogs(newLogs: List<Log>) {
        logDao.insert(newLogs)
    }
    suspend fun readLogs(): List<Log> {
        return logDao.getAll()
    }

    // member table
    suspend fun readMembers(): List<Member> {
        return memberDao.getAll()
    }
    suspend fun writeMembers(newMembers: List<Member>) {
        memberDao.insert(newMembers)
    }
    suspend fun writeMember(newMember: Member) {
        memberDao.insert(newMember)
    }
    suspend fun editMember(number: String, newNumber: String) {
        memberDao.editByNumber(number, newNumber)
    }
    suspend fun deleteMember(number: String) {
        memberDao.deleteByNumber(number)
    }

    // remote table
    suspend fun readRemotes(): List<Remote> {
        return remoteDao.getAll()
    }
    suspend fun writeRemotes(newMembers: List<Remote>) {
        remoteDao.insert(newMembers)
    }
    suspend fun writeRemote(newMember: Remote) {
        remoteDao.insert(newMember)
    }
    suspend fun editRemote(oldName: String, newName: String, newStatus: Boolean) {
        remoteDao.editByName(oldName, newName, newStatus)
    }
    suspend fun deleteRemote(name: String) {
        remoteDao.deleteByName(name)
    }

    // zone table
    suspend fun readWiredZones(): List<Zone> {
        return zoneDao.getAllWire()
    }
    suspend fun readWirelessZones(): List<Zone> {
        return zoneDao.getAllWireless()
    }
    suspend fun writeZones(newZones: List<Zone>) {
        zoneDao.insert(newZones)
    }
    suspend fun writeZone(newZone: Zone) {
        zoneDao.insert(newZone)
    }
    suspend fun editZone1(id: String, isWired: Boolean, newTitle: String) {
        zoneDao.editById(id, isWired, newTitle)
    }
    suspend fun editZone2(newZone :Zone) {
        //zoneDao.editByIdNooeZone(id, isWired, newTitle, newZoneNooe)
        zoneDao.deleteById(newZone.zoneId , newZone.typeIsWire)
        zoneDao.insert(newZone)
    }
    suspend fun deleteZone(id: String, isWired: Boolean) {
        zoneDao.deleteById(id, isWired)
    }
    suspend fun clearWiredZones() {
        zoneDao.clearWiredZones()
    }

    // output table
    suspend fun readOutputs(): List<Output> {
        return outputDao.getAll()
    }
    suspend fun editOutputEnability(outputId :String , isEnabledInHome :Boolean , lastUpdatedMillies :String) {
        outputDao.editOutputEnability(outputId , isEnabledInHome , lastUpdatedMillies)
    }
    suspend fun writeOutput(newOutput: Output) {
        outputDao.insert(newOutput)
    }
    suspend fun editOutput(newOutput: Output) {
        outputDao.deleteById(newOutput.outputId)
        outputDao.insert(newOutput)
    }
    suspend fun deleteOutput(outputId :String) {
        outputDao.deleteById(outputId)
    }

}
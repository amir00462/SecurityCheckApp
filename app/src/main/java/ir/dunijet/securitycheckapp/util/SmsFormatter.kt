package ir.dunijet.securitycheckapp.util

import ir.dunijet.securitycheckapp.model.data.Output
import ir.dunijet.securitycheckapp.model.data.Zone

class SmsFormatter {

    companion object {

        // sms body
        fun loginModir(numberUser: String, numberEngine: String, password: String): String {
            return """
                first_time
                admin_tell
                $password
                $numberUser
                $numberEngine
            """.trimIndent()
        }

        const val ResponseSignUpFirstTime = "please_change_your_system_password"
        const val ResponseSignUpNextTime = "login_again"

        fun loginUser(numberUser: String, numberEngine: String, password: String): String {
            return """
                getting_users_phone_numbers:
                $numberEngine
                $numberUser
                $password
            """.trimIndent()
        }

        const val ResponseLoginUser = "the_user_number_is_valid"

        fun changePassword(oldPass: String, newPass: String): String {
            return """
                change_password_admin
                old_pass
                $oldPass
                new_pass
                $newPass
            """.trimIndent()
        }

        const val ResponseAdminChangedUserPassword = "MERSSAD_SYSTEM_VERITY_CODE"

        fun setAdmin2(pass: String, numberAdmin2: String): String {
            return """
                set_admin_2
                save_to_system_admin_2
                $pass
                $numberAdmin2
            """.trimIndent()
        }

        const val ResponseSetAdmin2 = "admin_2_save_to_system"

        // only admin1 can do it
        fun createUser(userId: String, userNumber: String, adminPass: String): String {
            return """
                user_tell_system:
                save_user_tell_$userId:
                $userNumber
                $adminPass
            """.trimIndent()
        }

        const val ResponseCreateUser = "user_tell_number_" // #id

        // only admin1 can do it
        fun editUser(userId: String, userNumber: String, adminPass: String): String {
            return """
                user_tell_system:
                edit_user_tell_$userId:
                $userNumber
                $adminPass
            """.trimIndent()
        }

        const val ResponseEditUser = "user_tell_number_" // #id

        fun editAdmin2(numberAdmin2: String, password: String): String {
            return """
                set_admin_2
                edit_admin_2_to_system
                $password
                $numberAdmin2
            """.trimIndent()
        }
        // new number -> existed bushe dar list

        // only admin1 can do it
        fun deleteUser(userId: String, userNumber: String, adminPass: String): String {
            return """
                user_tell_system:
                del_user_tell_$userId:
                $userNumber
                $adminPass
            """.trimIndent()
        }

        const val ResponseDeleteUser = "user_tell_number_" // #id
        const val ResponseDeleteUser2 = "_delete"

        fun deleteAdmin2(numberAdmin2: String, password: String): String {
            return """
                set_admin_2
                del_admin_2_to_system
                $password
                $numberAdmin2
            """.trimIndent()
        }

        const val ResponseDeleteAdmin2 = "number_admin_tell_2_delete"

        fun getAllUsers(numberEngine: String, password: String): String {
            return """
                list_number_users:
                $password
            """.trimIndent()
        }

        const val ResponseGetAllUsers = "admin1"

        // response
        const val ResponseMain = "merssad"

        //  -    -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -

        // Remotes
        fun getAllRemotes(password: String): String {
            return """
                name_list_remote:
                $password
            """.trimIndent()
        }

        fun createRemote(remoteId: String, remoteName: String, remoteStatus: Boolean): String {
            return if (remoteStatus) {
                """
                        Config_remote:
                        get_name_remote_$remoteId
                        $remoteName:
                        rem_${remoteId}_on:
                    """.trimIndent()
            } else {
                """
                        Config_remote:
                        get_name_remote_$remoteId
                        $remoteName:
                        rem_${remoteId}_off:
                    """.trimIndent()
            }

        }

        fun editRemote(remoteId: String, remoteName: String, remoteStatus: Boolean): String {
            return if (remoteStatus) {
                """
                        Config_remote:
                        edit_name_remote_$remoteId
                        $remoteName:
                        rem_${remoteId}_on:
                    """.trimIndent()
            } else {
                """
                        Config_remote:
                        edit_name_remote_$remoteId
                        $remoteName:
                        rem_${remoteId}_off:
                    """.trimIndent()
            }
        }

        fun deleteRemote(remoteId: String): String {
            return """
                        config_remote:
                        del_remote_$remoteId:
                   """.trimIndent()
        }

        //  -    -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -

        //      HomeScreen ->
//      :1 حالت فعال
//      :2 حالت نیمه فعال
//      :3 حالت دینگ دانگ
//      :0 حالت غیر فعال
        fun getVaziatEngine(password: String): String {
            return """
                        home_page:
                        uph:
                        $password:
                   """
                .trimIndent()
        }

        fun updateVaziatEngine(password: String, it: HomeVaziat): String {

            val status = when (it) {
                HomeVaziat.Faal -> 1
                HomeVaziat.NimeFaal -> 2
                HomeVaziat.GheirFaal -> 0
            }

            return """
                        home_page:
                        status:$status
                        $password:
                   """
                .trimIndent()
        }

        fun getVaziatOutput(password: String, outputId: String): String {
            return """
                        home_page:
                        up_out_$outputId:
                        $password:
                   """
                .trimIndent()
        }

        fun updateVaziatOutput(password: String, outputId: String, vaziat: Boolean): String {

            val whatToSay = if (vaziat) "on" else "off"

            return """
                        home_page:
                        out_$outputId:$whatToSay
                        $password:
                   """
                .trimIndent()
        }

        // -    -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -

        // alarm screen ->
        fun changeAlarm(password: String, a1: String, a2: String, a3: String, a4: String): String {

            return """
                        alarm set:
                        $password:
                        at_the_time_of_theft:$a1
                        speaker_outside:$a2
                        siren_inside:$a3
                        volom_siren_inside:$a4
                   """
                .trimIndent()

        }

        // wired Zones ->
        fun saveWiredZones(password: String, wiredZones: List<Zone>): String {

            // 0 gheir faal
            // 1 faal
            // 2 nimeFaal
            // 3 dingDong
            // 4 faal shodan sensoor dood va atash
            // 5 sensor dood va atash gheir faal shodan

            val a1 = when (wiredZones.find { it.zoneId == "1" }!!.zoneStatus) {
                ZoneType.Faal -> 1
                ZoneType.NimeFaal -> 2
                ZoneType.GheirFaal -> 0
                ZoneType.DingDong -> 3
                else -> {
                    0
                }
            }

            val a2 = when (wiredZones.find { it.zoneId == "2" }!!.zoneStatus) {
                ZoneType.Faal -> 1
                ZoneType.NimeFaal -> 2
                ZoneType.GheirFaal -> 0
                ZoneType.DingDong -> 3
                else -> {
                    0
                }
            }

            val a3 = when (wiredZones.find { it.zoneId == "3" }!!.zoneStatus) {
                ZoneType.Faal -> 1
                ZoneType.NimeFaal -> 2
                ZoneType.GheirFaal -> 0
                ZoneType.DingDong -> 3
                else -> {
                    0
                }
            }

            val a4 = when (wiredZones.find { it.zoneId == "4" }!!.zoneStatus) {
                ZoneType.Faal -> 1
                ZoneType.NimeFaal -> 2
                ZoneType.GheirFaal -> 0
                ZoneType.DingDong -> 3
                else -> {
                    0
                }
            }

            val zone5 = wiredZones.find { it.zoneId == "5" }!!
            var a5 = -1
            when {

                zone5.zoneNooe == ZoneNooe.AtashDood && zone5.zoneStatus == ZoneType.Faal -> {
                    a5 = 4
                }

                zone5.zoneNooe == ZoneNooe.AtashDood && zone5.zoneStatus == ZoneType.GheirFaal -> {
                    a5 = 5
                }

                zone5.zoneNooe == ZoneNooe.Cheshmi && zone5.zoneStatus == ZoneType.Faal -> {
                    a5 = 1
                }

                zone5.zoneNooe == ZoneNooe.Cheshmi && zone5.zoneStatus == ZoneType.GheirFaal -> {
                    a5 = 0
                }

                zone5.zoneNooe == ZoneNooe.Cheshmi && zone5.zoneStatus == ZoneType.NimeFaal -> {
                    a5 = 2
                }

                zone5.zoneNooe == ZoneNooe.Cheshmi && zone5.zoneStatus == ZoneType.DingDong -> {
                    a5 = 3
                }

            }

            return """
                        config_room:
                        $password:
                        active_room_1:$a1
                        active_room_2:$a2
                        active_room_3:$a3
                        active_room_4:$a4
                        enable_fire:$a5
                   """
                .trimIndent()
        }

        // -    -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -
        // Output ->

        fun addNewOutput(password: String, newOutput: Output): String {

            when (newOutput.outputType) {

                OutputType.KhamooshRoshan -> {

                    // daem - khamoosh roshan
                    return """
                        out_${newOutput.outputId}_set:
                        $password:
                        stable:
                        1:
                    """.trimIndent()

                }

                OutputType.Lahzeii -> {

                    // lahzeii
                    return """
                        out_${newOutput.outputId}_set:
                        $password:
                        stable:
                        2:
                        ${newOutput.outputLahzeiiZaman.toInt()}:
                    """.trimIndent()

                }

                else -> {

                    // vabaste be dood atash
                    return """
                        out_${newOutput.outputId}_set:
                        $password:
                        stable:
                        3:
                    """.trimIndent()

                }
            }

        }
        fun editOutput(password: String, editingOutput: Output): String {

            when (editingOutput.outputType) {

                OutputType.KhamooshRoshan -> {

                    // daem - khamoosh roshan
                    return """
                        edit_out_${editingOutput.outputId}_set:
                        $password:
                        stable:
                        1:
                    """.trimIndent()

                }

                OutputType.Lahzeii -> {

                    // lahzeii
                    return """
                       edit_out_${editingOutput.outputId}_set:
                        $password:
                        stable:
                        2:
                        ${editingOutput.outputLahzeiiZaman.toInt()}:
                    """.trimIndent()

                }

                else -> {

                    // vabaste be dood atash
                    return """
                        edit_out_${editingOutput.outputId}_set:
                        $password:
                        stable:
                        3:
                    """.trimIndent()

                }
            }

        }
        fun deleteOutput(password: String, idDeleting :String): String {
            return """
                        out_${idDeleting}_delete:
                        $password:
                    """.trimIndent()
        }

    }
}
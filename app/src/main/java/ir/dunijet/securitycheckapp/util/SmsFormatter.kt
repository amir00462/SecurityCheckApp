package ir.dunijet.securitycheckapp.util

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
        fun updateVaziatEngine(password: String , it: HomeVaziat): String {

            val status = when(it) {
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
        fun getVaziatOutput(password: String ,outputId :String): String {
            return """
                        home_page:
                        up_out_$outputId:
                        $password:
                   """
                .trimIndent()
        }
        fun updateVaziatOutput(password: String , outputId :String , vaziat :Boolean): String {

            val whatToSay = if(vaziat) "on" else "off"

            return """
                        home_page:
                        out_$outputId:$whatToSay
                        $password:
                   """
                .trimIndent()
        }

        // -    -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -


    }

}
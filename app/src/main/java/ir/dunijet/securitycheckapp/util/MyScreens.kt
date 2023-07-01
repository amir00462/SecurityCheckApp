package ir.dunijet.securitycheckapp.util

sealed class MyScreens(val route: String) {
    object SignUpScreen : MyScreens("signUpScreen")
    object ChangePasswordScreen : MyScreens("changePasswordScreen")
    object SettingZone1Screen : MyScreens("settingZone1Screen")
    object SettingZone2Screen : MyScreens("settingZone2Screen")
    object SettingZone3Screen : MyScreens("settingZone3Screen")
    object MembersScreen : MyScreens("membersScreen")
    object ChangePasswordInApp : MyScreens("changePasswordInApp")
    object RemoteScreen : MyScreens("remoteScreen")
    object WiredZoneScreen : MyScreens("wiredZoneScreen")
    object WirelessZoneScreen : MyScreens("wirelessZoneScreen")
    object AlarmScreen : MyScreens("alarmScreen")
    object OutputScreen : MyScreens("outputScreen")
    object HomeScreen : MyScreens("homeScreen")
    object SelectOutputName : MyScreens("SelectOutputName")
}

package ir.dunijet.securitycheckapp.util

import ir.dunijet.securitycheckapp.model.data.Zone
import ir.dunijet.securitycheckapp.ui.theme.*
import ir.dunijet.securitycheckapp.R
import ir.dunijet.securitycheckapp.model.data.Member
import ir.dunijet.securitycheckapp.model.data.Output
import ir.dunijet.securitycheckapp.model.data.Remote

const val FIRST_LOGIC_DATA = "nufjas66wedd"
const val SECOND_LOGIC_DATA = "nubtoekdd"
const val SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED"
const val SMS_SENT = "SMS_SENT"

const val KEY_HOVIAT = "hoviat"
const val KEY_NUMBER_ENGINE = "keyNumberEngine"
const val KEY_NUMBER_USER = "keyNumberUser"
const val KEY_USER_PASSWORD = "keyUserPass"
const val KEY_SERIAL_ENGINE = "keySerialEngine"
const val KEY_IS_ADMIN = "isAdmin"

const val RouteToGo = "routeToGo"

const val WaitingToReceiveSms = 18000L

// New ->
enum class ZoneType {
    CheshmiFourTypes,
    CheshmiTwoTypes,
    GheirFaal,
    NimeFaal,
    Faal,
    DingDong,
    Hour24,
}

enum class ZoneNooe {
    Cheshmi,
    AtashDood
}

enum class ThemeData {
    LightTheme ,
    DarkTheme
}

enum class SensorType {
    Cheshmi,
    DoodAtash,
}

// Home Vaziat
enum class HomeVaziat {
    GheirFaal,
    NimeFaal,
    Faal
}

// Zones =>
enum class EyeTypes {
    GheirFaal,
    NimeFaal,
    Faal,
    DingDong
}

enum class CheshmiTwoTypesStep {
    FaalGheirFaal,
    BaleKheir,
    KhamooshRoshanLahzeii,
}

enum class SmsState {
    Init,
    Sent,
    NoService,
    AirplaneMode,
    Failed,
}

// Member
enum class MemberTask {
    AddUser,
    EditUser,
    DeleteUser
}

// Output =
enum class OutputType {
    KhamooshRoshan,
    Lahzeii,
    VabasteDoodAtash
}

// Colors =>
val darkColors = listOf(
    DPrimary,
    DColor1,
    DColor2,
    DColor3,
    DColor4,
    DColor5,
    DColor6,
    DColor7,
    DColor8,
    DColor9,
    LColorError,
    DBackground,
    DColorSuccess // 12
)
val lightColors = listOf(
    LPrimary, // 0
    LColor1,
    LColor2,
    LColor3,
    LColor4,
    LColor5,
    LColor6,
    LColor7,
    LColor8,
    LColor9,
    LColorError, // 10
    LBackground, // 11
    LColorSuccess // 12
)

val FAKE_MEMBER = Member(null, true, "", "")
val FAKE_REMOTE = Remote(null, "", "", false, false)
val FAKE_ZONE = Zone(null, false, "", R.drawable.ic_eye, "1", 2, ZoneType.CheshmiTwoTypes)
val FAKE_OUTPUT =
    Output(null, "چراغ\u200Cهای حیاط", R.drawable.ic_lamp, "1", OutputType.KhamooshRoshan, 27f)

val zoneType1 = listOf("غیرفعال", "نیمه فعال", "فعال", "دینگ دانگ")
val zoneType2 = listOf("غیرفعال", "24 ساعته", "فعال", "دینگ دانگ")
val zoneType3 = listOf("غیرفعال", "فعال")

fun getDefaultWiredZones(): List<Zone> {
    return listOf(
        Zone(
            null,
            true,
            "سنسور چشمی",
            R.drawable.ic_eye,
            "1",
            1,
            zoneStatus = ZoneType.GheirFaal,
            zoneNooe = ZoneNooe.Cheshmi
        ),
        Zone(
            null,
            true,
            "دوربین های پارکینگ",
            R.drawable.ic_eye,
            "2",
            1,
            zoneStatus = ZoneType.GheirFaal,
            zoneNooe = ZoneNooe.Cheshmi
        ),
        Zone(
            null,
            true,
            "دوربین های داخلی",
            R.drawable.ic_eye,
            "3",
            1,
            zoneStatus = ZoneType.GheirFaal,
            zoneNooe = ZoneNooe.Cheshmi
        ),
        Zone(
            null,
            true,
            "دوربین های حیاط",
            R.drawable.ic_eye,
            "4",
            2,
            zoneStatus = ZoneType.GheirFaal,
            zoneNooe = ZoneNooe.Cheshmi
        ),
        Zone(
            null,
            true,
            "سنسور دود و آتش",
            R.drawable.ic_fire,
            "5",
            3,
            zoneStatus = ZoneType.GheirFaal,
            zoneNooe = ZoneNooe.AtashDood
        ),
    )
}

fun getDefaultOutputs(): List<Output> {
    return listOf(

        Output(null, "چراغ\u200Cهای حیاط", R.drawable.ic_lamp, "1", OutputType.KhamooshRoshan, 27f),
        Output(
            null,
            "وابسته به سنسور دود و آتش",
            R.drawable.ic_fire,
            "2",
            OutputType.VabasteDoodAtash,
            27f
        )

    )
}

val outputNameList = listOf(
    Pair("درب خروجی", R.drawable.ic_home),
    Pair("درب ورودی", R.drawable.ic_home),
    Pair("پارکینگ", R.drawable.ic_home),
    Pair("انبار شیراز", R.drawable.ic_home),
    Pair("انبار تهران", R.drawable.ic_home),
    Pair("انبار اهواز", R.drawable.ic_home),
)
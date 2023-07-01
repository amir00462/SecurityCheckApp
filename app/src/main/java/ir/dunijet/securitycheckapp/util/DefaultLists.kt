package ir.dunijet.securitycheckapp.util

import ir.dunijet.securitycheckapp.R
import ir.dunijet.securitycheckapp.model.data.*

val FAKE_MEMBER = Member(null, true, "", "")
val FAKE_REMOTE = Remote(null, "", "", false, false)
val FAKE_ZONE = Zone(null, false, "", R.drawable.ic_eye, "1", 2, ZoneType.CheshmiTwoTypes)
val FAKE_WIRELESS_ZONE = Zone(null, false, "", R.drawable.ic_eye, "1", 2, zoneStatus = ZoneType.GheirFaal)
val FAKE_OUTPUT = Output(null, "چراغ\u200Cهای حیاط", R.drawable.ic_lamp, "1", OutputType.KhamooshRoshan, 27f , false , "نیاز به بروزرسانی")
val FAKE_OUTPUT_NAME = OutputName(null ,"درب منزل", R.drawable.ic_home)

val fakeOutputNameList = listOf(
    OutputName(null , "روشنایی داخل ساختمان", R.drawable.ic_home),
    OutputName(null ,"روشنایی محوطه", R.drawable.ic_home),
    OutputName(null ,"روشنایی پارکینگ", R.drawable.ic_home),
    OutputName(null ,"روشنایی حیاط", R.drawable.ic_home),
    OutputName(null ,"روشنایی انبار", R.drawable.ic_home),
    OutputName(null ,"روشنایی راه پله", R.drawable.ic_home),
    OutputName(null ,"روشنایی اتاق", R.drawable.ic_home),
    OutputName(null ,"درب منزل", R.drawable.ic_home),
    OutputName(null ,"درب حیاط", R.drawable.ic_home),
    OutputName(null ,"درب پارکینگ", R.drawable.ic_home),
    OutputName(null ,"درب انبار", R.drawable.ic_home),
    OutputName(null ,"کولر", R.drawable.ic_home),
    OutputName(null ,"پنکه", R.drawable.ic_home),
    OutputName(null ,"پکیج", R.drawable.ic_home),
    OutputName(null ,"کامپیوتر", R.drawable.ic_home),
    OutputName(null ,"اتاق سرور", R.drawable.ic_home),
    OutputName(null ,"برق کلی ساختمان", R.drawable.ic_home),
    OutputName(null ,"برق آسانسور", R.drawable.ic_home),
)

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

        Output(null, "چراغ\u200Cهای حیاط", R.drawable.ic_lamp, "1", OutputType.KhamooshRoshan, 27f , false , "نیاز به بروز رسانی"),
        Output(
            null,
            "وابسته به سنسور دود و آتش",
            R.drawable.ic_fire,
            "2",
            OutputType.VabasteDoodAtash,
            27f,
            false ,
            "نیاز به بروز رسانی"
        )

    )
}
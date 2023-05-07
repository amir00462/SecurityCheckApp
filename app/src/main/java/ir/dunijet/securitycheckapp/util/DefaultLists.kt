package ir.dunijet.securitycheckapp.util

import ir.dunijet.securitycheckapp.R
import ir.dunijet.securitycheckapp.model.data.Member
import ir.dunijet.securitycheckapp.model.data.Output
import ir.dunijet.securitycheckapp.model.data.Remote
import ir.dunijet.securitycheckapp.model.data.Zone

val FAKE_MEMBER = Member(null, true, "", "")
val FAKE_REMOTE = Remote(null, "", "", false, false)
val FAKE_ZONE = Zone(null, false, "", R.drawable.ic_eye, "1", 2, ZoneType.CheshmiTwoTypes)
val FAKE_OUTPUT = Output(null, "چراغ\u200Cهای حیاط", R.drawable.ic_lamp, "1", OutputType.KhamooshRoshan, 27f)

val outputNameList = listOf(
    Pair("درب خروجی", R.drawable.ic_home),
    Pair("درب ورودی", R.drawable.ic_home),
    Pair("پارکینگ", R.drawable.ic_home),
    Pair("انبار شیراز", R.drawable.ic_home),
    Pair("انبار تهران", R.drawable.ic_home),
    Pair("انبار اهواز", R.drawable.ic_home),
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
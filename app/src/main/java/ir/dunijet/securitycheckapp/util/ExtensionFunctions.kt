package ir.dunijet.securitycheckapp.util

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.util.LayoutDirection
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.layoutDirection
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import ir.dunijet.securitycheckapp.R
import ir.dunijet.securitycheckapp.model.data.Output
import ir.dunijet.securitycheckapp.ui.MainActivity
import ir.dunijet.securitycheckapp.ui.MainActivity.Companion.appColors
import ir.dunijet.securitycheckapp.ui.theme.VazirFont
import kotlinx.coroutines.CoroutineExceptionHandler
import saman.zamani.persiandate.PersianDate
import saman.zamani.persiandate.PersianDateFormat
import java.util.*

fun List<Output>.isExist(outputId: String): Boolean {

    forEach {
        if (it.outputId == outputId)
            return true
    }

    return false
}

fun Context.correctDate(timeInMillies: Long): String {

    val formatter1 = PersianDateFormat("Y/m/d")
    val formatter2 = PersianDateFormat("H:i")
    val formatter3 = PersianDateFormat("d")
    val savedTime = PersianDate(timeInMillies)

    // isToday
    return when {

        formatter3.format(PersianDate()).toInt() == formatter3.format(savedTime).toInt() -> {
            "امروز" + " ساعت " + formatter2.format(savedTime)
        }

        formatter3.format(PersianDate()).toInt() - 1 == formatter3.format(savedTime).toInt() -> {
            "دیروز" + " ساعت " + formatter2.format(savedTime)
        }

        else -> {
            formatter1.format(savedTime) + " ساعت " + formatter2.format(savedTime)
        }

    }

}

val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
    Log.v("error", "Error -> " + throwable.message)
}

fun convertPixelsToDp(px: Int, context: Context): Int {
    return px / (context.resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
}

fun String.correctNumber(): String {
    return this.replaceFirst("+98", "0")
}

fun String.beautifyNumber(): String {

    if (this == "") {
        return this
    }

    if (this.count() < 11) {
        val disCount = 11 - count()
        for (i in 0..disCount) {
            this.plus(" ")
        }
    }

    val first = this.substring(0, 4)
    val second = this.substring(4, 7)
    val third = this.substring(7)

    return first + " " + second + " " + third
}




@Stable
fun Modifier.mirror(): Modifier {

    return if (Locale.getDefault().layoutDirection
        == LayoutDirection.RTL)

        this.scale(scaleX = -1f, scaleY = 1f)
    else
        this
}





fun Context.showToast(str: String) {
    Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
}

fun isPermissionGranted(context: Context): Boolean {
    var result = false

    Dexter.withContext(context)
        .withPermissions(
            Manifest.permission.SEND_SMS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.RECEIVE_SMS
        )
        .withListener(object : MultiplePermissionsListener {

            override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                result = report.areAllPermissionsGranted()
            }

            override fun onPermissionRationaleShouldBeShown(
                p0: MutableList<PermissionRequest>?,
                p1: PermissionToken?
            ) {
            }

        }).check()

    return result
}

//fun getFlags() :Int {
//    return when {
//        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ->
//            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
//        else -> {
//            PendingIntent.FLAG_UPDATE_CURRENT
//        }
//    }
//}

//fun getFlags() :Int {
//    return when {
//        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ->
//            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_MUTABLE
//        else -> {
//            PendingIntent.FLAG_NO_CREATE
//        }
//    }
//}

fun getFlags(): Int {
    return when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ->
            PendingIntent.FLAG_IMMUTABLE
        else -> {
            0
        }
    }
}


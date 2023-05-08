package ir.dunijet.securitycheckapp.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import ir.dunijet.securitycheckapp.R

// Set of Material typography styles to start with
val VazirFont = FontFamily(
    Font(R.font.vazir_regular, FontWeight.Normal),
    Font(R.font.vazari_bold, FontWeight.Bold),
    Font(R.font.vazir_thin, FontWeight.Thin),)

val VazirFontDigits = FontFamily(
    Font(R.font.vazir_regular_digits, FontWeight.Normal))

val Typography = Typography(

    // bold fonts =>
    h1 = TextStyle(
        fontFamily = VazirFont,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp ,
        lineHeight = 36.sp
    ) ,
    h2 = TextStyle(
        fontFamily = VazirFont,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp ,
        lineHeight = 24.sp
    ) ,
    h3 = TextStyle(
        fontFamily = VazirFont,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp ,
        lineHeight = 20.sp
    ) ,

    // medium fonts =>
    body1 = TextStyle(
        fontFamily = VazirFont,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp ,
        lineHeight = 24.sp
    ) ,
    body2 = TextStyle(
        fontFamily = VazirFont,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp ,
        lineHeight = 20.sp
    ) ,

    // small fonts =>
    caption = TextStyle(
        fontFamily = VazirFont,
        fontWeight = FontWeight.Thin,
        fontSize = 16.sp ,
        lineHeight = 26.sp
    ) ,

    // other fonts =>
    defaultFontFamily = VazirFont ,
    button = TextStyle(
        fontFamily = VazirFont,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),

    /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
)
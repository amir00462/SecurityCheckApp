package ir.dunijet.securitycheckapp.ui.features

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.burnoo.cokoin.navigation.getNavController
import ir.dunijet.securitycheckapp.R
import ir.dunijet.securitycheckapp.ui.Zone
import ir.dunijet.securitycheckapp.util.CheshmiTwoTypesStep
import ir.dunijet.securitycheckapp.util.EyeTypes
import ir.dunijet.securitycheckapp.util.MyScreens
import ir.dunijet.securitycheckapp.util.ZoneType

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SettingZone2Screen() {

    val context = LocalContext.current
    val navigation = getNavController()

    // states
    val set1 = remember { mutableStateOf(27f) }
    val set2 = remember { mutableStateOf(EyeTypes.Faal) }
    val set3 = remember { mutableStateOf(EyeTypes.GheirFaal) }
    val set4 = remember { mutableStateOf(7f) }
    val set5 = remember { mutableStateOf(15f) }
    val set6 = remember { mutableStateOf(EyeTypes.Faal) }

    Scaffold(
        topBar = {

            TopAppBar(

                title = {
                    Text(
                        modifier = Modifier.fillMaxWidth(0.76f),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        text = "مرحله ٢ از ۳",
                        color = MaterialTheme.colors.onSecondary,
                        textAlign = TextAlign.Center

                    )
                },

                navigationIcon = {
                    IconButton(onClick = { navigation.popBackStack() }) {
                        Icon(
                            modifier = Modifier.scale(scaleX = -1f, scaleY = 1f),
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back Button",
                            tint = MaterialTheme.colors.onSecondary
                        )
                    }
                },

                backgroundColor = MaterialTheme.colors.primaryVariant,
                contentColor = Color.Gray,
                elevation = 0.dp
            )

        }
    ) {

        Surface(color = MaterialTheme.colors.background, modifier = Modifier.fillMaxSize()) {

            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {

                Text(
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    text = "تنظیمات آژیرها",
                    modifier = Modifier.padding(top = 22.dp, start = 16.dp)
                )

                Text(
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp,
                    text = "موقع سرقت",
                    modifier = Modifier.padding(top = 24.dp, start = 16.dp)
                )

                Zone(
                    Modifier.padding(top = 16.dp),
                    "زمان صدای آژیرها",
                    R.drawable.ic_clock,
                    "ثانیه",
                    40f,
                    set1.value
                ) {
                    set1.value = it
                }

                Text(
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp,
                    text = "حالت عادی",
                    modifier = Modifier.padding(top = 24.dp, start = 16.dp)
                )

                Zone(
                    Modifier.padding(top = 16.dp),
                    "آژیر خارجی",
                    R.drawable.ic_warning,
                    ZoneType.CheshmiTwoTypes,
                    CheshmiTwoTypesStep.FaalGheirFaal,
                    set2.value,
                ) {
                    set2.value = it
                }

                Zone(
                    Modifier.padding(top = 16.dp),
                    "آژیر داخلی",
                    R.drawable.ic_warning,
                    ZoneType.CheshmiTwoTypes,
                    CheshmiTwoTypesStep.FaalGheirFaal,
                    set3.value,
                ) {
                    set3.value = it
                }

                Zone(
                    Modifier.padding(top = 16.dp),
                    "بلندی صدای آژیر داخلی",
                    R.drawable.ic_ring,
                    null,
                    60f,
                    set4.value
                ) {
                    set4.value = it
                }

                Zone(
                    Modifier.padding(top = 16.dp),
                    "زمان صدای آژیر داخلی",
                    R.drawable.ic_clock,
                    "ثانیه",
                    60f,
                    set5.value
                ) {
                    Log.v("testLog", it.toInt().toString())
                    set5.value = it
                }

                Text(
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    text = "تنظیمات پیامک",
                    modifier = Modifier.padding(top = 24.dp, start = 16.dp)
                )

                Zone(
                    Modifier.padding(top = 16.dp),
                    "دریافت پیامک قطع و وصل برق",
                    R.drawable.ic_mail,
                    ZoneType.CheshmiTwoTypes,
                    CheshmiTwoTypesStep.BaleKheir,
                    set6.value,
                ) {
                    set6.value = it
                }

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 24.dp, bottom = 24.dp),
                    shape = MaterialTheme.shapes.large,
                    onClick = { navigation.navigate(MyScreens.SettingZone3Screen.route) }) {
                    Text(
                        text = "ذخیره و ارسال",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White

                    )
                }
            }
        }

    }


}
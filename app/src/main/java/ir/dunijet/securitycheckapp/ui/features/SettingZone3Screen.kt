package ir.dunijet.securitycheckapp.ui.features

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

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SettingZone3Screen() {

    val context = LocalContext.current
    val navigation = getNavController()

    // states
    val set1 = remember { mutableStateOf(EyeTypes.Faal) }
    val set2 = remember { mutableStateOf(EyeTypes.GheirFaal) }


    Scaffold(
        topBar = {

            TopAppBar(

                title = {
                    Text(
                        modifier = Modifier.fillMaxWidth(0.76f),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        text = "مرحله ۳ از ۳",
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
                    text = "تنظیمات خروجی ها",
                    modifier = Modifier.padding(top = 22.dp, start = 16.dp)
                )

                Zone(
                    Modifier.padding(top = 24.dp),
                    "درب ورودی منزل",
                    R.drawable.ic_home,
                    CheshmiTwoTypesStep.KhamooshRoshanLahzeii,
                    set1.value,
                    { set1.value = it },
                ) {

                }

                Zone(
                    Modifier.padding(top = 16.dp),
                    "چراغ های حیاط",
                    R.drawable.ic_lamp,
                    CheshmiTwoTypesStep.KhamooshRoshanLahzeii,
                    set2.value,
                    { set2.value = it },
                ) {

                }

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 24.dp, bottom = 24.dp),
                    shape = MaterialTheme.shapes.large,
                    onClick = {

                        navigation.navigate(MyScreens.MembersScreen.route)

                    }) {
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
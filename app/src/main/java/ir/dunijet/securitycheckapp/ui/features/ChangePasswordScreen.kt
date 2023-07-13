package ir.dunijet.securitycheckapp.ui.features

import android.content.BroadcastReceiver
import android.content.IntentFilter
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.burnoo.cokoin.get
import dev.burnoo.cokoin.navigation.getNavController
import ir.dunijet.securitycheckapp.model.data.Log
import ir.dunijet.securitycheckapp.service.local.LocalRepository
import ir.dunijet.securitycheckapp.service.sms.SmsRepository
import ir.dunijet.securitycheckapp.ui.MainActivity
import ir.dunijet.securitycheckapp.ui.TimingButton
import ir.dunijet.securitycheckapp.util.*

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ChangePasswordScreen() {

    lateinit var smsReceived: BroadcastReceiver
    lateinit var smsSent: BroadcastReceiver
    val smsService = get<SmsRepository>()
    val context = LocalContext.current
    val mainActivity = LocalContext.current as MainActivity
    val navigation = getNavController()
    val databaseService = get<LocalRepository>()
    val buttonIsLoading = remember { mutableStateOf(false) }

    // states
    val newPassword = remember { mutableStateOf("") }
    val newPasswordTekrar = remember { mutableStateOf("") }

    // useful functions
    fun validateInputs(): Boolean {
        if (newPassword.value.length == 6 && newPasswordTekrar.value.length == 6)
            if (newPassword.value == newPasswordTekrar.value)
                return true
        return false
    }
    fun changePasswordLogic() {
        buttonIsLoading.value = true

        // register
        smsReceived = smsReceivedListener(
            databaseService.readFromLocal(KEY_NUMBER_ENGINE),
            databaseService.readFromLocal(KEY_SERIAL_ENGINE)
        ) {

            val changedPassword = (it.lines()[2].split(':')[1]).removeRange(0, 1)
            if (changedPassword == newPassword.value) {
                mainActivity.logMain.add(Log(text = navigation.currentDestination?.route + "-" + "sms sent" + "-" + "Password_Changed"))

                mainActivity.addLogsToDb()

                context.unregisterReceiver(smsSent)
                context.unregisterReceiver(smsReceived)

                databaseService.writeToLocal(KEY_USER_PASSWORD, changedPassword)
                mainActivity.databaseService.writeToLocal(AuthenticatedOrNot, "home")
                navigation.navigate(MyScreens.HomeScreen.route)
            } else {
                mainActivity.logMain.add(Log(text = navigation.currentDestination?.route + "-" + "sms sent" + "-" + "Password_Change_Failed"))
                context.showToast("عملیات تغییر پسورد ناموفق بود")
            }

        }
        smsSent = smsSentListener(
            context,
            navigation.currentDestination?.route!!,
            { buttonIsLoading.value = it },
            { mainActivity.logMain.add(it) })
        context.registerReceiver(smsReceived, IntentFilter(SMS_RECEIVED))
        context.registerReceiver(smsSent, IntentFilter(SMS_SENT))

        // send sms =>
        val formattedSms = SmsFormatter.changePassword(
            databaseService.readFromLocal(KEY_USER_PASSWORD),
            newPassword.value
        )
        smsService.sendSms(
            formattedSms,
            databaseService.readFromLocal(KEY_NUMBER_ENGINE)
        )

    }
    DisposableEffect(Unit) {
        onDispose {
            context.unregisterReceiver(smsReceived)
            context.unregisterReceiver(smsSent)
        }
    }

    MainActivity.checkPermissions(context)
    Scaffold(
        topBar = {

            TopAppBar(

                title = {

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
                    text = "تغییر رمز ورود دستگاه",
                    modifier = Modifier.padding(top = 22.dp, start = 16.dp)
                )

                OtpTextField(
                    modifier = Modifier.padding(top = 24.dp),
                    txtSubject = "رمز ورود جدید",
                    onValueChanged = { newPassword.value = it },
                    value = newPassword.value,
                    length = 6
                )

                OtpTextField(
                    modifier = Modifier.padding(top = 24.dp),
                    txtSubject = "تکرار رمز ورود جدید",
                    onValueChanged = { newPasswordTekrar.value = it },
                    value = newPasswordTekrar.value,
                    length = 6
                )

                TimingButton(buttonIsLoading, validateInputs()) {

                    if (validateInputs()) {
                        changePasswordLogic()
                    } else
                        context.showToast("لطفا همه مقادیر را به درستی وارد کنید")

                }
            }
        }
    }
}
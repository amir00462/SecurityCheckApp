package ir.dunijet.securitycheckapp.ui.features

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.net.Uri
import android.util.Patterns
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import dev.burnoo.cokoin.get
import dev.burnoo.cokoin.navigation.getNavController
import ir.dunijet.securitycheckapp.model.data.Log
import ir.dunijet.securitycheckapp.service.local.LocalRepository
import ir.dunijet.securitycheckapp.service.sms.SmsRepository
import ir.dunijet.securitycheckapp.ui.MainActivity
import ir.dunijet.securitycheckapp.ui.MainActivity.Companion.appColors
import ir.dunijet.securitycheckapp.ui.MainActivity.Companion.checkPermissions
import ir.dunijet.securitycheckapp.util.*
import ir.dunijet.securitycheckapp.util.SmsFormatter.Companion.ResponseLoginUser
import ir.dunijet.securitycheckapp.util.SmsFormatter.Companion.ResponseMain
import ir.dunijet.securitycheckapp.util.SmsFormatter.Companion.ResponseSignUpFirstTime
import ir.dunijet.securitycheckapp.util.SmsFormatter.Companion.ResponseSignUpNextTime
import ir.dunijet.securitycheckapp.ui.MainTextField
import ir.dunijet.securitycheckapp.ui.TimingButton
import ir.dunijet.securitycheckapp.ui.ToggleButtonGroup
import ir.dunijet.securitycheckapp.ui.theme.VazirFontDigits
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Exception

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SignUpScreen() {

    // receivers
    lateinit var smsSent: BroadcastReceiver
    lateinit var smsReceived: BroadcastReceiver

    // needable
    val databaseService = get<LocalRepository>()
    val smsService = get<SmsRepository>()

    // variables
    val context = LocalContext.current
    val mainActivity = LocalContext.current as MainActivity
    val navigation = getNavController()
    val buttonIsLoading = remember { mutableStateOf(false) }

    // states
    val numberEngine = remember { mutableStateOf("") }
    val numberUser = remember { mutableStateOf("") }
    val passwordEngine = remember { mutableStateOf("") }
    val typeIsModir = remember { mutableStateOf(true) }

    // useful functions
    fun validateInputs(): Boolean {
        if (numberEngine.value.length == 11 && numberUser.value.length == 11 && passwordEngine.value.length == 6)
            if (numberEngine.value.startsWith("09") && numberUser.value.startsWith("09"))
                if (Patterns.PHONE.matcher(numberEngine.value).matches() && Patterns.PHONE.matcher(
                        numberUser.value
                    ).matches()
                )
                    return true
        return false
    }

    fun appLogic() {

        smsReceived = smsReceivedListener(numberEngine.value, ResponseMain) {

            //deleteSms(context , numberEngine.value)
            var serial = ""

            when {

                // admin first time -> change password
                it.contains(ResponseSignUpFirstTime) -> {
                    mainActivity.logMain.add(Log(text = navigation.currentDestination?.route + "-" + "sms received" + "-" + "loginAdminFirstTime"))
                    buttonIsLoading.value = false

                    serial = (it.lines()[1].split(":"))[1]

                    // save if it is admin1 or 2
                    when {
                        it.contains("tell_admin_number_1") -> {
                            // save as admin 1
                            databaseService.writeToLocal(
                                KEY_HOVIAT,
                                "admin1"
                            )
                        }
                        it.contains("tell_admin_number_2") -> {
                            // save as admin 2
                            databaseService.writeToLocal(
                                KEY_HOVIAT,
                                "admin2"
                            )
                        }

                    }

                    // save needable data
                    databaseService.writeToLocal(
                        KEY_NUMBER_ENGINE,
                        numberEngine.value
                    )
                    databaseService.writeToLocal(KEY_NUMBER_USER, numberUser.value)
                    databaseService.writeToLocal(
                        KEY_USER_PASSWORD,
                        passwordEngine.value
                    )

                    databaseService.writeToLocal(
                        KEY_SERIAL_ENGINE,
                        serial
                    )

                    // add logs to Db
                    mainActivity.addLogsToDb()

                    context.unregisterReceiver(smsReceived)
                    context.unregisterReceiver(smsSent)

                    mainActivity.databaseService.writeToLocal(AuthenticatedOrNot , "changePassword")
                    navigation.navigate(MyScreens.ChangePasswordScreen.route) {
                        popUpTo(MyScreens.SignUpScreen.route) {
                            inclusive = true
                        }
                    }

                }

                // admin or user not first time -> main screen , password changed
                it.contains(ResponseSignUpNextTime) -> {
                    mainActivity.logMain.add(Log(text = navigation.currentDestination?.route + "-" + "sms received" + "-" + "loginAgain"))
                    buttonIsLoading.value = false

                    serial = it.lines()[1].split(":")[1]
                    val userNumber = it.lines()[2].split("_")[1]

                    // save if it is admin1 or 2
                    when {
                        it.contains("tell_admin_number_1") -> {
                            // save as admin 1
                            databaseService.writeToLocal(
                                KEY_HOVIAT,
                                "admin1"
                            )
                        }
                        it.contains("tell_admin_number_2") -> {
                            // save as admin 2
                            databaseService.writeToLocal(
                                KEY_HOVIAT,
                                "admin2"
                            )
                        }
                        it.contains("user") -> {
                            databaseService.writeToLocal(
                                KEY_HOVIAT,
                                "user"
                            )
//                            databaseService.writeToLocal(
//                                KEY_USER_NUMBER,
//                                userNumber
//                            )
                        }

                    }

                    // save needable data
                    databaseService.writeToLocal(
                        KEY_NUMBER_ENGINE,
                        numberEngine.value
                    )
                    databaseService.writeToLocal(KEY_NUMBER_USER, numberUser.value)
                    databaseService.writeToLocal(
                        KEY_USER_PASSWORD,
                        passwordEngine.value
                    )

                    databaseService.writeToLocal(
                        KEY_SERIAL_ENGINE,
                        serial
                    )

                    mainActivity.addLogsToDb()

                    context.unregisterReceiver(smsReceived)
                    context.unregisterReceiver(smsSent)

                    mainActivity.databaseService.writeToLocal(AuthenticatedOrNot , "home")
                    navigation.navigate(MyScreens.HomeScreen.route) {
                        popUpTo(MyScreens.SignUpScreen.route) {
                            inclusive = true
                        }
                    }

                }

                // login user - first time
                it.contains(ResponseLoginUser) -> {
                    mainActivity.logMain.add(Log(text = navigation.currentDestination?.route + "-" + "sms received" + "-" + "loginUserFirstTime"))
                    buttonIsLoading.value = false

                    serial = it.lines()[1].split("r")[2]
                    val userNumber = it.lines()[2].split("_")[1]

                    // save needable data
                    databaseService.writeToLocal(
                        KEY_NUMBER_ENGINE,
                        numberEngine.value
                    )
                    databaseService.writeToLocal(KEY_NUMBER_USER, numberUser.value)
                    databaseService.writeToLocal(
                        KEY_USER_PASSWORD,
                        passwordEngine.value
                    )

                    databaseService.writeToLocal(
                        KEY_SERIAL_ENGINE,
                        serial
                    )

                    databaseService.writeToLocal(
                        KEY_HOVIAT,
                        "user"
                    )

//                    databaseService.writeToLocal(
//                        KEY_USER_NUMBER,
//                        userNumber
//                    )

                    // add logs to db
                    mainActivity.addLogsToDb()

                    context.unregisterReceiver(smsReceived)
                    context.unregisterReceiver(smsSent)

                    mainActivity.databaseService.writeToLocal(AuthenticatedOrNot , "home")
                    navigation.navigate(MyScreens.HomeScreen.route) {
                        popUpTo(MyScreens.SignUpScreen.route) {
                            inclusive = true
                        }
                    }

                }

            }

        }
        smsSent = smsSentListener(context, navigation.currentDestination?.route!!, { buttonIsLoading.value = it }, { mainActivity.logMain.add(it) })

        context.registerReceiver(smsReceived, IntentFilter(SMS_RECEIVED))
        context.registerReceiver(smsSent, IntentFilter(SMS_SENT))

        val formattedSms = if (typeIsModir.value) SmsFormatter.loginModir(
            numberUser.value,
            numberEngine.value,
            passwordEngine.value
        ) else SmsFormatter.loginUser(numberUser.value, numberEngine.value, passwordEngine.value)
        smsService.sendSms(formattedSms, numberEngine.value)

    }

    // main ui - check permission
    checkPermissions(context)
    Surface(color = appColors[11], modifier = Modifier.fillMaxSize()) {

        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {

            Text(
                modifier = Modifier.padding(top = 22.dp, start = 16.dp),
                text = "ورود به سیستم به عنوان",
                color = appColors[8],
                style = MaterialTheme.typography.h1
            )

            ToggleButtonGroup(Modifier.padding(top = 22.dp), "کاربر", "مدیر") {
                typeIsModir.value = it
            }

            MainTextField(
                null ,
                "شماره سیم کارت دستگاه",
                edtValue = numberEngine.value,
                onValueChanges = { numberEngine.value = it })

            MainTextField(
                null ,
                if (typeIsModir.value) "شماره موبایل مدیر" else "شماره موبایل کاربر",
                edtValue = numberUser.value,
                onValueChanges = { numberUser.value = it })

            OtpTextField(
                modifier = Modifier.padding(top = 24.dp),
                txtSubject = if (typeIsModir.value) "رمز ورود دستگاه" else "رمز ورود",
                onValueChanged = { passwordEngine.value = it },
                value = passwordEngine.value,
                length = 6
            )

            TimingButton(buttonIsLoading, validateInputs()) {

                if (validateInputs()) {
                    appLogic()
                } else
                    context.showToast("لطفا همه مقادیر را به درستی وارد کنید")

            }

        }

    }
}


@ExperimentalComposeUiApi
@Composable
fun OtpTextField(
    modifier: Modifier,
    txtSubject: String,
    length: Int = 6,
    value: String = "",
    onValueChanged: (String) -> Unit

) {

    Column {

        Text(
            modifier = modifier.padding(start = 16.dp),
            text = txtSubject,
            color = appColors[6],
            style = MaterialTheme.typography.h3
        )

        CompositionLocalProvider(LocalLayoutDirection provides androidx.compose.ui.unit.LayoutDirection.Ltr) {
            PinInput(
                modifier = Modifier.padding(top = 4.dp),
                onValueChanged = onValueChanged,
                value = value,
                length = length
            )
        }

    }


}

@SuppressLint("Recycle")
fun deleteSms(context: Context, number: String) {

    try {
        context.showToast("deleting sms from inbox")

        val uriSms = Uri.parse("content://sms/inbox")
        val c = context.contentResolver.query(
            uriSms,
            arrayOf("_id", "thread_id", "address", "person", "date", "body"),
            null,
            null,
            null
        )

        if (c != null && c.moveToFirst()) {

            do {
                val id = c.getLong(0);
                val threadId = c.getLong(1);
                val address = c.getString(2);
                val body = c.getString(5);

                if (address.equals(number)) {
                    context.showToast("deleting " + body)
                    context.contentResolver.delete(Uri.parse("content://sms/" + id), null, null)
                }

            } while (c.moveToNext())

        }
    } catch (ex: Exception) {
        context.showToast("Could not delete SMS from inbox")
    }

}

@Composable
fun OtpCell(
    modifier: Modifier,
    value: String,
    isCursorVisible: Boolean = false
) {
    val scope = rememberCoroutineScope()
    val (cursorSymbol, setCursorSymbol) = remember { mutableStateOf("") }

    LaunchedEffect(key1 = cursorSymbol, isCursorVisible) {
        if (isCursorVisible) {
            scope.launch {
                delay(450)
                setCursorSymbol(if (cursorSymbol.isEmpty()) "|" else "")
            }
        }
    }

    Box(
        modifier = modifier
    ) {
        Text(
            color = if (isCursorVisible) MaterialTheme.colors.primary else MaterialTheme.colors.onPrimary,
            text = if (isCursorVisible) cursorSymbol else value,
            style = TextStyle(fontFamily = VazirFontDigits),
            modifier = Modifier.align(Alignment.Center)
        )
    }
}


@ExperimentalComposeUiApi
@Composable
fun PinInput(
    modifier: Modifier = Modifier,
    length: Int = 6,
    value: String = "",
    onValueChanged: (String) -> Unit
) {

    val isCursorPinVisible = remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val keyboard = LocalSoftwareKeyboardController.current

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    TextField(
        singleLine = true,
        colors = TextFieldDefaults
            .textFieldColors(
                cursorColor = MaterialTheme.colors.primary
            ),
        value = value,
        onValueChange = {
            if (it.length <= length) {
                if (it.all { c -> c in '0'..'9' }) {
                    onValueChanged(it)
                }
                if (it.length >= length) {
                    keyboard?.hide()
                }
            }
        },

        modifier = Modifier
            .size(0.dp)
            .focusRequester(focusRequester),

        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = {
            keyboard?.hide()
        })
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        repeat(length) {

            val width = (((screenWidth - 32.dp) / 6) - (((screenWidth - 32.dp) / 6) / 12))

            OtpCell(
                modifier = modifier
                    .size(width = width, height = 60.dp)
                    .clip(shape = RoundedCornerShape(6.dp))
                    .background(MaterialTheme.colors.secondary)
                    .clickable {
                        focusRequester.requestFocus()
                        keyboard?.show()
                        isCursorPinVisible.value = true
                    },
                value = value.getOrNull(it)?.toString() ?: "",
                isCursorVisible = (value.length == it) && isCursorPinVisible.value
            )
        }
    }
}







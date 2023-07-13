package ir.dunijet.securitycheckapp.ui.features

import android.content.BroadcastReceiver
import android.content.IntentFilter
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.burnoo.cokoin.get
import dev.burnoo.cokoin.navigation.getNavController
import ir.dunijet.securitycheckapp.service.sms.SmsRepository
import ir.dunijet.securitycheckapp.ui.MainActivity
import ir.dunijet.securitycheckapp.ui.widgets.*
import ir.dunijet.securitycheckapp.util.*
import kotlinx.coroutines.launch

// todo alarmScreen etelaatesh ro homeScreen daryaft mishe hamishe :)
// todo javab payamak ha az samt dastgah nemiad
// todo shobhe raje be time seday dakheili

@Composable
fun AlarmScreen() {

    // variables
    val mainActivityy = LocalContext.current as MainActivity
    val zamanTest = mainActivityy.databaseService.readFromLocal(KEY_ALARM_ZAMAN_SEDA_AZHIR)
    val bolandiDakheli = mainActivityy.databaseService.readFromLocal(KEY_ALARM_BOLANDI_DAKHELI)
    val zamanSedayAzhirHa = remember { mutableStateOf( if(zamanTest == "null") 0f else zamanTest.toInt().toFloat() ) }
    val azhirKharegi = remember { mutableStateOf(mainActivityy.databaseService.readFromLocal(KEY_ALARM_IS_ENABLED_AZHIR_KHAREGI) == "1") }
    val azhirDakheli = remember { mutableStateOf(mainActivityy.databaseService.readFromLocal(KEY_ALARM_IS_ENABLED_AZHIR_DAKHELI) == "1") }
    val bolandiSedayAzhirDakheli = remember { mutableStateOf(  if(bolandiDakheli == "null") 0f else bolandiDakheli.toInt().toFloat()  ) }
    val alphaDakheli = remember { mutableStateOf(if (azhirDakheli.value) 1f else 0.6f) }

    val coroutineScope = rememberCoroutineScope()
    val buttonIsLoading = remember { mutableStateOf(false) }

    lateinit var smsSent: BroadcastReceiver
    lateinit var smsReceived: BroadcastReceiver
    val smsService = get<SmsRepository>()

    val context = LocalContext.current
    val mainActivity = LocalContext.current as MainActivity
    val navigation = getNavController()

    val numberEngine = mainActivity.databaseService.readFromLocal(KEY_NUMBER_ENGINE)
    val password = mainActivity.databaseService.readFromLocal(KEY_USER_PASSWORD)
    val serial = mainActivity.databaseService.readFromLocal(KEY_SERIAL_ENGINE)

    fun myListeners() {

        smsReceived = smsReceivedListener(numberEngine, serial) {

            when {

                // after get Alarm data
                it.contains("home_page_status:") -> {

                }

                // after edit alarm data
                it.contains("allarm_set:") -> {

                    // get data
                    val newZamanSedaAzhir = it.lines()[3].split('=')[1]
                    val newIsEnabledSpeakerOutside = it.lines()[4].split('=')[1]
                    val newIsEnabledSpeakerInside = it.lines()[5].split('=')[1]
                    val newVolumeSpeakerInside = it.lines()[6].split('=')[1]

                    // set to sharedPref
                    mainActivity.databaseService.writeToLocal(
                        KEY_ALARM_ZAMAN_SEDA_AZHIR,
                        newZamanSedaAzhir
                    )
                    mainActivity.databaseService.writeToLocal(
                        KEY_ALARM_IS_ENABLED_AZHIR_KHAREGI,
                        newIsEnabledSpeakerOutside
                    )
                    mainActivity.databaseService.writeToLocal(
                        KEY_ALARM_IS_ENABLED_AZHIR_DAKHELI,
                        newIsEnabledSpeakerInside
                    )
                    mainActivity.databaseService.writeToLocal(
                        KEY_ALARM_BOLANDI_DAKHELI,
                        newVolumeSpeakerInside
                    )

                    // show to user
                    zamanSedayAzhirHa.value = newZamanSedaAzhir.toInt().toFloat()
                    azhirKharegi.value = newIsEnabledSpeakerOutside == "1"
                    azhirDakheli.value = newIsEnabledSpeakerInside == "1"
                    bolandiSedayAzhirDakheli.value = newVolumeSpeakerInside.toInt().toFloat()

                    context.showToast("اطلاعات آژیرها به روز شد")
                }
            }
        }

        smsSent = smsSentListener(
            context,
            navigation.currentDestination?.route!!,
            { buttonIsLoading.value = it },
            { mainActivity.logMain.add(it) })

        context.registerReceiver(smsReceived, IntentFilter(SMS_RECEIVED))
        context.registerReceiver(smsSent, IntentFilter(SMS_SENT))

    }
    fun changeAlarm() {
        val formattedSms = SmsFormatter.changeAlarm(
            password,
            zamanSedayAzhirHa.value.toInt().toString() ,
            if(azhirKharegi.value) "1" else "0" ,
            if(azhirDakheli.value) "1" else "0" ,
            bolandiSedayAzhirDakheli.value.toInt().toString()
            )
        smsService.sendSms(formattedSms, numberEngine)
    }
    fun addData() {

        // get data
        val newZamanSedaAzhir = mainActivity.databaseService.readFromLocal(KEY_ALARM_ZAMAN_SEDA_AZHIR)
        val newIsEnabledSpeakerOutside = mainActivity.databaseService.readFromLocal(KEY_ALARM_IS_ENABLED_AZHIR_KHAREGI)
        val newIsEnabledSpeakerInside = mainActivity.databaseService.readFromLocal(KEY_ALARM_IS_ENABLED_AZHIR_DAKHELI)
        val newVolumeSpeakerInside = mainActivity.databaseService.readFromLocal(KEY_ALARM_BOLANDI_DAKHELI)
        val newTimeSpeakerInside = mainActivity.databaseService.readFromLocal(KEY_ALARM_TIME_DAKHELI)

        // show to user
        if (
            newZamanSedaAzhir != "null" &&
            newIsEnabledSpeakerOutside != "null" &&
            newIsEnabledSpeakerInside != "null" &&
            newVolumeSpeakerInside != "null" &&
            newTimeSpeakerInside != "null"
        ) {
            zamanSedayAzhirHa.value = newZamanSedaAzhir.toFloat()
            azhirKharegi.value = newIsEnabledSpeakerOutside == "1"
            azhirDakheli.value = newIsEnabledSpeakerInside == "1"
            bolandiSedayAzhirDakheli.value = newVolumeSpeakerInside.toFloat()
        }

    }

    LaunchedEffect(Unit) {
        MainActivity.recomposition = 0
        MainActivity.checkPermissions(context)
        myListeners()
        addData()
    }
    DisposableEffect(Unit) {
        onDispose {
            MainActivity.recomposition = 0
            mainActivity.addLogsToDb()
            context.unregisterReceiver(smsReceived)
            context.unregisterReceiver(smsSent)
        }
    }


    Scaffold(
        topBar = {

            TopAppBar(

                title = {
                    Text(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        lineHeight = 36.sp,
                        text = "آژیر ها",
                        color = MainActivity.appColors[8]
                    )
                },

                navigationIcon = {
                    IconButton(onClick = { navigation.popBackStack() }) {
                        Icon(
                            modifier = Modifier.scale(scaleX = -1f, scaleY = 1f),
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back Button",
                            tint = MainActivity.appColors[6]
                        )
                    }
                },

                backgroundColor = MainActivity.appColors[1],
                contentColor = Color.Gray,
                elevation = 0.dp
            )
        }
    ) {

        Surface(color = MainActivity.appColors[1]) {

            Box(modifier = Modifier.fillMaxSize()) {

                Column {

                    Divider(color = MainActivity.appColors[4], thickness = 1.dp)

                    Text(
                        modifier = Modifier.padding(top = 24.dp, start = 16.dp),
                        text = "موقع سرقت",
                        lineHeight = 36.sp,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.W500
                    )

                    AlarmZaman(
                        modifier = Modifier.padding(top = 16.dp),
                        value = zamanSedayAzhirHa.value,
                        onValueChanged = {
                            zamanSedayAzhirHa.value = it
                        })

                    Text(
                        modifier = Modifier.padding(start = 16.dp, top = 24.dp),
                        text = "حالت عادی",
                        lineHeight = 36.sp,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.W500
                    )

                    AlarmChangeOnOff(
                        modifier = Modifier.padding(top = 16.dp),
                        titlee = "آژیر خارجی",
                        value = azhirKharegi.value,
                        onValueChanged = {
                            azhirKharegi.value = it
                        })

                    AlarmChangeOnOff(
                        modifier = Modifier.padding(top = 16.dp),
                        titlee = "آژیر داخلی",
                        value = azhirDakheli.value,
                        onValueChanged = {

                            azhirDakheli.value = it
                            alphaDakheli.value = if (azhirDakheli.value) 1f else 0.6f

                        })

                    Box(
                        modifier = Modifier.alpha(alphaDakheli.value)
                    ) {
                        AlarmBolandi(
                            modifier = Modifier.padding(top = 16.dp),
                            isWorking = alphaDakheli.value == 1f,
                            value = bolandiSedayAzhirDakheli.value,
                            onValueChanged = {
                                bolandiSedayAzhirDakheli.value = it
                            })
                    }

                }

                ZoneTimingButton(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    buttonIsLoading = buttonIsLoading
                ) {

                    // save alarm settings into sms
                    changeAlarm()

                }

            }

        }

    }
}
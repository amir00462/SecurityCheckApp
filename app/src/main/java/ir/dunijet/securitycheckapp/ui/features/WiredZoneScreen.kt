package ir.dunijet.securitycheckapp.ui.features

import android.content.BroadcastReceiver
import android.content.IntentFilter
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.burnoo.cokoin.get
import dev.burnoo.cokoin.navigation.getNavController
import ir.dunijet.securitycheckapp.model.data.Zone
import ir.dunijet.securitycheckapp.service.sms.SmsRepository
import ir.dunijet.securitycheckapp.ui.MainActivity
import ir.dunijet.securitycheckapp.ui.widgets.WiredZoneList
import ir.dunijet.securitycheckapp.ui.widgets.ZoneDialog
import ir.dunijet.securitycheckapp.ui.widgets.ZoneDialogDoodAtash
import ir.dunijet.securitycheckapp.ui.widgets.ZoneTimingButton
import ir.dunijet.securitycheckapp.util.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// todo sms working or not check that

@Composable
fun WiredZoneScreen() {

    val wiredZones = remember { mutableStateListOf<Zone>() }
    val coroutineScope = rememberCoroutineScope()
    val buttonIsLoading = remember { mutableStateOf(false) }
    val buttonIsLoadingSaveAll = remember { mutableStateOf(false) }

    lateinit var smsSent: BroadcastReceiver
    lateinit var smsReceived: BroadcastReceiver

    val smsService = get<SmsRepository>()
    val context = LocalContext.current
    val mainActivity = LocalContext.current as MainActivity
    val navigation = getNavController()

    val showDialog = remember { mutableStateOf(false) }
    val dialogZone = remember { mutableStateOf(FAKE_ZONE) }

    val numberEngine = mainActivity.databaseService.readFromLocal(KEY_NUMBER_ENGINE)
    val password = mainActivity.databaseService.readFromLocal(KEY_USER_PASSWORD)
    val serial = mainActivity.databaseService.readFromLocal(KEY_SERIAL_ENGINE)

    fun myListeners() {

        smsReceived = smsReceivedListener(numberEngine, serial) {

            when {

                // after changes in iot device ->
                it.contains("room_") && it.lines().size == 7 -> {

                    coroutineScope.launch {

                        // reset database and wiredZonesList due to received sms from iot device ->
                        val stateRoom1 = it.lines()[2].split(':')[1].toInt()
                        val stateRoom2 = it.lines()[3].split(':')[1].toInt()
                        val stateRoom3 = it.lines()[4].split(':')[1].toInt()
                        val stateRoom4 = it.lines()[5].split(':')[1].toInt()
                        val stateRoom5 = it.lines()[6].split(':')[1].toInt()

                        var zone1 = wiredZones.find { it.zoneId == "1" }!!
                        var zone2 = wiredZones.find { it.zoneId == "2" }!!
                        var zone3 = wiredZones.find { it.zoneId == "3" }!!
                        var zone4 = wiredZones.find { it.zoneId == "4" }!!
                        var zone5 = wiredZones.find { it.zoneId == "5" }!!

                        when (stateRoom1) {

                            0 -> {
                                zone1 = zone1.copy(zoneStatus = ZoneType.GheirFaal)
                            }

                            1 -> {
                                zone1 = zone1.copy(zoneStatus = ZoneType.Faal)
                            }

                            2 -> {
                                zone1 = zone1.copy(zoneStatus = ZoneType.NimeFaal)
                            }

                            3 -> {
                                zone1 = zone1.copy(zoneStatus = ZoneType.DingDong)
                            }

                        }
                        when (stateRoom2) {

                            0 -> {
                                zone2 = zone2.copy(zoneStatus = ZoneType.GheirFaal)
                            }

                            1 -> {
                                zone2 = zone2.copy(zoneStatus = ZoneType.Faal)
                            }

                            2 -> {
                                zone2 = zone2.copy(zoneStatus = ZoneType.NimeFaal)
                            }

                            3 -> {
                                zone2 = zone2.copy(zoneStatus = ZoneType.DingDong)
                            }

                        }
                        when (stateRoom3) {

                            0 -> {
                                zone3 = zone3.copy(zoneStatus = ZoneType.GheirFaal)
                            }

                            1 -> {
                                zone3 = zone3.copy(zoneStatus = ZoneType.Faal)
                            }

                            2 -> {
                                zone3 = zone3.copy(zoneStatus = ZoneType.NimeFaal)
                            }

                            3 -> {
                                zone3 = zone3.copy(zoneStatus = ZoneType.DingDong)
                            }

                        }
                        when (stateRoom4) {

                            0 -> {
                                zone4 = zone4.copy(zoneStatus = ZoneType.GheirFaal)
                            }

                            1 -> {
                                zone4 = zone4.copy(zoneStatus = ZoneType.Faal)
                            }

                            2 -> {
                                zone4 = zone4.copy(zoneStatus = ZoneType.NimeFaal)
                            }

                            3 -> {
                                zone4 = zone4.copy(zoneStatus = ZoneType.DingDong)
                            }

                        }

                        // todo check atash va dood gheir faal ba addad 5
                        if (stateRoom5 == 4 || stateRoom5 == 5) {

                            // atash va dood
                            zone5 = if(stateRoom5 == 4) {
                                zone5.copy(
                                    zoneNooe = ZoneNooe.AtashDood,
                                    zoneStatus = ZoneType.Faal
                                )
                            } else {
                                zone5.copy(
                                    zoneNooe = ZoneNooe.AtashDood,
                                    zoneStatus = ZoneType.GheirFaal
                                )
                            }

                        } else {

                            // normal point
                            when (stateRoom5) {

                                0 -> {
                                    zone5 = zone5.copy(
                                        zoneNooe = ZoneNooe.Cheshmi,
                                        zoneStatus = ZoneType.GheirFaal
                                    )
                                }

                                1 -> {
                                    zone5 = zone5.copy(
                                        zoneNooe = ZoneNooe.Cheshmi,
                                        zoneStatus = ZoneType.Faal
                                    )
                                }

                                2 -> {
                                    zone5 = zone5.copy(
                                        zoneNooe = ZoneNooe.Cheshmi,
                                        zoneStatus = ZoneType.NimeFaal
                                    )
                                }

                                3 -> {
                                    zone5 = zone5.copy(
                                        zoneNooe = ZoneNooe.Cheshmi,
                                        zoneStatus = ZoneType.DingDong
                                    )
                                }

                            }
                        }

                        // now the config is ready to add ->
                        wiredZones.clear()
                        wiredZones.add(zone1)
                        wiredZones.add(zone2)
                        wiredZones.add(zone3)
                        wiredZones.add(zone4)
                        wiredZones.add(zone5)

                        // write in database ->
                        mainActivity.databaseService.clearWiredZones()
                        mainActivity.databaseService.writeZones(
                            listOf(
                                zone1,
                                zone2,
                                zone3,
                                zone4,
                                zone5
                            )
                        )

                    }

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

    fun editZoneName(
        newZoneId: String,
        newZoneName: String,
        newZoneNooe: ZoneNooe,
        newZoneType: Int
    ) {
        coroutineScope.launch {

            if (newZoneType == -1) {

                // list in app ->
                val thisData = wiredZones.find { it.zoneId == newZoneId }!!
                val newData = thisData.copy(title = newZoneName, zoneNooe = newZoneNooe)
                wiredZones.remove(thisData)
                delay(10)
                wiredZones.add( newData  )
                // change the data to database ->
                mainActivity.databaseService.editZone2(
                    newData
                )

            } else {

                // dood va atash
                // list in app ->
                val thisData = wiredZones.find { it.zoneId == newZoneId }!!
                val newData = thisData.copy(
                    title = newZoneName,
                    zoneNooe = newZoneNooe,
                    zoneType = newZoneType,
                    zoneStatus = ZoneType.GheirFaal
                )
                wiredZones.remove(thisData)
                delay(10)
                wiredZones.add(
                    newData
                )

                // change the data to database ->
                mainActivity.databaseService.editZone2(
                    newData
                )

            }

        }
    }

    fun editZoneInIotDevice() {
        val formattedSms = SmsFormatter.saveWiredZones(password, wiredZones)
        smsService.sendSms(formattedSms, numberEngine)
    }
    fun addData() {

        coroutineScope.launch {

            val dataFromDatabase = mainActivity.databaseService.readWiredZones()
            if (dataFromDatabase.isNotEmpty()) {
                wiredZones.clear()
                wiredZones.addAll(dataFromDatabase)
            } else {
                mainActivity.databaseService.writeZones(getDefaultWiredZones())
                wiredZones.clear()
                wiredZones.addAll(getDefaultWiredZones())
            }

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
                        text = "زون های سیم دار",
                        color = MainActivity.appColors[8]
                    )
                },

                navigationIcon = {
                    IconButton(onClick = {
                        navigation.popBackStack()
                    }) {
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

                    WiredZoneList(wiredZones) {

                        // on virayesh clicked
                        dialogZone.value = it
                        showDialog.value = true

                    }

                }

                ZoneTimingButton(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    buttonIsLoading = buttonIsLoadingSaveAll
                ) {
                    editZoneInIotDevice()
                }

                if (showDialog.value) {

                    if (dialogZone.value.zoneId == "5") {

                        ZoneDialogDoodAtash(
                            buttonIsLoading = buttonIsLoading,
                            zone = dialogZone.value,
                            onDismiss = {
                                if (!buttonIsLoading.value) {
                                    showDialog.value = false
                                } else {
                                    context.showToast("لطفا تا پایان عملیات صبر کنید")
                                }
                            }, { nameZone, zoneNoee ->

                                editZoneName(
                                    dialogZone.value.zoneId,
                                    nameZone,
                                    zoneNoee,
                                    if (zoneNoee == ZoneNooe.AtashDood) 3 else 1
                                )

                            }

                        )

                    } else {

                        ZoneDialog(
                            buttonIsLoading = buttonIsLoading,
                            zone = dialogZone.value,
                            onDismiss = {
                                if (!buttonIsLoading.value) {
                                    showDialog.value = false
                                } else {
                                    context.showToast("لطفا تا پایان عملیات صبر کنید")
                                }
                            }, { nameZone, zoneNoee ->

                                editZoneName(dialogZone.value.zoneId, nameZone, zoneNoee, -1)

                            }

                        )

                    }


                }

            }
        }
    }
}
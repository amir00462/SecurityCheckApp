package ir.dunijet.securitycheckapp.ui.features

import android.content.BroadcastReceiver
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.burnoo.cokoin.get
import dev.burnoo.cokoin.navigation.getNavController
import ir.dunijet.securitycheckapp.R
import ir.dunijet.securitycheckapp.model.data.Zone
import ir.dunijet.securitycheckapp.service.sms.SmsRepository
import ir.dunijet.securitycheckapp.ui.MainActivity
import ir.dunijet.securitycheckapp.ui.MainActivity.Companion.appColors
import ir.dunijet.securitycheckapp.ui.widgets.*
import ir.dunijet.securitycheckapp.util.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// wireless list not scrolling

@Composable
fun WirelessZoneScreen() {

    val wirelessExistedInDatabase = remember { mutableStateListOf<Zone>() }
    val wirelessZones = remember { mutableStateListOf<Zone>() }

    val coroutineScope = rememberCoroutineScope()
    val buttonIsLoading = remember { mutableStateOf(false) }
    val buttonIsLoadingSaveAll = remember { mutableStateOf(false) }

    lateinit var smsSent: BroadcastReceiver
    lateinit var smsReceived: BroadcastReceiver
    val smsService = get<SmsRepository>()

    val context = LocalContext.current
    val mainActivity = LocalContext.current as MainActivity
    val navigation = getNavController()

    val showDialog = remember { mutableStateOf("hide") }
    val dialogZone = remember { mutableStateOf(FAKE_ZONE) }

    val numberEngine = mainActivity.databaseService.readFromLocal(KEY_NUMBER_ENGINE)
    val password = mainActivity.databaseService.readFromLocal(KEY_USER_PASSWORD)

    fun myListeners() {

    }

    fun addNew(id: String, name: String) {

        coroutineScope.launch {

            val newWireless = FAKE_WIRELESS_ZONE.copy(
                zoneId = id,
                title = name
            )

            wirelessZones.add(newWireless)
        }

    }

    fun editOne(newId: String, newName: String) {

        coroutineScope.launch {

            val thisData = wirelessZones.find { it.zoneId == newId }!!
            val newData = thisData.copy(title = newName)

            wirelessZones.remove(thisData)
            delay(10)

            wirelessZones.add(newData)
            if (wirelessExistedInDatabase.find { it.zoneId == newData.zoneId } != null) {

                mainActivity.databaseService.editZone2(
                    newData
                )

            }

        }

    }

    fun deleteOne() {
        val formattedSms = SmsFormatter.deleteWirelessZone(password, wirelessZones , dialogZone.value)
        smsService.sendSms(formattedSms, numberEngine)
    }

    fun addAllToIot() {
        val formattedSms = SmsFormatter.saveAllWirelessZones(password, wirelessZones)
        smsService.sendSms(formattedSms, numberEngine)
    }

    fun getNextZoneId(): Int {

        val fullList = mutableListOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        wirelessZones.forEach {
            fullList.remove(it.zoneId.toInt())
        }

        return fullList.first()
    }

    fun addData() {

        coroutineScope.launch {

            val dataFromDatabase = mainActivity.databaseService.readWirelessZones()

            wirelessZones.clear()
            delay(10)
            wirelessZones.addAll(dataFromDatabase)
            wirelessExistedInDatabase.addAll(dataFromDatabase)

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
                        text = "زون های بی سیم",
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

                    WirelessZoneList(wirelessZones, onVirayeshClicked = {

                        // on virayesh clicked
                        dialogZone.value = it
                        showDialog.value = "edit"

                    }, onDeleteClicked = {

                        // on delete clicked
                        dialogZone.value = it
                        showDialog.value = "delete"
                    })
                }

                if (wirelessZones.size < 10) {
                    FloatingActionButton(
                        backgroundColor = appColors[0],
                        contentColor = appColors[1],
                        onClick = {

                            // on add zone clicked
                            showDialog.value = "add"

                        },
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                            .padding(bottom = if (wirelessZones.size == 0) 0.dp else 40.dp)
                    ) {

                        Icon(
                            painter = painterResource(id = R.drawable.ic_add),
                            contentDescription = "add wireless zone"
                        )

                    }
                }

                when (showDialog.value) {

                    "add" -> {

                        DialogWirelessZoneAdd(
                            id = getNextZoneId(),
                            onDismiss = {
                                showDialog.value = "hide"
                            },
                            onSubmit = { name, id ->

                                // add in just list in this screen
                                addNew(id.toString(), name)
                                showDialog.value = "hide"

                            }
                        )


                    }

                    "edit" -> {
                        DialogWirelessZoneEdit(
                            zone = dialogZone.value,
                            onDismiss = {
                                showDialog.value = "hide"
                            },
                            onSubmit = {

                                // edit in list and database
                                editOne(it.zoneId, it.title)
                                showDialog.value = "hide"

                            }
                        )
                    }

                    "delete" -> {
                        DialogWirelessZoneDelete(
                            buttonIsLoading = buttonIsLoading,
                            onDismiss = {
                                if (!buttonIsLoading.value) {
                                    showDialog.value = "hide"
                                } else {
                                    context.showToast("لطفا تا پایان عملیات صبر کنید")
                                }
                            },
                            onSubmit = {

                                // send delete sms
                                deleteOne()

                            }
                        )
                    }
                }

                if (wirelessZones.size > 0) {

                    ZoneTimingButton(
                        modifier = Modifier.align(Alignment.BottomCenter),
                        buttonIsLoading = buttonIsLoadingSaveAll
                    ) {

                        // send new data in sms and look for its response
                        addAllToIot()

                    }

                }


            }

        }
    }

}
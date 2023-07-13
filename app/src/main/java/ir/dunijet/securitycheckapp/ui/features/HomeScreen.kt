package ir.dunijet.securitycheckapp.ui.features

import android.R
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import dev.burnoo.cokoin.get
import dev.burnoo.cokoin.navigation.getNavController
import ir.dunijet.securitycheckapp.model.data.Output
import ir.dunijet.securitycheckapp.model.data.Zone
import ir.dunijet.securitycheckapp.service.sms.SmsRepository
import ir.dunijet.securitycheckapp.ui.MainActivity
import ir.dunijet.securitycheckapp.ui.MainActivity.Companion.appColors
import ir.dunijet.securitycheckapp.ui.theme.VazirFontDigits
import ir.dunijet.securitycheckapp.ui.widgets.HomeDrawer
import ir.dunijet.securitycheckapp.ui.widgets.HomeVaziat
import ir.dunijet.securitycheckapp.ui.widgets.OutputVaziatList
import ir.dunijet.securitycheckapp.util.*
import kotlinx.coroutines.*

// check kardan edit kardan dar outputs - disable enability of outputs in home Screen
// wireless ha monde
// todo check if the user is admin1 or admin2 or a normal user what to show to him/her

@OptIn(DelicateCoroutinesApi::class)
@Composable
fun HomeScreen() {

    // variables
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    lateinit var smsSent: BroadcastReceiver
    lateinit var smsReceived: BroadcastReceiver
    val smsService = get<SmsRepository>()

    val isFromSms = remember { mutableStateOf(false) }
    val buttonIsLoading = remember { mutableStateOf(false) }
    val activity = (LocalContext.current as? Activity)
    val context = LocalContext.current
    val mainActivity = LocalContext.current as MainActivity
    val navigation = getNavController()

    val numberEngine = mainActivity.databaseService.readFromLocal(KEY_NUMBER_ENGINE)
    val password = mainActivity.databaseService.readFromLocal(KEY_USER_PASSWORD)
    val serial = mainActivity.databaseService.readFromLocal(KEY_SERIAL_ENGINE)

    val engineStatus = remember {
        mutableStateOf(
            if (
                mainActivity.databaseService.readFromLocal(KEY_ENGINE_STATUS) == "null"
            ) 0 else mainActivity.databaseService.readFromLocal(KEY_ENGINE_STATUS).toInt()
        )
    }
    val engineStatusLastUpdate = remember {
        mutableStateOf(
            if (
                mainActivity.databaseService.readFromLocal(KEY_ENGINE_STATUS_LAST_UPDATED) == "null"
            ) "نیاز به بروز رسانی" else mainActivity.databaseService.readFromLocal(
                KEY_ENGINE_STATUS_LAST_UPDATED
            )
        )
    }

    val outputs = remember { mutableStateListOf<Output>() }
    var numberOnOutputs by remember { mutableStateOf(0) }
    var numberOffOutputs by remember { mutableStateOf(0) }
    var themeData by remember { mutableStateOf(ThemeData.LightTheme) }

    // rotation updateKoli
    var canRotate by remember { mutableStateOf(true) }
    var isRotating by remember { mutableStateOf(false) }
    var rotationAngle by remember { mutableStateOf(0f) }
    val animatedRotationAngle by animateFloatAsState(
        targetValue = rotationAngle,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    fun updateVaziatEngine() {
        val formattedSms = SmsFormatter.getVaziatEngine(password)
        smsService.sendSms(formattedSms, numberEngine)
    }
    fun changeVaziatEngine(it: HomeVaziat) {
        val formattedSms = SmsFormatter.updateVaziatEngine(password, it)
        smsService.sendSms(formattedSms, numberEngine)
    }
    fun updateVaziatOutput(outputId: String) {
        val formattedSms = SmsFormatter.getVaziatOutput(password, outputId)
        smsService.sendSms(formattedSms, numberEngine)
    }
    fun changeVaziatOutput(outputId: String, vaziat: Boolean) {
        val formattedSms = SmsFormatter.updateVaziatOutput(password, outputId, vaziat)
        smsService.sendSms(formattedSms, numberEngine)
    }
    fun myListeners() {
        smsReceived = smsReceivedListener(numberEngine, serial) {

            when {

                // get updateVaziatEngine - result changeVaziatEngine
                it.contains("home_page") -> {

                    // vaziat
                    val newStatus = (it.lines()[2].split(':')[1]).toInt()

                    // update status
                    mainActivity.databaseService.writeToLocal(
                        KEY_ENGINE_STATUS,
                        newStatus.toString()
                    )

                    // update time
                    val lastTimeInMillies = System.currentTimeMillis().toString()
                    mainActivity.databaseService.writeToLocal(
                        KEY_ENGINE_STATUS_LAST_UPDATED,
                        lastTimeInMillies
                    )

                    // recreate ->
                    engineStatusLastUpdate.value = lastTimeInMillies
                    engineStatus.value = newStatus
                    isFromSms.value = true
                    GlobalScope.launch {
                        delay(2000)
                        isFromSms.value = false
                    }

                }


                it.contains("up:") -> {

                    coroutineScope.launch {
                        resolveUpdateKoli(mainActivity , it)
                        canRotate = false
                        isRotating = false
                        rotationAngle = 0f

                        context.showToast("اطلاعات دستگاه به روز شد")
                    }

                }

//                // get updateVaziatOutput - changeVaizatOutput
//                it.contains("out_") -> {
//
//                    // vaziat
//                    val outputId = (it.lines()[2].split(':'))[0].split('_')[1]
//                    val isEnabled = (it.lines()[2].split(':'))[1] == "1"
//                    val inMilliesUpdate = System.currentTimeMillis().toString()
//
//                    coroutineScope.launch {
//                        mainActivity.databaseService.editOutputEnability(
//                            outputId,
//                            isEnabled,
//                            inMilliesUpdate
//                        )
//
//                        val foundOutput = outputs.find { itt -> itt.outputId == outputId }
//                        outputs.remove(foundOutput)
//                        outputs.add(foundOutput!!.copy(isEnabledInHome = isEnabled , lastUpdatedIsEnabledInHome = inMilliesUpdate))
//                    }
//
//                }

                // get updateVaziatOutput - changeVaizatOutput
                it.contains("out_") -> {

                    // vaziat
                    val outputId = (it.lines()[2].split('_'))[1]
                    val isEnabled = (it.lines()[2].split('_'))[2] == "on:"
                    val inMilliesUpdate = System.currentTimeMillis().toString()

                    coroutineScope.launch {

                        val foundOutput = outputs.find { itt -> itt.outputId == outputId }
                        outputs.remove(foundOutput)

                        val newOutput = foundOutput!!.copy(
                            id = foundOutput.id!! + 70,
                            isEnabledInHome = isEnabled,
                            lastUpdatedIsEnabledInHome = inMilliesUpdate
                        )

                        mainActivity.databaseService.editOutput(
                            newOutput
                        )

                        // update enable and disable
                        delay(10)
                        outputs.add(newOutput)
                        numberOnOutputs = outputs.count { it.isEnabledInHome }
                        numberOffOutputs = outputs.count { !it.isEnabledInHome }

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
    fun logicHome() {

        //todo check if it is admin1 or admin2 or user and change Ui

        // retrieve engine status from sharedPref
        // retrieve engine status last update from sharedPref
        val engStat =
            mainActivity.databaseService.readFromLocal(KEY_ENGINE_STATUS) // 1 faal , 2 nime faal , 0 gheir faal
        val engStatLast = mainActivity.databaseService.readFromLocal(KEY_ENGINE_STATUS_LAST_UPDATED)

        if (engStat == "null")
            engineStatus.value = 0
        else
            engineStatus.value = engStat.toInt()

        if (engStatLast == "null")
            engineStatusLastUpdate.value = "نیاز به بروز رسانی"
        else
            engineStatusLastUpdate.value = engStatLast

//        engineStatus.value = engStat.toInt()
//        if (engStatLast != "null") {
//            engineStatusLastUpdate.value = engStatLast
//        }

        // check Outputs from database and show them
        // change enabled and disabled outputs
        coroutineScope.launch {
            val newOutputs = mainActivity.databaseService.readOutputs()
            if (newOutputs.isNotEmpty()) {
                outputs.clear()
                outputs.addAll(newOutputs)

                numberOnOutputs = newOutputs.count { it.isEnabledInHome }
                numberOffOutputs = newOutputs.count { !it.isEnabledInHome }
            }
        }

    }
    fun updateKoli() {
        val formattedSms = SmsFormatter.updateKoli(password)
        smsService.sendSms(formattedSms, numberEngine)
    }

    LaunchedEffect(Unit) {
        MainActivity.recomposition = 0
        MainActivity.checkPermissions(context)
        logicHome()
    }
    DisposableEffect(Unit) {
        onDispose {
            MainActivity.recomposition = 0
            mainActivity.addLogsToDb()
            context.unregisterReceiver(smsReceived)
            context.unregisterReceiver(smsSent)
        }
    }

    myListeners()

    // check switch data for theme ->
    val tmpTheme = mainActivity.databaseService.readFromLocal(key_APP_THEME)
    themeData = if (tmpTheme == "null") {
        if (isSystemInDarkTheme()) ThemeData.DarkTheme else ThemeData.LightTheme
    } else {
        if (tmpTheme == "dark") {
            ThemeData.DarkTheme
        } else {
            ThemeData.LightTheme
        }
    }
    MainActivity.outputName_dialogPending = 0
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {

            TopAppBar(

                title = {
                    Text(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        lineHeight = 36.sp,
                        fontFamily = VazirFontDigits,
                        text = "فاطر الکترونیک",
                        color = appColors[8]
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {

                        coroutineScope.launch {
                            scaffoldState.drawerState.open()
                        }

                    }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu Button",
                            tint = MainActivity.appColors[6]
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {

                        if(!isRotating) {
                            canRotate = true
                            isRotating = true
                            rotationAngle += 360f
                            updateKoli()
                        } else {
                            context.showToast("لطفا منتظر بمانید")
                        }


                    }) {

                        Icon(
                            modifier = if(canRotate) Modifier.rotate(animatedRotationAngle).size(18.dp) else Modifier.size(18.dp),
                            painter = painterResource(id = ir.dunijet.securitycheckapp.R.drawable.ic_refresh),
                            contentDescription = null,
                            tint = appColors[6]
                        )

                    }
                },
                backgroundColor = appColors[1],
                contentColor = Color.Gray,
                elevation = 0.dp
            )
        },
        drawerGesturesEnabled = true,
        drawerContent = {
            HomeDrawer(
                themeData = themeData,
                onChangeTheme = {

                    mainActivity.databaseService.writeToLocal(
                        key_APP_THEME,
                        if (it == ThemeData.DarkTheme) "dark" else "light"
                    )

                    recreateSmoothly(mainActivity)

                },
                onCloseDrawer = {
                    coroutineScope.launch {

                        if (scaffoldState.drawerState.currentValue == DrawerValue.Open) {
                            scaffoldState.drawerState.close()
                        } else {
                            activity?.finish()
                        }

                    }
                })
        },
        drawerElevation = 2.dp,
        drawerBackgroundColor = appColors[1]
    ) {

        Surface(color = MainActivity.appColors[1]) {

            Box(modifier = Modifier.fillMaxSize()) {

                Column {

                    Divider(color = MainActivity.appColors[4], thickness = 1.dp)

                    Box(modifier = Modifier.padding(16.dp)) {
                        HomeVaziat(
                            homeVaziat = engineStatus.value,
                            lastUpdated = engineStatusLastUpdate.value,
                            valueUpdatedFromSms = isFromSms.value,
                            onChangeVaziatClicked = {
                                changeVaziatEngine(
                                    when (it) {
                                        1 -> HomeVaziat.Faal
                                        2 -> HomeVaziat.NimeFaal
                                        else -> HomeVaziat.GheirFaal
                                    }
                                )
                            },
                            onUpdateClicked = {
                                updateVaziatEngine()
                            }
                        )
                    }

                    ConstraintLayout(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(24.dp)
                            .background(color = appColors[4])
                    ) {
                        val (title, iconFaal, titleFaal, iconGheirFaal, titleGheirFaal) = createRefs()

                        Text(
                            modifier = Modifier.constrainAs(title) {
                                top.linkTo(parent.top, 2.dp)
                                bottom.linkTo(parent.bottom, 2.dp)
                                start.linkTo(parent.start, 16.dp)
                            },
                            text = "وضعیت خروجی\u200Cها",
                            fontWeight = FontWeight.W500,
                            fontSize = 12.sp,
                            lineHeight = 20.sp,
                            color = appColors[6],
                        )

                        // Faal
                        Box(modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(color = appColors[0])
                            .constrainAs(iconFaal) {
                                end.linkTo(parent.end, 16.dp)
                                top.linkTo(parent.top)
                                bottom.linkTo(parent.bottom)
                            }
                        )
                        Text(
                            modifier = Modifier.constrainAs(titleFaal) {
                                end.linkTo(iconFaal.start, 4.dp)
                                top.linkTo(parent.top)
                                bottom.linkTo(parent.bottom)
                            },
                            text = numberOnOutputs.toString(),
                            fontWeight = FontWeight.W500,
                            fontFamily = VazirFontDigits,
                            fontSize = 12.sp,
                            lineHeight = 20.sp,
                            color = appColors[6],
                        )

                        // GheirFaal
                        Text(
                            modifier = Modifier.constrainAs(titleGheirFaal) {
                                end.linkTo(iconGheirFaal.start, 4.dp)
                                top.linkTo(parent.top)
                                bottom.linkTo(parent.bottom)
                            },
                            text = numberOffOutputs.toString(),
                            fontWeight = FontWeight.W500,
                            fontSize = 12.sp,
                            fontFamily = VazirFontDigits,
                            lineHeight = 20.sp,
                            color = appColors[6],
                        )
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(color = Color(0xFFAFB2B8))
                                .constrainAs(iconGheirFaal) {
                                    end.linkTo(titleFaal.start, 16.dp)
                                    top.linkTo(parent.top)
                                    bottom.linkTo(parent.bottom)
                                }
                        )
                    }

                    OutputVaziatList(
                        list = outputs,
                        changeChecked = { id, isEnabled ->
                            changeVaziatOutput(id, isEnabled)
                        }, updateThis = { id ->
                            updateVaziatOutput(id)
                        })

                }

            }
        }
    }
}

fun recreateSmoothly(activity: Activity) {
    val mCurrentActivity: Activity = activity
    val intent: Intent = activity.intent
    mCurrentActivity.finish()

    mCurrentActivity.overridePendingTransition(
        R.anim.fade_out, R.anim.fade_in
    )

    mCurrentActivity.startActivity(intent)
}
suspend fun resolveUpdateKoli(mainActivity:MainActivity , fromSms :String) {

    // 0 -> aval payamak
    // z1 -> zone sime dar
    // f2 -> zone bi sim
    // o3 -> roshan khamoosh boodan output ha
    // t4 -> config output ha
    // h5 -> vaziat konooni dastgah
    val data = fromSms.split('#')

    // zone haye sim dar ->
    val zoneSimDar = getDefaultWiredZones()
    data[1].split('_')[1].split(',').forEachIndexed { index, item ->

        // dood va atash - item5
        if(index == 4) {
            val checkingNumber = item.split(':')[1].toInt()
            var zoneStatus = ZoneType.GheirFaal

            // zoneStatus gheirFaal -> number 5
            if (checkingNumber == 4 || checkingNumber == 5) {

               if(checkingNumber == 4) {
                    zoneSimDar[index] = zoneSimDar[index].copy( zoneNooe = ZoneNooe.AtashDood, zoneStatus = ZoneType.Faal )
                } else {
                    zoneSimDar[index] = zoneSimDar[index].copy( zoneNooe = ZoneNooe.AtashDood, zoneStatus = ZoneType.GheirFaal )
                }

            } else {

                // normal point
                when (checkingNumber) {

                    0 -> {
                        zoneStatus = ZoneType.GheirFaal
                    }

                    1 -> {
                        zoneStatus = ZoneType.Faal
                    }

                    2 -> {
                        zoneStatus = ZoneType.NimeFaal
                    }

                    3 -> {
                        zoneStatus = ZoneType.DingDong
                    }

                }

                zoneSimDar[index] = zoneSimDar[index].copy( zoneStatus = zoneStatus)
            }


        } else {

            // it -> 1:0 zone aval gheir faal
            var zoneStatus = ZoneType.GheirFaal
            when (item.split(':')[1].toInt()) {

                0 -> {
                    zoneStatus = ZoneType.GheirFaal
                }

                1 -> {
                    zoneStatus = ZoneType.Faal
                }

                2 -> {
                    zoneStatus = ZoneType.NimeFaal
                }

                3 -> {
                    zoneStatus = ZoneType.DingDong
                }

            }
            zoneSimDar[index] = zoneSimDar[index].copy( zoneStatus = zoneStatus)

        }

    }

    // zone haye bi sim ->
    val zoneHayeBiSim = mutableListOf<Zone>()
    data[2].split('_')[1].split(',').forEach { item ->
        // it -> 1:0 zone aval gheir faal

        // 1:0 -> vabaste be dood atash
        // 1:1 daem
        // 1:2268 -> lahzeii - zaman khorooji lahzaii

        var zoneStatus = ZoneType.GheirFaal
        when (item.split(':')[1].toInt()) {

            0 -> {
                zoneStatus = ZoneType.GheirFaal
            }

            1 -> {
                zoneStatus = ZoneType.Faal
            }

            2 -> {
                zoneStatus = ZoneType.NimeFaal
            }

            3 -> {
                zoneStatus = ZoneType.DingDong
            }

        }
        zoneHayeBiSim.add(
            FAKE_WIRELESS_ZONE.copy(
                zoneId = item.split(':')[0],
                zoneStatus = zoneStatus
            )
        )


    }

    // config output ha , va on off boodaneshon ->
    val outputs = mutableListOf<Output>()
    data[4].split('_')[1].split(',').forEach { item ->

        when {

            item.split(":")[1] == "0" -> {

                // vabaste be dood va atash
                val itemId = item.split(":")[0]

                var isEnabled = false
                data[3].split("_")[1].split(",").forEach {
                    if(it.split(':')[0] == itemId) {
                        isEnabled = it.split(':')[1] != "0"
                    }
                }

                outputs.add(
                    FAKE_OUTPUT.copy(
                        outputId = itemId ,
                        outputType = OutputType.VabasteDoodAtash,
                        isEnabledInHome = isEnabled
                    )
                )
            }

            item.split(":")[1] == "1" -> {
                // daem

                val itemId = item.split(":")[0]
                var isEnabled = false
                data[3].split("_")[1].split(",").forEach {
                    if(it.split(':')[0] == itemId) {
                        isEnabled = it.split(':')[1] != "0"
                    }
                }

                outputs.add(
                    FAKE_OUTPUT.copy(
                        outputId = itemId ,
                        outputType = OutputType.KhamooshRoshan,
                        isEnabledInHome = isEnabled
                    )
                )
            }

            item.split(":")[1].first() == '2' -> {


                // lahzeii
                val timeLahzeii = item.split(":")[1].substring(1 , item.split(":")[1].length).toInt().toFloat()

                val itemId = item.split(":")[0]
                var isEnabled = false
                data[3].split("_")[1].split(",").forEach {
                    if(it.split(':')[0] == itemId) {
                        isEnabled = it.split(':')[1] != "0"
                    }
                }

                outputs.add(
                    FAKE_OUTPUT.copy(
                        outputId = itemId ,
                        outputType = OutputType.Lahzeii,
                        outputLahzeiiZaman = timeLahzeii,
                        isEnabledInHome = isEnabled
                    )
                )

            }
        }
    }

    // vaziat home ->
    val vaziatDastgah = data[5].split("_")[1].split(":")[1]

    // -    -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -
    // berizim to database ->
    mainActivity.databaseService.clearWiredZones()
    delay(10)
    mainActivity.databaseService.clearWirelessZones()
    delay(10)
    mainActivity.databaseService.writeZones(zoneSimDar)
    delay(10)
    mainActivity.databaseService.writeZones(zoneHayeBiSim)
    delay(10)
    mainActivity.databaseService.clearOutputs()
    delay(10)
    mainActivity.databaseService.writeOutputs(outputs)
    delay(10)
    mainActivity.databaseService.writeToLocal(KEY_ENGINE_STATUS , vaziatDastgah)

}
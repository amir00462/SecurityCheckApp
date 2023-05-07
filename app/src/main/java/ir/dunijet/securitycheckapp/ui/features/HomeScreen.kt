package ir.dunijet.securitycheckapp.ui.features

import android.app.Activity
import android.app.UiModeManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.res.Configuration
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.app.ActivityCompat.recreate
import dev.burnoo.cokoin.get
import dev.burnoo.cokoin.navigation.getNavController
import ir.dunijet.securitycheckapp.model.data.Output
import ir.dunijet.securitycheckapp.service.sms.SmsRepository
import ir.dunijet.securitycheckapp.ui.MainActivity
import ir.dunijet.securitycheckapp.ui.MainActivity.Companion.appColors
import ir.dunijet.securitycheckapp.ui.theme.VazirFontDigits
import ir.dunijet.securitycheckapp.ui.widgets.*
import ir.dunijet.securitycheckapp.util.*
import kotlinx.coroutines.launch

// vaziatDastgah , lastBeroozresani , vaziatKhoroojiHayeFaalVaGheirFaaal va beRoozResani Anha

@Composable
fun HomeScreen() {

    // variables
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    lateinit var smsSent: BroadcastReceiver
    lateinit var smsReceived: BroadcastReceiver
    val smsService = get<SmsRepository>()

    val activity = (LocalContext.current as? Activity)
    val context = LocalContext.current
    val mainActivity = LocalContext.current as MainActivity
    val navigation = getNavController()

    val numberEngine = mainActivity.databaseService.readFromLocal(KEY_NUMBER_ENGINE)
    val password = mainActivity.databaseService.readFromLocal(KEY_USER_PASSWORD)

    val engineStatus = remember { mutableStateOf(HomeVaziat.GheirFaal) }
    val engineStatusLastUpdate = remember { mutableStateOf("نیاز به آپدیت اولیه") }
    val outputs = remember { mutableStateOf(listOf<Output>()) }
    var numberOnOutputs by remember { mutableStateOf(0) }
    var numberOffOutputs by remember { mutableStateOf(0) }
    var themeData by remember { mutableStateOf(ThemeData.LightTheme) }

    fun myListeners() {

//        // get them from sms
//        smsReceived = smsReceivedListenerLong(SmsFormatter.ResponseMain) {
//
//            if (it.contains("admin1") && it.contains("admin2")) {
//                val newMembersFromSms = mutableListOf<Member>()
//
//                for (i in 2..3) {
//                    val adminNumber = it.lines()[i].split(':')[1]
//                    if (adminNumber != "") {
//                        newMembersFromSms.add(Member(null, true, (i - 1).toString(), adminNumber))
//                    }
//                }
//
//                for (i in 4..13) {
//                    val memberNumber = it.lines()[i].split(':')[1]
//                    if (memberNumber != "") {
//                        newMembersFromSms.add(Member(null, false, (i - 3).toString(), memberNumber))
//                    }
//                }
//
//                coroutineScope.launch {
//                    members.clear()
//                    members.addAll(newMembersFromSms)
//                    mainActivity.databaseServiceMain.writeMembers(members.toList())
//                }
//
//            } else {
//
//                when (dialogTask.value) {
//
//                    // working number meghdar nadarad
//
//                    MemberTask.AddUser -> {
//
//                        // motmaen shodan
//                        // do that task in database
//                        // update states and database
//                        // log
//                        // dismiss dialog
//
//                        if (typeIsModir.value && it.contains(SmsFormatter.ResponseSetAdmin2)) {
//
//                            val numberAdmin2 = it.lines()[3].split(":")[1]
//                            coroutineScope.launch {
//                                mainActivity.databaseServiceMain.writeMember(
//                                    Member(
//                                        null,
//                                        true,
//                                        "2",
//                                        numberAdmin2
//                                    )
//                                )
//
//                                members.add(
//                                    Member(
//                                        null,
//                                        true,
//                                        "2",
//                                        numberAdmin2
//                                    )
//                                )
//                                showDialog.value = false
//                                mainActivity.logMain.add(Log(text = navigation.currentDestination?.route + "-" + "sms received" + "-" + "admin2Added"))
//                            }
//                        }
//
//                        if (!typeIsModir.value && it.contains(SmsFormatter.ResponseCreateUser)) {
//
//                            val userCreatedNumber = it.lines()[2].split(':')[1].substring(1)
//                            val userId = (it.lines()[2].split('_')[3]).split(':')[0]
//
//                            coroutineScope.launch {
//                                mainActivity.databaseServiceMain.writeMember(
//                                    Member(
//                                        null,
//                                        false,
//                                        userId,
//                                        userCreatedNumber
//                                    )
//                                )
//                                members.add(
//                                    Member(
//                                        null,
//                                        false,
//                                        userId,
//                                        userCreatedNumber
//                                    )
//                                )
//                                showDialog.value = false
//                                mainActivity.logMain.add(Log(text = navigation.currentDestination?.route + "-" + "sms received" + "-" + "newUserAdded"))
//                            }
//                        }
//                    }
//
//                    MemberTask.EditUser -> {
//
//                        // oldMember -> is number that on three dots clicking
//                        // number from sms - edited number
//                        // dialogMember.value
//
//                        // old number -> old member of user
//                        // new number -> get the new member from sms
//
//                        var newNumber = ""
//                        if(it.contains("admin")) {
//                            newNumber = it.lines()[3].split(':')[1]
//                        } else {
//                            newNumber = it.lines()[2].split(':')[1].substring(1)
//                        }
//
//                        coroutineScope.launch {
//                            mainActivity.databaseServiceMain.editMember(
//                                oldMember,
//                                newNumber
//                            )
//
//                            val foundMember = members.find { itt -> itt.memberNumber == oldMember }
//                            members.remove(foundMember)
//                            members.add(foundMember!!.copy(memberNumber = newNumber))
//
//                            showDialog.value = false
//                            mainActivity.logMain.add(Log(text = navigation.currentDestination?.route + "-" + "sms received" + "-" + "someoneEdited"))
//                        }
//
//                    }
//
//                    MemberTask.DeleteUser -> {
//
//                        // if (it.contains(workingNumber)) {
//                        coroutineScope.launch {
//                            mainActivity.databaseServiceMain.deleteMember(oldMember)
//
//                            val foundMember = members.find { itt -> itt.memberNumber == oldMember }
//                            members.remove(foundMember)
//
//                            showDialog.value = false
//                            mainActivity.logMain.add(Log(text = navigation.currentDestination?.route + "-" + "sms received" + "-" + "someoneRemoved"))
//
//                            if(it.contains(SmsFormatter.ResponseDeleteAdmin2)) {
//                                modirCount = 1
//                                createNewAdmin.value = true
//                            }
//
//                            userCount = members.filter { !it.typeIsModir }.size
//                            if(it.contains(SmsFormatter.ResponseDeleteUser) && userCount == 9) {
//                                createNewUser.value = true
//                            }
//
//                        }
//                        // }
//
//                    }
//                }
//            }
//        }
//        smsSent = smsSentListener(
//            context,
//            navigation.currentDestination?.route!!,
//            { buttonIsLoading.value = it },
//            { mainActivity.logMain.add(it) })
//        context.registerReceiver(smsReceived, IntentFilter(SMS_RECEIVED))
//        context.registerReceiver(smsSent, IntentFilter(SMS_SENT))

    }
    fun logicHome() {

        //todo check if it is admin1 or admin2 or user and change Ui

        // retrieve engine status from sharedPref
        // retrieve engine status last update from sharedPref
        val engStat =
            mainActivity.databaseService.readFromLocal(KEY_ENGINE_STATUS) // 1 faal , 2 nime faal , 3 gheir faal
        val engStatLast = mainActivity.databaseService.readFromLocal(KEY_ENGINE_STATUS_LAST_UPDATED)
        when (engStat) {

            "1" -> {
                engineStatus.value = HomeVaziat.Faal
            }

            "2" -> {
                engineStatus.value = HomeVaziat.NimeFaal
            }

            "3" -> {
                engineStatus.value = HomeVaziat.GheirFaal
            }

        }
        if (engStatLast != "null") {
            engineStatusLastUpdate.value = engStatLast
        }

        // check Outputs from database and show them
        // change enabled and disabled outputs
        coroutineScope.launch {
            val newOutputs = mainActivity.databaseService.readOutputs()
            if (newOutputs.isNotEmpty()) {
                outputs.value = newOutputs
                numberOnOutputs = newOutputs.count { it.isEnabledInHome }
                numberOffOutputs = newOutputs.count { !it.isEnabledInHome }
            }
        }

        // todo think about updating each output - when it should be done

    }

    LaunchedEffect(Unit) {
        MainActivity.recomposition = 0
        MainActivity.checkPermissions(context)
        //logicHome()
        myListeners()
    }
    DisposableEffect(Unit) {
        onDispose {
            MainActivity.recomposition = 0
            mainActivity.addLogsToDb()
        }
    }

    val tmpTheme = mainActivity.databaseService.readFromLocal(key_APP_THEME)
    themeData = if(tmpTheme == "null") {
        if(isSystemInDarkTheme()) ThemeData.DarkTheme else ThemeData.LightTheme
    } else {
        if(tmpTheme == "dark") {
            ThemeData.DarkTheme
        } else {
            ThemeData.LightTheme
        }
    }
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
                        color = MainActivity.appColors[8]
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

                    mainActivity.databaseService.writeToLocal(key_APP_THEME , if(it == ThemeData.DarkTheme) "dark" else "light")

                    mainActivity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    mainActivity.recreate()
                    mainActivity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

                    //context.showToast("on change theme data clicked")
                    //val uiModeManager = context.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
                    //uiModeManager.nightMode = if (it == ThemeData.LightTheme) UiModeManager.MODE_NIGHT_NO else UiModeManager.MODE_NIGHT_YES


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
                            homeVaziat = HomeVaziat.Faal,
                            lastUpdated = "امروز ساعت 10:36",
                            onChangeVaziatClicked = {},
                            onUpdateClicked = {}
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
                            text = "7",
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
                            text = "9",
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

                    OutputVaziatList()

                }
            }
        }
    }
}
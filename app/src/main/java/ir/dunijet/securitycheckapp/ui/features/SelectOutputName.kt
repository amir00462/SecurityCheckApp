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
import ir.dunijet.securitycheckapp.model.data.Output
import ir.dunijet.securitycheckapp.model.data.Remote
import ir.dunijet.securitycheckapp.model.data.Zone
import ir.dunijet.securitycheckapp.service.sms.SmsRepository
import ir.dunijet.securitycheckapp.ui.MainActivity
import ir.dunijet.securitycheckapp.ui.MainActivity.Companion.appColors
import ir.dunijet.securitycheckapp.ui.TimingButton
import ir.dunijet.securitycheckapp.ui.widgets.*
import ir.dunijet.securitycheckapp.util.*
import kotlinx.coroutines.launch

@Composable
fun SelectOutputName() {

    val outputs = remember { mutableStateListOf<Output>() }
    val coroutineScope = rememberCoroutineScope()
    val buttonIsLoading = remember { mutableStateOf(false) }

    lateinit var smsSent: BroadcastReceiver
    lateinit var smsReceived: BroadcastReceiver
    val smsService = get<SmsRepository>()

    val context = LocalContext.current
    val mainActivity = LocalContext.current as MainActivity
    val navigation = getNavController()

    val showDialog = remember { mutableStateOf("hide") }
    val dialogOutput = remember { mutableStateOf(FAKE_OUTPUT) }

    val numberEngine = mainActivity.databaseServiceMain.readFromLocal(KEY_NUMBER_ENGINE)
    val password = mainActivity.databaseServiceMain.readFromLocal(KEY_USER_PASSWORD)

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
    fun getNextOutputId(): Int {

        val fullList = (1..64).toMutableList()
        outputs.forEach {
            fullList.remove(it.outputId.toInt())
        }

        return fullList.first()
    }
    fun addData() {

        coroutineScope.launch {

            val dataFromDatabase = mainActivity.databaseServiceMain.readWiredZones()
            if (dataFromDatabase.isNotEmpty()) {
//                wirelessZones.clear()
//                wirelessZones.addAll(getDefaultWiredZones())
            } else {

                if (MainActivity.recomposition < 1) {

                    // get from sms
//                    val formattedSms = SmsFormatter.getAllRemotes(password)
//                    smsService.sendSms(formattedSms, numberEngine)

                    // mock data from sms
                    outputs.clear()
                    outputs.addAll(getDefaultOutputs())
                    // mainActivity.databaseServiceMain.writeRemotes(remotes.toList())

//                    context.showToast("در حال دریافت اطلاعات از دستگاه")
                    context.showToast("لطفا 30 ثانیه صبر کنید")
                    MainActivity.recomposition += 1
                }
            }
        }
    }

    addData()
    LaunchedEffect(Unit) {
        MainActivity.recomposition = 0
        MainActivity.checkPermissions(context)
        myListeners()
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
                        text = "نام خروجی را انتخاب کنید",
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

        Surface(color = MainActivity.appColors[2]) {

                Column(modifier = Modifier.fillMaxSize()) {

                    Divider(color = MainActivity.appColors[4], thickness = 1.dp)

                    SearchBar {

                    }

                    Divider(color = MainActivity.appColors[4], thickness = 1.dp)

                    OutputNamesList { title , icon ->

                        // onSelected

                    }

                }

        }
    }
}
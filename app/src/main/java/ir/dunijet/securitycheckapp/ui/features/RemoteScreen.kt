package ir.dunijet.securitycheckapp.ui.features

import android.content.BroadcastReceiver
import android.content.IntentFilter
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import ir.dunijet.securitycheckapp.model.data.Remote
import ir.dunijet.securitycheckapp.service.sms.SmsRepository
import ir.dunijet.securitycheckapp.ui.MainActivity
import ir.dunijet.securitycheckapp.ui.widgets.RemoteDialog
import ir.dunijet.securitycheckapp.ui.widgets.RemoteList
import ir.dunijet.securitycheckapp.util.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// todo -> ok listerenrs , format of sms are ok

@Composable
fun RemoteScreen() {

    val remotes = remember { mutableStateListOf<Remote>() }
    val coroutineScope = rememberCoroutineScope()
    val buttonIsLoading = remember { mutableStateOf(false) }

    lateinit var smsSent: BroadcastReceiver
    lateinit var smsReceived: BroadcastReceiver
    val smsService = get<SmsRepository>()

    val context = LocalContext.current
    val mainActivity = LocalContext.current as MainActivity
    val navigation = getNavController()

    // dialog working ->
    val dialogRemote = remember { mutableStateOf(FAKE_REMOTE) }
    val dialogTask = remember { mutableStateOf(MemberTask.AddUser) }
    val showDialog = remember { mutableStateOf(false) }

    val numberEngine = mainActivity.databaseService.readFromLocal(KEY_NUMBER_ENGINE)
    val password = mainActivity.databaseService.readFromLocal(KEY_USER_PASSWORD)

    fun getNextUserId(): Int {

        val fullList =
            mutableListOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20)
        remotes.forEach {
            fullList.remove(it.remoteId.toInt())
        }

        return fullList.first()
    }

    fun getAll() {
        val formattedSms = SmsFormatter.getAllRemotes(password)
        smsService.sendSms(formattedSms, numberEngine)
    }
    fun addRemote(id: String, name: String, status: Boolean) {
        val formattedSms = SmsFormatter.createRemote(id, name, status)
        smsService.sendSms(formattedSms, numberEngine)
    }
    fun editRemote(id: String, name: String, status: Boolean) {
        val formattedSms = SmsFormatter.editRemote(id, name, status)
        smsService.sendSms(formattedSms, numberEngine)
    }
    fun deleteRemote(id: String) {
        val formattedSms = SmsFormatter.deleteRemote(id)
        smsService.sendSms(formattedSms, numberEngine)
    }

    fun myListeners() {

        // get them from sms
        smsReceived = smsReceivedListenerLong(SmsFormatter.ResponseMain) {

            // list of all remotes ->
            if (it.contains("name1")) {
                coroutineScope.launch {
                    val listFromSms = resolveRemoteData(it)

                    mainActivity.databaseService.clearRemotes()
                    mainActivity.databaseService.writeRemotes(listFromSms)

                    delay(10)

                    remotes.clear()
                    remotes.addAll(listFromSms)
                }
            }



            // add , edit , remove

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
        }

        smsSent = smsSentListener(
            context,
            navigation.currentDestination?.route!!,
            { buttonIsLoading.value = it },
            { mainActivity.logMain.add(it) })
        context.registerReceiver(smsReceived, IntentFilter(SMS_RECEIVED))
        context.registerReceiver(smsSent, IntentFilter(SMS_SENT))

    }

    fun addData() {

        coroutineScope.launch {

            val dataFromDatabase = mainActivity.databaseService.readRemotes()
            if (dataFromDatabase.isNotEmpty()) {
                remotes.clear()
                remotes.addAll(dataFromDatabase)
            } else {

                if (MainActivity.recomposition < 1) {

                    // get from sms
                    getAll()
                    context.showToast("در حال دریافت اطلاعات از دستگاه")

                    // mock data from sms
//                    remotes.clear()
//                    remotes.addAll(getRemoteFake())
                    // mainActivity.databaseServiceMain.writeRemotes(remotes.toList())

//                    context.showToast("در حال دریافت اطلاعات از دستگاه")
//                    context.showToast("لطفا 30 ثانیه صبر کنید")
                    MainActivity.recomposition += 1
                }
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
                        text = "ریموت های دستگاه",
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

            Column {

                if (remotes.isEmpty()) {
                    Divider(color = MainActivity.appColors[4], thickness = 1.dp)
                }

                Spacer(modifier = Modifier.height(6.dp))

                RemoteList(remotes) { remote, task ->

                    // dialogMember , dialogTask , showDialog
                    if (task == "ویرایش") {
                        dialogRemote.value = remote
                        dialogTask.value = MemberTask.EditUser
                        showDialog.value = true
                    } else if (task == "حذف") {
                        dialogRemote.value = remote
                        dialogTask.value = MemberTask.DeleteUser
                        showDialog.value = true
                    }
                }

                Divider(color = MainActivity.appColors[4], thickness = 1.dp)
            }

            Box(modifier = Modifier.fillMaxSize()) {

                Column(modifier = Modifier.align(Alignment.BottomStart)) {

                    FloatingActionButton(
                        backgroundColor = MainActivity.appColors[0],
                        contentColor = MainActivity.appColors[1],
                        modifier = Modifier.padding(16.dp),
                        onClick = {

                            // change dialogRemote for id+1 dialogRemote.value = remote
                            dialogRemote.value = FAKE_REMOTE.copy(remoteId = getNextUserId().toString())
                            dialogTask.value = MemberTask.AddUser
                            showDialog.value = true

                        }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_add),
                            contentDescription = "add remote"
                        )
                    }

                }

                if (showDialog.value) {

                    RemoteDialog(
                        buttonIsLoading = buttonIsLoading,
                        task = dialogTask.value,
                        remote = dialogRemote.value,
                        onDismiss = {
                            if (!buttonIsLoading.value) {
                                showDialog.value = false
                            } else {
                                context.showToast("لطفا تا پایان عملیات صبر کنید")
                            }
                        },
                        onSubmit = { id , remoteName, status ->

                            when (dialogTask.value) {

                                MemberTask.AddUser -> {
                                    addRemote(id , remoteName , status)
                                }

                                MemberTask.EditUser -> {
                                    editRemote(id , remoteName , status)
                                }

                                MemberTask.DeleteUser -> {
                                    deleteRemote(id)
                                }

                            }
                        }
                    )
                }
            }
        }
    }
}

fun getRemoteFake(): List<Remote> {

    return listOf(
        Remote(null, "1", "Hamid Reza", true, false),
        Remote(null, "2", "Hamid", false, false),
        Remote(null, "4", "Reza", false, false),
        Remote(null, "5", "Mahnia", true, true),
        Remote(null, "7", "Ahmad", false, false),
        Remote(null, "8", "Mosayeb", true, true),
    )

}

fun resolveRemoteData(response: String): List<Remote> {

    /*
    merssad
    serial_number:14016260
    name1:amirhosein$a$
    name2:mohsen$d$
     */

    val newRemoteFromSms = mutableListOf<Remote>()

    for (i in 2 until (response.lines().size)) {
        if (response.lines()[i].contains("name")) {
            val remoteName = (response.lines()[i].split(':')[1]).split('@')[0]
            val remoteStatus = (response.lines()[i].split(':')[1]).split('@')[1]
            val remoteId = (response.lines()[i].split(':')[0]).split('e')[1]

            if (remoteName.isNotEmpty() && remoteName.isNotBlank() && remoteName != "") {
                newRemoteFromSms.add(
                    Remote(
                        null,
                        remoteId,
                        remoteName,
                        if (remoteStatus == "a") true else false,
                        false
                    )
                )
            }

        }
    }

    return newRemoteFromSms
}

package ir.dunijet.securitycheckapp.ui.features

import android.content.BroadcastReceiver
import android.content.IntentFilter
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import dev.burnoo.cokoin.get
import dev.burnoo.cokoin.navigation.getNavController
import ir.dunijet.securitycheckapp.R
import ir.dunijet.securitycheckapp.model.data.Log
import ir.dunijet.securitycheckapp.model.data.Member
import ir.dunijet.securitycheckapp.service.sms.SmsRepository
import ir.dunijet.securitycheckapp.ui.MainActivity
import ir.dunijet.securitycheckapp.ui.MainActivity.Companion.appColors
import ir.dunijet.securitycheckapp.ui.MainActivity.Companion.checkPermissions
import ir.dunijet.securitycheckapp.ui.MainActivity.Companion.mainTypeModir
import ir.dunijet.securitycheckapp.ui.MainActivity.Companion.recomposition
import ir.dunijet.securitycheckapp.ui.MemberDialog
import ir.dunijet.securitycheckapp.ui.SwipeableView
import ir.dunijet.securitycheckapp.ui.ToggleButtonGroup
import ir.dunijet.securitycheckapp.ui.theme.ColorIcons
import ir.dunijet.securitycheckapp.ui.theme.VazirFontDigits
import ir.dunijet.securitycheckapp.util.*
import ir.dunijet.securitycheckapp.util.SmsFormatter.Companion.ResponseCreateUser
import ir.dunijet.securitycheckapp.util.SmsFormatter.Companion.ResponseDeleteAdmin2
import ir.dunijet.securitycheckapp.util.SmsFormatter.Companion.ResponseDeleteUser
import ir.dunijet.securitycheckapp.util.SmsFormatter.Companion.ResponseSetAdmin2
import kotlinx.coroutines.launch

// 0993 989 7750
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MembersScreen() {

    // tmp
    var workingNumber = ""
    var workingId = ""
    var oldMember = ""

    // receivers
    lateinit var smsSent: BroadcastReceiver
    lateinit var smsReceived: BroadcastReceiver
    val smsService = get<SmsRepository>()

    val context = LocalContext.current
    val mainActivity = LocalContext.current as MainActivity
    val navigation = getNavController()

    // state
    val members = remember { mutableStateListOf<Member>() }
    val coroutineScope = rememberCoroutineScope()
    val dialogMember = remember { mutableStateOf(FAKE_MEMBER) }
    val dialogTask = remember { mutableStateOf(MemberTask.AddUser) }
    val showDialog = remember { mutableStateOf(false) }
    val buttonIsLoading = remember { mutableStateOf(false) }
    val typeIsModir = remember { mutableStateOf(true) }
    val createNewAdmin = remember { mutableStateOf(true) }
    val createNewUser = remember { mutableStateOf(true) }

    val numberEngine = mainActivity.databaseService.readFromLocal(KEY_NUMBER_ENGINE)
    val password = mainActivity.databaseService.readFromLocal(KEY_USER_PASSWORD)

    var modirCount = 0
    var userCount = 0

    fun getNextUserId(): Int {

        val fullList = mutableListOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        members.forEach {
            if (it.typeIsModir == false) {
                fullList.remove(it.memberId.toInt())
            }
        }

        return fullList.first()
    }
    fun myListeners() {

        // get them from sms
        smsReceived = smsReceivedListenerLong(SmsFormatter.ResponseMain) {

            if (it.contains("admin1") && it.contains("admin2")) {
                val newMembersFromSms = mutableListOf<Member>()

                for (i in 2..3) {
                    val adminNumber = it.lines()[i].split(':')[1]
                    if (adminNumber != "") {
                        newMembersFromSms.add(Member(null, true, (i - 1).toString(), adminNumber))
                    }
                }

                for (i in 4..13) {
                    val memberNumber = it.lines()[i].split(':')[1]
                    if (memberNumber != "") {
                        newMembersFromSms.add(Member(null, false, (i - 3).toString(), memberNumber))
                    }
                }

                coroutineScope.launch {
                    members.clear()
                    members.addAll(newMembersFromSms)
                    mainActivity.databaseService.writeMembers(members.toList())
                }

            } else {

                when (dialogTask.value) {

                    // working number meghdar nadarad

                    MemberTask.AddUser -> {

                        // motmaen shodan
                        // do that task in database
                        // update states and database
                        // log
                        // dismiss dialog

                        if (typeIsModir.value && it.contains(ResponseSetAdmin2)) {

                            val numberAdmin2 = it.lines()[3].split(":")[1]
                            coroutineScope.launch {
                                mainActivity.databaseService.writeMember(
                                    Member(
                                        null,
                                        true,
                                        "2",
                                        numberAdmin2
                                    )
                                )

                                members.add(
                                    Member(
                                        null,
                                        true,
                                        "2",
                                        numberAdmin2
                                    )
                                )
                                showDialog.value = false
                                mainActivity.logMain.add(Log(text = navigation.currentDestination?.route + "-" + "sms received" + "-" + "admin2Added"))
                            }
                        }

                        if (!typeIsModir.value && it.contains(ResponseCreateUser)) {

                            val userCreatedNumber = it.lines()[2].split(':')[1].substring(1)
                            val userId = (it.lines()[2].split('_')[3]).split(':')[0]

                            coroutineScope.launch {
                                mainActivity.databaseService.writeMember(
                                    Member(
                                        null,
                                        false,
                                        userId,
                                        userCreatedNumber
                                    )
                                )
                                members.add(
                                    Member(
                                        null,
                                        false,
                                        userId,
                                        userCreatedNumber
                                    )
                                )
                                showDialog.value = false
                                mainActivity.logMain.add(Log(text = navigation.currentDestination?.route + "-" + "sms received" + "-" + "newUserAdded"))
                            }
                        }
                    }

                    MemberTask.EditUser -> {

                        // oldMember -> is number that on three dots clicking
                        // number from sms - edited number
                        // dialogMember.value

                        // old number -> old member of user
                        // new number -> get the new member from sms

                        var newNumber = ""
                        if (it.contains("admin")) {
                            newNumber = it.lines()[3].split(':')[1]
                        } else {
                            newNumber = it.lines()[2].split(':')[1].substring(1)
                        }

                        coroutineScope.launch {
                            mainActivity.databaseService.editMember(
                                oldMember,
                                newNumber
                            )

                            val foundMember = members.find { itt -> itt.memberNumber == oldMember }
                            members.remove(foundMember)
                            members.add(foundMember!!.copy(memberNumber = newNumber))

                            showDialog.value = false
                            mainActivity.logMain.add(Log(text = navigation.currentDestination?.route + "-" + "sms received" + "-" + "someoneEdited"))
                        }

                    }

                    MemberTask.DeleteUser -> {

                        // if (it.contains(workingNumber)) {
                        coroutineScope.launch {
                            mainActivity.databaseService.deleteMember(oldMember)

                            val foundMember = members.find { itt -> itt.memberNumber == oldMember }
                            members.remove(foundMember)

                            showDialog.value = false
                            mainActivity.logMain.add(Log(text = navigation.currentDestination?.route + "-" + "sms received" + "-" + "someoneRemoved"))

                            if (it.contains(ResponseDeleteAdmin2)) {
                                modirCount = 1
                                createNewAdmin.value = true
                            }

                            userCount = members.filter { !it.typeIsModir }.size
                            if (it.contains(ResponseDeleteUser) && userCount == 9) {
                                createNewUser.value = true
                            }

                        }
                        // }

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
    fun addData() {

        coroutineScope.launch {

            val dataFromDatabase = mainActivity.databaseService.readMembers()
            if (dataFromDatabase.isNotEmpty()) {
                members.clear()
                members.addAll(dataFromDatabase)
            } else {

                if (recomposition < 1) {

                    // get from sms
                    val formattedSms = SmsFormatter.getAllUsers(numberEngine, password)
                    smsService.sendSms(formattedSms, numberEngine)

                    // mock data from sms
//                    members.clear()
//                    members.addAll(fakeMemberGenerator())
//                    mainActivity.databaseServiceMain.writeMembers(members.toList())

                    context.showToast("در حال دریافت اطلاعات از دستگاه")
                    recomposition += 1
                }
            }
        }
    }

    addData()
    LaunchedEffect(Unit) {
        recomposition = 0
        checkPermissions(context)
        myListeners()
        typeIsModir.value = mainTypeModir
    }
    DisposableEffect(Unit) {
        onDispose {
            recomposition = 0
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
                        modifier = Modifier.clickable {

                            android.util.Log.v(
                                "testLog",
                                mainActivity.databaseService.readFromLocal(KEY_HOVIAT)
                            )

                            navigation.navigate(MyScreens.RemoteScreen.route)

                        }, fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        lineHeight = 36.sp,
                        text = "اعضای دستگاه",
                        color = appColors[8]
                    )
                },

                navigationIcon = {
                    IconButton(onClick = { navigation.popBackStack() }) {
                        Icon(
                            modifier = Modifier.scale(scaleX = -1f, scaleY = 1f),
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back Button",
                            tint = appColors[6]
                        )
                    }
                },

                backgroundColor = appColors[1],
                contentColor = Color.Gray,
                elevation = 0.dp
            )
        }

    ) {

        Surface(color = appColors[1]) {

            Column {

                Divider(color = appColors[4], thickness = 1.dp)

                ToggleButtonGroup(
                    Modifier.padding(top = 6.dp),
                    "کاربران",
                    "مدیران",
                    typeIsModir.value
                ) { typeIsModir.value = it }

                modirCount = members.filter { it.typeIsModir }.size
                userCount = members.filter { !it.typeIsModir }.size
                if (modirCount >= 2) {
                    createNewAdmin.value = false
                }
                if (userCount >= 10) {
                    createNewUser.value = false
                }

                Spacer(modifier = Modifier.height(6.dp))
                MembersList(if (typeIsModir.value) getAdmins(members.toList()) else getUsers(members.toList())) { member, task ->

                    // dialogMember , dialogTask , showDialog
                    if (task == "ویرایش") {
                        dialogMember.value = member
                        dialogTask.value = MemberTask.EditUser
                        showDialog.value = true
                        oldMember = member.memberNumber
                    } else if (task == "حذف") {
                        dialogMember.value = member
                        dialogTask.value = MemberTask.DeleteUser
                        showDialog.value = true
                        oldMember = member.memberNumber
                    }

                }

                Divider(color = appColors[4], thickness = 1.dp)

                // swiping ->
                SwipeableView(
                    Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                ) {
                    typeIsModir.value = it != "Left"
                }
            }

            Box(modifier = Modifier.fillMaxSize()) {

                Column(modifier = Modifier.align(Alignment.BottomStart)) {

                    FloatingActionButton(
                        backgroundColor = appColors[6],
                        contentColor = appColors[1],
                        modifier = Modifier.padding(start = 16.dp, bottom = 16.dp),
                        onClick = {
                            mainTypeModir = typeIsModir.value
                            navigation.navigate(MyScreens.ChangePasswordInApp.route + "/" + typeIsModir.value)
                        }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_member_lock),
                            contentDescription = "Change Password"
                        )
                    }

                    if ((typeIsModir.value && createNewAdmin.value) ||
                        (!typeIsModir.value && createNewUser.value)
                    ) {
                        FloatingActionButton(
                            backgroundColor = appColors[0],
                            contentColor = appColors[1],
                            modifier = Modifier
                                .padding(bottom = 16.dp, start = 16.dp, end = 16.dp),
                            onClick = {

                                // add
                                dialogMember.value = FAKE_MEMBER.copy(
                                    memberId = (if (typeIsModir.value) 2 else getNextUserId()).toString(),
                                    typeIsModir = typeIsModir.value
                                )
                                dialogTask.value = MemberTask.AddUser
                                showDialog.value = true

                                // functionality add user
                                // open dialog :)

                            }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_member_add),
                                contentDescription = "Add User"
                            )
                        }
                    }
                }

                if (showDialog.value) {
                    MemberDialog(
                        buttonIsLoading = buttonIsLoading,
                        task = dialogTask.value,
                        member = dialogMember.value,
                        onDismiss = {

                            if (!buttonIsLoading.value) {
                                showDialog.value = false
                            } else {
                                context.showToast("لطفا تا پایان عملیات صبر کنید")
                            }

                        })

                    { edtValue, userId ->

                        workingNumber = edtValue
                        workingId = userId

                        when (dialogTask.value) {

                            MemberTask.AddUser -> {

                                if (typeIsModir.value) {

                                    // admin2
                                    val formattedSms = SmsFormatter.setAdmin2(password, edtValue)
                                    smsService.sendSms(formattedSms, numberEngine)

                                } else {

                                    // normal user
                                    val formattedSms = SmsFormatter.createUser(
                                        dialogMember.value.memberId,
                                        edtValue,
                                        password
                                    )
                                    smsService.sendSms(formattedSms, numberEngine)

                                }

                            }

                            MemberTask.EditUser -> {

                                if (typeIsModir.value) {

                                    // edit admin2
                                    val formattedSms = SmsFormatter.editAdmin2(edtValue, password)
                                    smsService.sendSms(formattedSms, numberEngine)

                                } else {

                                    // edit normal user
                                    val formattedSms =
                                        SmsFormatter.editUser(userId, edtValue, password)
                                    smsService.sendSms(formattedSms, numberEngine)

                                }

                            }

                            MemberTask.DeleteUser -> {

                                if (typeIsModir.value) {

                                    // delete admin2
                                    val formattedSms = SmsFormatter.deleteAdmin2(
                                        dialogMember.value.memberNumber,
                                        password
                                    )
                                    smsService.sendSms(formattedSms, numberEngine)

                                } else {

                                    // delete normal user
                                    val formattedSms = SmsFormatter.deleteUser(
                                        userId,
                                        dialogMember.value.memberNumber,
                                        password
                                    )
                                    smsService.sendSms(formattedSms, numberEngine)

                                }

                            }

                        }

                    }
                }

            }
        }
    }
}

@Composable
fun MembersList(list: List<Member>, menuClicked: (Member, String) -> Unit) {

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        items(list.size) {

            // kalame
            Divider(color = appColors[4], thickness = 1.dp)

            CompositionLocalProvider(LocalLayoutDirection provides androidx.compose.ui.unit.LayoutDirection.Ltr) {
                MemberItem(member = list[it], menuClicked = { itt ->
                    menuClicked.invoke(list[it], itt)
                })
            }

        }
    }

}

@Composable
fun MemberItem(member: Member, menuClicked: (String) -> Unit) {

    // three dot menu
    val items = mutableListOf<Pair<String, Int>>()
    items.add(Pair("ویرایش", R.drawable.ic_edit))
    if (!(member.typeIsModir && member.memberId == "1")) {
        items.add(Pair("حذف", R.drawable.ic_delete))
    }

    Surface(color = appColors[2]) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .height(66.dp),
        ) {
            val (id, number, menu) = createRefs()


            MyMenu(
                Modifier
                    .constrainAs(menu) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                    }
                    .padding(end = 6.dp), items = items, itemClicked = menuClicked
            )


            MemberId(modifier = Modifier
                .constrainAs(id) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
                .padding(top = 12.dp, start = 18.dp), id = member.memberId)

            Text(modifier = Modifier
                .constrainAs(number) {
                    start.linkTo(parent.start)
                    top.linkTo(id.bottom)
                    bottom.linkTo(parent.bottom)
                }
                .padding(start = 16.dp),
                text = member.memberNumber.beautifyNumber(),
                fontFamily = VazirFontDigits,
                fontSize = 16.sp,
                lineHeight = 16.sp,
                fontWeight = FontWeight.W400,
                color = appColors[8]
            )

        }
    }

}

@Composable
fun MemberId(modifier: Modifier, id: String , backColor :Color = appColors[1]) {

    Box(
        modifier = modifier
            .size(30.dp, 18.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(backColor),
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = "#" + id,
            fontFamily = VazirFontDigits,
            fontSize = 12.sp,
            lineHeight = 12.sp,
            fontWeight = FontWeight.W500,
            color = appColors[6]
        )

    }

}

@Composable
fun MyMenu(modifier: Modifier, items: List<Pair<String, Int>>, itemClicked: (String) -> Unit) {

    // state of the menu
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier,
        contentAlignment = Alignment.Center
    ) {

        // three dot button
        IconButton(onClick = {

            expanded = true
        }) {
            Icon(
                tint = appColors[6],
                modifier = Modifier.rotate(90f),
                imageVector = Icons.Default.MoreVert,
                contentDescription = null
            )
        }

        // drop down menu
        DropdownMenu(
            modifier = Modifier.width(160.dp),
            offset = DpOffset(x = (16).dp, y = (-18).dp),
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {

            // adding items
            items.forEach { itemValue ->

                DropdownMenuItem(
                    onClick = {
                        itemClicked.invoke(itemValue.first)
                        expanded = false
                    }) {

                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {

                        Text(
                            text = itemValue.first,
                            style = MaterialTheme.typography.body1
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        Icon(
                            painter = painterResource(id = itemValue.second),
                            contentDescription = null,
                            tint = ColorIcons
                        )

                    }

                }
            }
        }
    }

}

fun fakeMemberGenerator(): List<Member> {

    val textFromSms = """
            merssad
            serial_number:14016260
            admin1:09900668721
            admin2:09138936697
            user1:09131005510
            user2:
            user3:
            user4:
            user5:
            user6:
            user7:
            user8:
            user9:
            user10:
        """.trimIndent()

    val newMembersFromSms = mutableListOf<Member>()

    for (i in 2..3) {
        val adminNumber = textFromSms.lines()[i].split(':')[1]
        if (adminNumber != "") {
            newMembersFromSms.add(Member(null, true, (i - 1).toString(), adminNumber))
        }
    }

    for (i in 4..13) {
        val memberNumber = textFromSms.lines()[i].split(':')[1]
        if (memberNumber != "") {
            newMembersFromSms.add(Member(null, false, (i - 3).toString(), memberNumber))
        }
    }

    return newMembersFromSms
}
fun getUsers(fullList: List<Member>): List<Member> {
    return (fullList.filter { !it.typeIsModir }).sortedBy { it.memberId.toInt() }
}
fun getAdmins(fullList: List<Member>): List<Member> {
    return (fullList.filter { it.typeIsModir }).sortedBy { it.memberId.toInt() }
}
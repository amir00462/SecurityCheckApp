package ir.dunijet.securitycheckapp.ui.features

import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
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
import ir.dunijet.securitycheckapp.service.sms.SmsRepository
import ir.dunijet.securitycheckapp.ui.MainActivity
import ir.dunijet.securitycheckapp.ui.MainActivity.Companion.appColors
import ir.dunijet.securitycheckapp.ui.widgets.*
import ir.dunijet.securitycheckapp.util.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// todo - deal lastUpdatedTime in OutputScreen - done
// todo - close dialog after receiving sms - done
// todo - output edit section restriction not added, and disability of fireman
// todo - why edit of 1 not working
// todo - outputs should not change in config screen
// todo - i change vaziat of output in config but it doesnt change in ui of list

@Composable
fun OutputScreen() {

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

    val numberEngine = mainActivity.databaseService.readFromLocal(KEY_NUMBER_ENGINE)
    val password = mainActivity.databaseService.readFromLocal(KEY_USER_PASSWORD)
    val serial = mainActivity.databaseService.readFromLocal(KEY_SERIAL_ENGINE)

    fun myListeners() {

        smsReceived = smsReceivedListener(numberEngine, serial) {
            val idOutput = it.lines()[2].split('_')[1]

            if (outputs.isExist(idOutput)) {

                // mode update , delete
                when {

                    // add daem - khamoosh roshan
                    it.contains("_permament:") -> {

                        showDialog.value = "hide"
                        coroutineScope.launch {

                            val existData = outputs.find {  it.outputId == idOutput  }
                            outputs.remove(existData)
                            outputs.add(
                                dialogOutput.value.copy(
                                    outputId = idOutput,
                                    outputType = OutputType.KhamooshRoshan,
                                    isEnabledInHome = false,
                                    lastUpdatedIsEnabledInHome = System.currentTimeMillis().toString()
                                )
                            )

                            mainActivity.databaseService.deleteOutput(idOutput)
                            delay(1)
                            mainActivity.databaseService.writeOutput(
                                dialogOutput.value.copy(
                                    outputId = idOutput,
                                    outputType = OutputType.KhamooshRoshan,
                                    isEnabledInHome = false,
                                    lastUpdatedIsEnabledInHome = System.currentTimeMillis().toString()
                                )
                            )

                        }

                    }

                    // add lahzeii
                    it.contains("_momentary:") -> {

                        showDialog.value = "hide"
                        coroutineScope.launch {
                            val timeLahzeii = it.lines()[3].split(':')[1].split('*')[1]

                            mainActivity.databaseService.deleteOutput(idOutput)
                            mainActivity.databaseService.writeOutput(
                                dialogOutput.value.copy(
                                    outputId = idOutput,
                                    outputType = OutputType.Lahzeii,
                                    outputLahzeiiZaman = timeLahzeii.toInt().toFloat(),
                                    isEnabledInHome = false,
                                    lastUpdatedIsEnabledInHome = System.currentTimeMillis().toString()
                                )
                            )

                            val existData = outputs.find {  it.outputId == idOutput  }
                            outputs.remove(existData)
                            outputs.add(
                                dialogOutput.value.copy(
                                    outputId = idOutput,
                                    outputType = OutputType.Lahzeii,
                                    outputLahzeiiZaman = timeLahzeii.toInt().toFloat(),
                                    isEnabledInHome = false,
                                    lastUpdatedIsEnabledInHome = System.currentTimeMillis().toString()
                                )
                            )

                        }
                    }

                    // add fireman
                    it.contains("_fireman:") -> {

                        showDialog.value = "hide"
                        coroutineScope.launch {

                            mainActivity.databaseService.deleteOutput(idOutput)
                            mainActivity.databaseService.writeOutput(
                                dialogOutput.value.copy(
                                    outputId = idOutput,
                                    outputType = OutputType.VabasteDoodAtash,
                                    isEnabledInHome = false,
                                    lastUpdatedIsEnabledInHome = System.currentTimeMillis().toString()
                                )
                            )

                            val existData = outputs.find {  it.outputId == idOutput  }
                            outputs.remove(existData)
                            outputs.add(
                                dialogOutput.value.copy(
                                    outputId = idOutput,
                                    outputType = OutputType.VabasteDoodAtash,
                                    isEnabledInHome = false,
                                    lastUpdatedIsEnabledInHome = System.currentTimeMillis().toString()
                                )
                            )
                        }

                    }

                    // delete an output
                    it.contains("_delete") -> {

                        showDialog.value = "hide"
                        coroutineScope.launch {

                            mainActivity.databaseService.deleteOutput(idOutput)

                            val foundData = outputs.find {  it.outputId == idOutput  }
                            outputs.remove(foundData)

                        }

                    }

                }

            } else {

                // mode add
                when {

                    // add daem - khamoosh roshan
                    it.contains("_permament:") -> {

                        showDialog.value = "hide"
                        coroutineScope.launch {
                            mainActivity.databaseService.writeOutput(
                                dialogOutput.value.copy(
                                    outputId = idOutput,
                                    outputType = OutputType.KhamooshRoshan,
                                    isEnabledInHome = false,
                                    lastUpdatedIsEnabledInHome = System.currentTimeMillis().toString()
                                )
                            )
                            outputs.add(
                                dialogOutput.value.copy(
                                    outputId = idOutput,
                                    outputType = OutputType.KhamooshRoshan,
                                    isEnabledInHome = false,
                                    lastUpdatedIsEnabledInHome = System.currentTimeMillis().toString()
                                )
                            )

                        }

                    }

                    // add lahzeii
                    it.contains("_momentary:") -> {

                        showDialog.value = "hide"
                        coroutineScope.launch {
                            val timeLahzeii = it.lines()[3].split(':')[1].split('*')[1]

                            mainActivity.databaseService.writeOutput(
                                dialogOutput.value.copy(
                                    outputId = idOutput,
                                    outputType = OutputType.Lahzeii,
                                    outputLahzeiiZaman = timeLahzeii.toInt().toFloat(),
                                    isEnabledInHome = false,
                                    lastUpdatedIsEnabledInHome = System.currentTimeMillis().toString()
                                )
                            )
                            outputs.add(
                                dialogOutput.value.copy(
                                    outputId = idOutput,
                                    outputType = OutputType.Lahzeii,
                                    outputLahzeiiZaman = timeLahzeii.toInt().toFloat(),
                                    isEnabledInHome = false,
                                    lastUpdatedIsEnabledInHome = System.currentTimeMillis().toString()
                                )
                            )


                        }

                    }

                    // add fireman
                    it.contains("_fireman:") -> {

                        showDialog.value = "hide"
                        coroutineScope.launch {
                            mainActivity.databaseService.writeOutput(
                                dialogOutput.value.copy(
                                    outputId = idOutput,
                                    outputType = OutputType.VabasteDoodAtash,
                                    isEnabledInHome = false,
                                    lastUpdatedIsEnabledInHome = System.currentTimeMillis().toString()
                                )
                            )
                            outputs.add(
                                dialogOutput.value.copy(
                                    outputId = idOutput,
                                    outputType = OutputType.VabasteDoodAtash,
                                    isEnabledInHome = false,
                                    lastUpdatedIsEnabledInHome = System.currentTimeMillis().toString()
                                )
                            )
                        }

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
    fun getNextOutputId(): Int {

        val fullList = (1..64).toMutableList()
        outputs.forEach {
            fullList.remove(it.outputId.toInt())
        }

        return fullList.first()
    }
    fun addData() {

        coroutineScope.launch {

            val dataFromDatabase = mainActivity.databaseService.readOutputs()
            if (dataFromDatabase.isNotEmpty()) {
                outputs.clear()
                outputs.addAll(dataFromDatabase)
            } else {

//                if (MainActivity.recomposition < 1) {
//
//                    // get from sms
////                    val formattedSms = SmsFormatter.getAllRemotes(password)
////                    smsService.sendSms(formattedSms, numberEngine)
//
//                    // mock data from sms
//                    outputs.clear()
//                    outputs.addAll(getDefaultOutputs())
//                    // mainActivity.databaseServiceMain.writeRemotes(remotes.toList())
//
////                    context.showToast("در حال دریافت اطلاعات از دستگاه")
//                    context.showToast("لطفا 30 ثانیه صبر کنید")
//                    MainActivity.recomposition += 1
//                }
            }
        }

    }

    // this functions use data in dialogOutput ->
    fun addNewOutput(newOutput: Output) {
        //dialogOutput.value = newOutput.copy( outputId = getNextOutputId().toString() )

        val formattedSms = SmsFormatter.addNewOutput(password, newOutput)
        smsService.sendSms(formattedSms, numberEngine)
    }
    fun editOutput(editedOutput: Output) {
        //dialogOutput.value = editedOutput.copy( outputId = getNextOutputId().toString() )

        val formattedSms = SmsFormatter.editOutput(password, editedOutput)
        smsService.sendSms(formattedSms, numberEngine)
    }
    fun deleteOutput(idDeleting :String) {
        val formattedSms = SmsFormatter.deleteOutput(password, idDeleting)
        smsService.sendSms(formattedSms, numberEngine)
    }
    fun checkDialogs() {

        when(MainActivity.outputName_dialogPending) {

            // add
            1 -> {
                Log.e("ccheck" , "check main activity in outputscrent -> " + MainActivity.outputName_dialogPendingOutputWorking)

                dialogOutput.value = MainActivity.outputName_dialogPendingOutputWorking.copy(
                    title =  MainActivity.outputName_newOutputName.title,
                    icon = MainActivity.outputName_newOutputName.icon ,
                )

                Log.e("ccheck" , "check value in outputscrent -> " + dialogOutput.value)
                showDialog.value = "add"
            }

            // edit
            2 -> {

                dialogOutput.value = MainActivity.outputName_dialogPendingOutputWorking.copy(
                    title =  MainActivity.outputName_newOutputName.title,
                    icon = MainActivity.outputName_newOutputName.icon ,
                )

                showDialog.value = "edit"
            }

            // non of them
            else -> {
                showDialog.value = "hide"
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
    checkDialogs()
    Scaffold(
        topBar = {

            TopAppBar(
                title = {
                    Text(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        lineHeight = 36.sp,
                        text = "خروجی\u200Cهای دستگاه",
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

        Surface(color = appColors[1]) {
            Box(modifier = Modifier.fillMaxSize()) {
                Column {

                    Divider(color = MainActivity.appColors[4], thickness = 1.dp)
                    OutputList(outputs, onVirayeshClicked = {

                        // on virayesh clicked
                        dialogOutput.value = it
                        showDialog.value = "edit"

                    }, onDeleteClicked = {

                        // on delete clicked
                        dialogOutput.value = it
                        showDialog.value = "delete"

                    })

                }

                // todo check another if statement for if the manager bought gold version
                if (outputs.size < 64) {
                    FloatingActionButton(
                        backgroundColor = appColors[0],
                        contentColor = appColors[1],
                        onClick = {

                            // on add output clicked
                            showDialog.value = "add"

                        },
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {

                        Icon(
                            painter = painterResource(id = R.drawable.ic_add),
                            contentDescription = "add wireless zone"
                        )

                    }
                }

                when (showDialog.value) {

                    "add" -> {

                        Log.e("testLog" , dialogOutput.value.title)
                        if (getNextOutputId() == 1) {
                            DialogOutputAddId1(
                                idd = getNextOutputId().toString(),
                                value = dialogOutput.value,
                                buttonIsLoading = buttonIsLoading,
                                onDismiss = {
                                    if (!buttonIsLoading.value) {
                                        showDialog.value = "hide"
                                    } else {
                                        context.showToast("لطفا تا پایان عملیات صبر کنید")
                                    }
                                },
                                onSubmit = {

                                    // send add sms
                                    addNewOutput(it)

                                }
                            )
                        } else {
                            DialogOutputAdd(
                                idd = getNextOutputId().toString(),
                                value = dialogOutput.value,
                                buttonIsLoading = buttonIsLoading,
                                onDismiss = {
                                    if (!buttonIsLoading.value) {
                                        showDialog.value = "hide"
                                    } else {
                                        context.showToast("لطفا تا پایان عملیات صبر کنید")
                                    }
                                },
                                onSubmit = {

                                    // send add sms
                                    addNewOutput(it)

                                }
                            )
                        }
                    }

                    "edit" -> {
                        if (dialogOutput.value.outputId == "1") {
                            DialogOutputEditId1(
                                output = dialogOutput.value,
                                buttonIsLoading = buttonIsLoading,
                                onDismiss = {
                                    // so we have change in dialog output and this is what we have done :)
                                    if (!buttonIsLoading.value) {
                                        Log.v("testOutput27" , dialogOutput.value.toString())
                                        showDialog.value = "hide"
                                    } else {
                                        context.showToast("لطفا تا پایان عملیات صبر کنید")
                                    }
                                },
                                onSubmit = {
                                    // send add sms
                                    editOutput(it)
                                }
                            )
                        } else {
                            DialogOutputEdit(
                                output = dialogOutput.value,
                                buttonIsLoading = buttonIsLoading,
                                onDismiss = {
                                    if (!buttonIsLoading.value) {
                                        MainActivity.outputName_dialogPendingOutputWorking = dialogOutput.value
                                        showDialog.value = "hide"
                                    } else {
                                        context.showToast("لطفا تا پایان عملیات صبر کنید")
                                    }
                                },
                                onSubmit = {

                                    // send add sms

                                    editOutput(it)

                                }
                            )
                        }

                    }

                    "delete" -> {
                        DialogOutputDelete(
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

                                deleteOutput(dialogOutput.value.outputId)

                            }
                        )
                    }
                }

            }

        }

    }

}

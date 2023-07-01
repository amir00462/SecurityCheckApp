package ir.dunijet.securitycheckapp.ui.features

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.burnoo.cokoin.navigation.getNavController
import ir.dunijet.securitycheckapp.model.data.OutputName
import ir.dunijet.securitycheckapp.ui.MainActivity
import ir.dunijet.securitycheckapp.ui.widgets.*
import ir.dunijet.securitycheckapp.util.*
import kotlinx.coroutines.launch

@Composable
fun SelectOutputName() {
    val outputNames = remember { mutableStateListOf<OutputName>() }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val mainActivity = LocalContext.current as MainActivity
    val navigation = getNavController()
    val showDialog = remember { mutableStateOf(false) }
    val dialogOutputName = remember { mutableStateOf(FAKE_OUTPUT_NAME) }

    fun addData() {
        coroutineScope.launch {
            val dataFromDatabase = mainActivity.databaseService.readOutputNames()
            if (dataFromDatabase.isNotEmpty()) {
                outputNames.clear()
                outputNames.addAll(dataFromDatabase)
            } else {
                mainActivity.databaseService.writeOutputNames(fakeOutputNameList)
                outputNames.clear()
                outputNames.addAll(fakeOutputNameList)
            }
        }
    }

    LaunchedEffect(Unit) {
        MainActivity.recomposition = 0
        MainActivity.checkPermissions(context)
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

                    OutputNamesList(outputNames) { selected ->

                        // onSelected , return to screen A
                        MainActivity.outputName_newOutputName = selected
                        Log.e("ccheck" , "selected ->  " + selected)
                        Log.e("ccheck" , "changed in main activity on outputname screen -> " + MainActivity.outputName_newOutputName)
                        navigation.popBackStack()

                    }

                }

        }
    }
}
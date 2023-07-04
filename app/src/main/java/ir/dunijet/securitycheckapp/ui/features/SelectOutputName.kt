package ir.dunijet.securitycheckapp.ui.features

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.shapes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import dev.burnoo.cokoin.navigation.getNavController
import ir.dunijet.securitycheckapp.R
import ir.dunijet.securitycheckapp.model.data.OutputName
import ir.dunijet.securitycheckapp.ui.MainActivity
import ir.dunijet.securitycheckapp.ui.widgets.*
import ir.dunijet.securitycheckapp.util.*
import kotlinx.coroutines.launch

@Composable
fun SelectOutputName() {
    val fullList = arrayListOf<OutputName>()
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
                fullList.addAll(dataFromDatabase)
            } else {
                mainActivity.databaseService.writeOutputNames(fakeOutputNameList)
                outputNames.clear()
                outputNames.addAll(fakeOutputNameList)
                fullList.addAll(fakeOutputNameList)
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
                actions = {
                    IconButton(onClick = {
                        showDialog.value = true
                    }) {

                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Button",
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
            Box(modifier = Modifier.fillMaxSize()) {
                Column(modifier = Modifier.fillMaxSize()) {

                    Divider(color = MainActivity.appColors[4], thickness = 1.dp)

                    SearchBar { char ->

                        if (char == "") {
                            outputNames.clear()
                            outputNames.addAll(fullList)
                        } else {
                            val filteredList = fullList.filter { it.title.contains(char) }
                            outputNames.clear()
                            outputNames.addAll(filteredList)
                        }

                    }

                    Divider(color = MainActivity.appColors[4], thickness = 1.dp)

                    OutputNamesList(outputNames) { selected ->

                        // onSelected , return to screen A
                        MainActivity.outputName_newOutputName = selected
                        navigation.popBackStack()

                    }

                }

                if (showDialog.value) {

                    DialogAddOutputName(
                        onDismiss = { showDialog.value = false },
                        onSubmit = { title, icon ->

                            coroutineScope.launch {
                                showDialog.value = false
                                val newOutputName = OutputName(null , title , icon)
                                fullList.add(newOutputName)
                                outputNames.add(newOutputName)
                                mainActivity.databaseService.writeOutputName(newOutputName)
                                context.showToast("مورد جدید اضافه شد")
                            }
                        })
                }

            }
        }
    }

}

@Composable
fun IconGrid(
    modifier: Modifier,
    icons: List<Int>,
    selectedIcon: Int,
    onIconSelected: (Int) -> Unit
) {

    LazyVerticalGrid(modifier = modifier.padding(start = 16.dp , end = 16.dp , bottom = 16.dp),
        columns = GridCells.Fixed(9)) {
        items(icons.size) { index ->
            val iconId = icons[index]
            val isSelected = (selectedIcon == iconId)

            Box(
                modifier = Modifier
                    .clip(shapes.medium)
                    .aspectRatio(0.95f)
                    .background( if(isSelected) MainActivity.appColors[5] else  MainActivity.appColors[2])
                    .clickable { onIconSelected(iconId) }
            ) {
                Icon(
                    painter = painterResource(iconId),
                    tint = if(isSelected) MainActivity.appColors[8] else MainActivity.appColors[6],
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize().padding(3.dp)
                )
            }

        }
    }

}

@Composable
fun DialogAddOutputName(
    onDismiss: () -> Unit,
    onSubmit: (String, Int) -> Unit
) {

    val context = LocalContext.current
    val nameRemoteEdt = remember { mutableStateOf("") }
    var selectedIcon by remember { mutableStateOf(-1) }
    val allIcons = listOf(
        R.drawable.ic_home,
        R.drawable.ic_clock,
        R.drawable.ic_home,
        R.drawable.ic_delete,
        R.drawable.ic_home,
        R.drawable.ic_eye,
        R.drawable.ic_home,
        R.drawable.ic_edit,
        R.drawable.ic_home,
        R.drawable.ic_home,
        R.drawable.ic_eye,
        R.drawable.ic_home,
        R.drawable.ic_eye,
        R.drawable.ic_home,
        R.drawable.ic_home,
        R.drawable.ic_fire,
        R.drawable.ic_home,
        R.drawable.ic_eye,
        R.drawable.ic_home,
        R.drawable.ic_edit,
        R.drawable.ic_home,
        R.drawable.ic_home,
        R.drawable.ic_home,
        R.drawable.ic_home,
        R.drawable.ic_edit,
        R.drawable.ic_home,
        R.drawable.ic_home,
        R.drawable.ic_eye,
        R.drawable.ic_home,
        R.drawable.ic_eye,
        R.drawable.ic_fire,
        R.drawable.ic_home,
        R.drawable.ic_eye,
        R.drawable.ic_lamp,
        R.drawable.ic_home,
        R.drawable.ic_fire,
    )

    Dialog(onDismissRequest = onDismiss) {

        Card(
            elevation = 8.dp,
            shape = RoundedCornerShape(8.dp)
        ) {

            Column {

                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .background(MainActivity.appColors[1])
                ) {
                    val (mainText) = createRefs()

                    Text(
                        modifier = Modifier
                            .wrapContentSize()
                            .constrainAs(mainText) {
                                top.linkTo(parent.top)
                                bottom.linkTo(parent.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            },
                        text = "تعریف نام خروجی جدید",
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.W500,
                        fontSize = 18.sp,
                        lineHeight = 36.sp,
                        color = MainActivity.appColors[8],
                    )
                }

                Divider(color = MainActivity.appColors[4], thickness = 1.dp)

                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MainActivity.appColors[2])
                ) {

                    val (textField, iconSelector , txtIcon) = createRefs()

                    ZoneTextField(mainModifier = Modifier
                        .constrainAs(textField) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                        .padding(top = 17.dp),
                        txtSubject = "نام خروجی",
                        edtValue = nameRemoteEdt.value,
                        onValueChanges = { nameRemoteEdt.value = it })

                    Text(
                        modifier = Modifier.constrainAs(txtIcon) {
                            top.linkTo(textField.bottom)
                            end.linkTo(textField.end)
                        }.padding(start = 16.dp, top = 24.dp),
                        text = "انتخاب آیکون",
                        style = MaterialTheme.typography.h3,
                        color = MainActivity.appColors[6],
                    )

                    IconGrid(modifier = Modifier
                        .constrainAs(iconSelector) {
                            top.linkTo(txtIcon.bottom)
                            end.linkTo(txtIcon.end)
                        }
                        .padding(top = 6.dp),
                        icons = allIcons,
                        selectedIcon = selectedIcon,
                        onIconSelected = { iconId -> selectedIcon = iconId }
                    )

                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                ) {

                    Box(modifier = Modifier
                        .background(MainActivity.appColors[4])
                        .clickable {
                            onDismiss.invoke()
                        }
                        .weight(1f)
                        .fillMaxHeight(), contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "لغو",
                            style = MaterialTheme.typography.h2,
                            color = MainActivity.appColors[8],
                        )
                    }

                    Box(modifier = Modifier
                        .background(MainActivity.appColors[0])
                        .clickable {

                            if (nameRemoteEdt.value.count() > 0) {

                                onSubmit.invoke(nameRemoteEdt.value, selectedIcon)

                            } else {
                                context.showToast("لطفا نام را وارد کنید")
                            }

                        }
                        .weight(1f)
                        .fillMaxHeight(), contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "ایجاد",
                            style = MaterialTheme.typography.h2,
                            color = MainActivity.appColors[1],
                        )
                    }
                }
            }
        }
    }
}
package ir.dunijet.securitycheckapp.ui.widgets

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.createFontFamilyResolver
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import dev.burnoo.cokoin.navigation.getNavController
import ir.dunijet.securitycheckapp.R
import ir.dunijet.securitycheckapp.model.data.Output
import ir.dunijet.securitycheckapp.model.data.OutputName
import ir.dunijet.securitycheckapp.model.data.justNameChanged
import ir.dunijet.securitycheckapp.ui.MainActivity
import ir.dunijet.securitycheckapp.ui.MainActivity.Companion.appColors
import ir.dunijet.securitycheckapp.ui.MemberId
import ir.dunijet.securitycheckapp.ui.MyMenu
import ir.dunijet.securitycheckapp.ui.theme.Shapes
import ir.dunijet.securitycheckapp.ui.theme.VazirFont
import ir.dunijet.securitycheckapp.ui.theme.VazirFontDigits
import ir.dunijet.securitycheckapp.util.*
import kotlinx.coroutines.Dispatchers
import java.util.*

@Composable
fun OutputList(
    outputs: SnapshotStateList<Output>,
    onVirayeshClicked: (Output) -> Unit,
    onDeleteClicked: (Output) -> Unit
) {

    outputs.sortBy { it.outputId.toInt() }
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(outputs.size) {

            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {

                OutputWidget(
                    Modifier,
                    outputs[it],
                    onValueChanged = { itt ->

                        // change selected state
                        // todo gharare vaziatesh teghir nakone
                        // val valueToAdd = outputs[it].copy(outputType = itt)
                        // outputs[it] = valueToAdd

//                            zones.remove(zones[it])
//                            zones.add(valueToAdd)

                    },
                    onVirayeshClicked = {

                        // virayesh
                        onVirayeshClicked.invoke(it)

                    },
                    onDeleteClicked = {

                        // delete
                        onDeleteClicked.invoke(it)

                    })

            }
        }
    }
}

@Composable
fun OutputWidget(
    modifier: Modifier,
    output: Output,
    onValueChanged: (OutputType) -> Unit,
    onVirayeshClicked: (Output) -> Unit,
    onDeleteClicked: (Output) -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    var selectedPart by remember { mutableStateOf(output.outputType) }

    // three dot menu
    val items = mutableListOf<Pair<String, Int>>()
    items.add(Pair("ویرایش", R.drawable.ic_edit))
    items.add(Pair("حذف", R.drawable.ic_edit))

    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp)
            .fillMaxWidth()
            .clip(Shapes.medium)
            .background(MainActivity.appColors[4])
    ) {

        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .height(36.dp),
        ) {
            val (id, title, icon, menu) = createRefs()


            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                MyMenu(
                    Modifier
                        .constrainAs(menu) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            end.linkTo(parent.end)
                        }
                        .padding(top = 8.dp),
                    items = items,
                    itemClicked = {

                        if (it == "ویرایش")
                            onVirayeshClicked.invoke(output)
                        else
                            onDeleteClicked.invoke(output)
                    }
                )
            }

            MemberId(modifier = Modifier
                .constrainAs(id) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(menu.start)
                }
                .padding(top = 8.dp),
                id = output.outputId,
                backColor = MainActivity.appColors[5])

            Icon(
                modifier = Modifier
                    .constrainAs(icon) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
                    .padding(start = 10.dp, top = 16.dp),
                painter = painterResource(id = output.icon),
                contentDescription = null,
                tint = MainActivity.appColors[6]
            )

            Text(
                modifier = Modifier
                    .constrainAs(title) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(icon.end)
                    }
                    .padding(start = 10.dp, top = 8.dp),
                fontWeight = FontWeight.Medium,
                lineHeight = 24.sp,
                color = MainActivity.appColors[6],
                text = output.title
            )

        }

        Divider(
            modifier = Modifier.padding(top = 8.dp),
            color = MainActivity.appColors[6].copy(0.16f),
            thickness = 1.dp
        )

        Row(
            Modifier
                .fillMaxWidth()
                .height(42.dp)
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            if (selectedPart == OutputType.VabasteDoodAtash) {

                // vabaste be atash va dood
                Box(
                    Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .clip(MaterialTheme.shapes.small)
                        .background(
                            if (selectedPart != OutputType.VabasteDoodAtash) Color.Transparent
                            else MainActivity.appColors[1]
                        )
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            // todo do not change it on value change
                            // selectedPart = OutputType.VabasteDoodAtash
                            // onValueChanged.invoke(selectedPart)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "این خروجی به سنسور دود و آتش وابسته است",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = MainActivity.appColors[8]
                    )
                }

            } else {

                // khamoosh roshan
                Box(
                    Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(MaterialTheme.shapes.small)
                        .background(
                            if (selectedPart != OutputType.KhamooshRoshan) Color.Transparent
                            else MainActivity.appColors[1]
                        )
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            // todo do not change it on value change
                            // selectedPart = OutputType.KhamooshRoshan
                            // onValueChanged.invoke(selectedPart)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "دائم",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = if (selectedPart != OutputType.KhamooshRoshan) MainActivity.appColors[6]
                        else MainActivity.appColors[8]
                    )
                }


                // lahzeii
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(MaterialTheme.shapes.small)
                        .background(
                            if (selectedPart == OutputType.Lahzeii) MainActivity.appColors[1]
                            else Color.Transparent
                        )
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            // todo do not change it on value change
                            // selectedPart = OutputType.Lahzeii
                            // onValueChanged.invoke(selectedPart)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "لحظه ای",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = if (selectedPart == OutputType.Lahzeii) MainActivity.appColors[8]
                        else MainActivity.appColors[6]
                    )
                }
            }
        }
    }
}

@Composable
fun DialogOutputAddId1(
    idd: String,
    value: Output,
    buttonIsLoading: MutableState<Boolean>,
    onDismiss: () -> Unit,
    onSubmit: (Output) -> Unit
) {

    val navigation = getNavController()
    val context = LocalContext.current
    val creatingOutput = remember { mutableStateOf( value.copy() ) }
    val alphaLahzeii = remember { mutableStateOf(if (creatingOutput.value.outputType == OutputType.Lahzeii) 1f else 0.6f) }
    val isDoodAtash = remember { mutableStateOf(creatingOutput.value.outputType == OutputType.VabasteDoodAtash) }

    Dialog(onDismissRequest = onDismiss) {

        Card(
            //modifier = Modifier.height(600.dp),
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
                        text = "تعریف خروجی جدید",
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.W500,
                        fontSize = 18.sp,
                        lineHeight = 36.sp,
                        color = MainActivity.appColors[8],
                    )
                }

                Divider(color = MainActivity.appColors[4], thickness = 1.dp)

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MainActivity.appColors[2])
                ) {

                    // name
                    OutputName(
                        modifier = Modifier.padding(top = 40.dp),
                        id = idd,
                        name = creatingOutput.value.title,
                        icon = creatingOutput.value.icon,
                        isDoodAtash = isDoodAtash.value
                    ) {

                        // open new screen to choose new icon and title
                        // todo check this

                        onDismiss.invoke()
                        MainActivity.outputName_dialogPending = 1
                        MainActivity.outputName_dialogPendingOutputWorking = creatingOutput.value
                        navigation.navigate(MyScreens.SelectOutputName.route)

                    }


                    // type
                    OutputTypeWidgetId1(
                        modifier = Modifier.padding(top = 24.dp),
                        value = creatingOutput.value.outputType
                    ) {
                        Log.v("testHH", "$it")

                        creatingOutput.value.outputType = it
                        alphaLahzeii.value = if (creatingOutput.value.outputType == OutputType.Lahzeii) 1f else 0.6f
                        isDoodAtash.value = creatingOutput.value.outputType == OutputType.VabasteDoodAtash
                    }

                    // zaman
                    Box(
                        modifier = Modifier.alpha(alphaLahzeii.value)
                    ) {
                        OutputTimeLahzeii(
                            modifier = Modifier.padding(top = 24.dp, bottom = 40.dp),
                            isWorking = alphaLahzeii.value == 1f,
                            value = creatingOutput.value.outputLahzeiiZaman,
                            onValueChanged = {
                                creatingOutput.value.outputLahzeiiZaman = it
                            })
                    }

                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                ) {

                    Box(modifier = Modifier
                        .background(MainActivity.appColors[4])
                        .clickable {
                            if (!buttonIsLoading.value) {
                                onDismiss.invoke()
                            } else {
                                context.showToast("لطفا تا پایان عملیات صبر کنید")
                            }
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

                            if (!buttonIsLoading.value) {
                                onSubmit.invoke(creatingOutput.value.copy(outputId = idd))

                                buttonIsLoading.value = true
                                Timer().schedule(object : TimerTask() {
                                    override fun run() {
                                        buttonIsLoading.value = false
                                    }
                                }, WaitingToReceiveSms)

                            }

                        }
                        .weight(1f)
                        .fillMaxHeight(), contentAlignment = Alignment.Center
                    ) {
                        if (buttonIsLoading.value) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
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
}

// if the int be 1 normal point if 0 it is name chaning ...
@Composable
fun DialogOutputEditId1(
    output: Output,
    buttonIsLoading: MutableState<Boolean>,
    onDismiss: () -> Unit,
    onSubmit: (Output , Int) -> Unit
) {
    val navigation = getNavController()
    val context = LocalContext.current
    val creatingOutput = remember { mutableStateOf(output.copy()) }
    val alphaLahzeii = remember { mutableStateOf(if (creatingOutput.value.outputType == OutputType.Lahzeii) 1f else 0.6f) }
    val isDoodAtash = remember { mutableStateOf(creatingOutput.value.outputType == OutputType.VabasteDoodAtash) }

    Dialog(onDismissRequest = { onDismiss.invoke() } ) {

        Card(
            //modifier = Modifier.height(600.dp),
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
                        text = "ویرایش خروجی",
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.W500,
                        fontSize = 18.sp,
                        lineHeight = 36.sp,
                        color = MainActivity.appColors[8],
                    )
                }

                Divider(color = MainActivity.appColors[4], thickness = 1.dp)

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MainActivity.appColors[2])
                ) {

                    // name
                    OutputName(
                        modifier = Modifier.padding(top = 40.dp),
                        id = creatingOutput.value.outputId,
                        name = creatingOutput.value.title,
                        icon = creatingOutput.value.icon,
                        isDoodAtash = isDoodAtash.value
                    ) {

                        // open new screen to choose new icon and title

                        onDismiss.invoke()
                        MainActivity.outputName_dialogPending = 2
                        MainActivity.outputName_dialogPendingOutputWorking = creatingOutput.value
                        navigation.navigate(MyScreens.SelectOutputName.route)

                    }

                    // type
                    OutputTypeWidgetId1(
                        modifier = Modifier.padding(top = 24.dp),
                        value = creatingOutput.value.outputType
                    ) {

                        creatingOutput.value.outputType = it
                        alphaLahzeii.value = if (creatingOutput.value.outputType == OutputType.Lahzeii) 1f else 0.6f
                        isDoodAtash.value = creatingOutput.value.outputType == OutputType.VabasteDoodAtash

                    }

                    // zaman
                    Box(
                        modifier = Modifier.alpha(alphaLahzeii.value)
                    ) {
                        OutputTimeLahzeii(
                            modifier = Modifier.padding(top = 24.dp, bottom = 40.dp),
                            isWorking = alphaLahzeii.value == 1f,
                            value = creatingOutput.value.outputLahzeiiZaman,
                            onValueChanged = {
                                creatingOutput.value.outputLahzeiiZaman = it
                            })
                    }

                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                ) {

                    Box(modifier = Modifier
                        .background(MainActivity.appColors[4])
                        .clickable {
                            if (!buttonIsLoading.value) {
                                onDismiss.invoke()
                            } else {
                                context.showToast("لطفا تا پایان عملیات صبر کنید")
                            }
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

                            if (!buttonIsLoading.value) {

                                if(creatingOutput.value.justNameChanged(output) ) {
                                    onSubmit.invoke(creatingOutput.value , 0)
                                } else {
                                    onSubmit.invoke(creatingOutput.value , 1)

                                    buttonIsLoading.value = true
                                    Timer().schedule(object : TimerTask() {
                                        override fun run() {
                                            buttonIsLoading.value = false
                                        }
                                    }, WaitingToReceiveSms)
                                }

                            }
                        }
                        .weight(1f)
                        .fillMaxHeight(), contentAlignment = Alignment.Center
                    ) {
                        if (buttonIsLoading.value) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = "ویرایش",
                                style = MaterialTheme.typography.h2,
                                color = MainActivity.appColors[1],
                            )
                        }
                    }

                }

            }


        }
    }

}

@Composable
fun DialogOutputAdd(
    idd: String,
    value :Output,
    buttonIsLoading: MutableState<Boolean>,
    onDismiss: () -> Unit,
    onSubmit: (Output) -> Unit
) {

    Log.v("testAdd" , value.toString())
    val navigation = getNavController()
    val context = LocalContext.current
    val creatingOutput = remember { mutableStateOf(value.copy()) }
    val alphaLahzeii = remember { mutableStateOf(if (creatingOutput.value.outputType == OutputType.Lahzeii) 1f else 0.6f) }
    val isDoodAtash = remember { mutableStateOf(creatingOutput.value.outputType == OutputType.VabasteDoodAtash) }

    Dialog(onDismissRequest = onDismiss) {

        Card(
            //modifier = Modifier.height(600.dp),
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
                        text = "تعریف خروجی جدید",
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.W500,
                        fontSize = 18.sp,
                        lineHeight = 36.sp,
                        color = MainActivity.appColors[8],
                    )
                }

                Divider(color = MainActivity.appColors[4], thickness = 1.dp)

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MainActivity.appColors[2])
                ) {

                    // name
                    OutputName(
                        modifier = Modifier.padding(top = 40.dp),
                        id = idd,
                        name = creatingOutput.value.title,
                        icon = creatingOutput.value.icon,
                        isDoodAtash = isDoodAtash.value
                    ) {

                        // open new screen to choose new icon and title
                        // todo check this

                        onDismiss.invoke()
                        MainActivity.outputName_dialogPending = 1
                        MainActivity.outputName_dialogPendingOutputWorking = creatingOutput.value
                        navigation.navigate(MyScreens.SelectOutputName.route)

                    }


                    // type
                    OutputTypeWidget(
                        modifier = Modifier.padding(top = 24.dp),
                        value = creatingOutput.value.outputType
                    ) {
                        Log.v("testHH", "$it")

                        creatingOutput.value.outputType = it
                        alphaLahzeii.value =
                            if (creatingOutput.value.outputType == OutputType.Lahzeii) 1f else 0.6f
                        isDoodAtash.value =
                            creatingOutput.value.outputType == OutputType.VabasteDoodAtash
                    }

                    // zaman
                    Box(
                        modifier = Modifier.alpha(alphaLahzeii.value)
                    ) {
                        OutputTimeLahzeii(
                            modifier = Modifier.padding(top = 24.dp, bottom = 40.dp),
                            isWorking = alphaLahzeii.value == 1f,
                            value = creatingOutput.value.outputLahzeiiZaman,
                            onValueChanged = {
                                creatingOutput.value.outputLahzeiiZaman = it
                            })
                    }

                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                ) {

                    Box(modifier = Modifier
                        .background(MainActivity.appColors[4])
                        .clickable {
                            if (!buttonIsLoading.value) {
                                onDismiss.invoke()
//                                MainActivity.outputName_dialogPendingOutputWorking = FAKE_OUTPUT
//                                MainActivity.outputName_newOutputName = FAKE_OUTPUT_NAME
                            } else {
                                context.showToast("لطفا تا پایان عملیات صبر کنید")
                            }
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

                            if (!buttonIsLoading.value) {
                                onSubmit.invoke(creatingOutput.value.copy(outputId = idd))
//                                MainActivity.outputName_dialogPendingOutputWorking = FAKE_OUTPUT
//                                MainActivity.outputName_newOutputName = FAKE_OUTPUT_NAME

                                buttonIsLoading.value = true
                                Timer().schedule(object : TimerTask() {
                                    override fun run() {
                                        buttonIsLoading.value = false
                                    }
                                }, WaitingToReceiveSms)

                            }

                        }
                        .weight(1f)
                        .fillMaxHeight(), contentAlignment = Alignment.Center
                    ) {
                        if (buttonIsLoading.value) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
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
}

@Composable
fun DialogOutputEdit(
    output: Output,
    buttonIsLoading: MutableState<Boolean>,
    onDismiss: () -> Unit,
    onSubmit: (Output , Int) -> Unit
) {
    val navigation = getNavController()
    val context = LocalContext.current
    val creatingOutput = remember { mutableStateOf(output.copy()) }
    val alphaLahzeii = remember { mutableStateOf(if (creatingOutput.value.outputType == OutputType.Lahzeii) 1f else 0.6f) }
    val isDoodAtash = remember { mutableStateOf(creatingOutput.value.outputType == OutputType.VabasteDoodAtash) }

    Dialog(onDismissRequest = onDismiss) {

        Card(
            //modifier = Modifier.height(600.dp),
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
                        text = "ویرایش خروجی",
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.W500,
                        fontSize = 18.sp,
                        lineHeight = 36.sp,
                        color = MainActivity.appColors[8],
                    )
                }

                Divider(color = MainActivity.appColors[4], thickness = 1.dp)

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MainActivity.appColors[2])
                ) {

                    // name
                    OutputName(
                        modifier = Modifier.padding(top = 40.dp),
                        id = creatingOutput.value.outputId,
                        name = creatingOutput.value.title,
                        icon = creatingOutput.value.icon,
                        isDoodAtash = isDoodAtash.value
                    ) {

                        // open new screen to choose new icon and title
                        // todo check this

                        onDismiss.invoke()
                        MainActivity.outputName_dialogPending = 2
                        MainActivity.outputName_dialogPendingOutputWorking = creatingOutput.value
                        navigation.navigate(MyScreens.SelectOutputName.route)

                    }

                    // type
                    OutputTypeWidget(
                        modifier = Modifier.padding(top = 24.dp),
                        value = creatingOutput.value.outputType
                    ) {

                        creatingOutput.value.outputType = it
                        alphaLahzeii.value = if (creatingOutput.value.outputType == OutputType.Lahzeii) 1f else 0.6f
                        isDoodAtash.value = creatingOutput.value.outputType == OutputType.VabasteDoodAtash

                    }

                    // zaman
                    Box(
                        modifier = Modifier.alpha(alphaLahzeii.value)
                    ) {
                        OutputTimeLahzeii(
                            modifier = Modifier.padding(top = 24.dp, bottom = 40.dp),
                            isWorking = alphaLahzeii.value == 1f,
                            value = creatingOutput.value.outputLahzeiiZaman,
                            onValueChanged = {
                                creatingOutput.value.outputLahzeiiZaman = it
                            })
                    }

                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                ) {

                    Box(modifier = Modifier
                        .background(MainActivity.appColors[4])
                        .clickable {
                            if (!buttonIsLoading.value) {
                                onDismiss.invoke()
                            } else {
                                context.showToast("لطفا تا پایان عملیات صبر کنید")
                            }
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

                            if (!buttonIsLoading.value) {

                                if(creatingOutput.value.justNameChanged(output) ) {
                                    onSubmit.invoke(creatingOutput.value , 0)
                                } else {
                                    onSubmit.invoke(creatingOutput.value , 1)

                                    buttonIsLoading.value = true
                                    Timer().schedule(object : TimerTask() {
                                        override fun run() {
                                            buttonIsLoading.value = false
                                        }
                                    }, WaitingToReceiveSms)
                                }

                            }
                        }
                        .weight(1f)
                        .fillMaxHeight(), contentAlignment = Alignment.Center
                    ) {
                        if (buttonIsLoading.value) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = "ویرایش",
                                style = MaterialTheme.typography.h2,
                                color = MainActivity.appColors[1],
                            )
                        }
                    }

                }

            }


        }
    }

}


@Composable
fun DialogOutputDelete(
    buttonIsLoading: MutableState<Boolean>,
    onDismiss: () -> Unit,
    onSubmit: () -> Unit
) {
    val context = LocalContext.current

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

                    val txt = "حذف خروجی"
                    Text(
                        modifier = Modifier
                            .wrapContentSize()
                            .constrainAs(mainText) {
                                top.linkTo(parent.top)
                                bottom.linkTo(parent.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            },
                        text = txt,
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
                        .height(115.dp)
                        .background(MainActivity.appColors[2])
                ) {
                    val (txtDel1, txtDel2) = createRefs()

                    Text(
                        modifier = Modifier
                            .constrainAs(txtDel1) {
                                top.linkTo(parent.top)
                                end.linkTo(parent.end)
                            }
                            .padding(top = 25.dp, start = 16.dp),
                        text = "آیا از حذف خروجی اطمینان دارید؟",
                        style = MaterialTheme.typography.caption,
                        color = MainActivity.appColors[8],
                    )
                    Text(
                        modifier = Modifier
                            .constrainAs(txtDel2) {
                                top.linkTo(txtDel1.bottom)
                                end.linkTo(parent.end)
                            }
                            .padding(top = 4.dp, start = 16.dp),
                        text = "این عملیات قابل بازگشت نیست.",
                        style = MaterialTheme.typography.body2,
                        color = MainActivity.appColors[10],
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                ) {

                    Box(modifier = Modifier
                        .background(MainActivity.appColors[4])
                        .clickable {
                            if (!buttonIsLoading.value) {
                                onDismiss.invoke()
                            } else {
                                context.showToast("لطفا تا پایان عملیات صبر کنید")
                            }
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
                        .background(MainActivity.appColors[10])
                        .clickable {

                            if (!buttonIsLoading.value) {
                                onSubmit.invoke()

                                buttonIsLoading.value = true
                                Timer().schedule(object : TimerTask() {
                                    override fun run() {
                                        buttonIsLoading.value = false
                                    }
                                }, WaitingToReceiveSms)

                            } else {
                                context.showToast("لطفا صبر کنید")
                            }

                        }
                        .weight(1f)
                        .fillMaxHeight(), contentAlignment = Alignment.Center
                    ) {
                        if (buttonIsLoading.value) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = "حذف",
                                style = MaterialTheme.typography.h2,
                                color = MainActivity.appColors[1],
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OutputName(
    modifier: Modifier,
    id: String,
    name: String,
    icon: Int,
    isDoodAtash: Boolean,
    onChangeIconAndNameClicked: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .clip(Shapes.medium)
            .background(MainActivity.appColors[4])
    ) {

        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .height(36.dp),
        ) {
            val (idd, title) = createRefs()

            MemberId(
                modifier = Modifier
                    .constrainAs(idd) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                    }
                    .padding(end = 8.dp, top = 8.dp),
                id = id,
                backColor = MainActivity.appColors[5]
            )

            Text(
                modifier = Modifier
                    .constrainAs(title) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                    }
                    .padding(start = 8.dp, top = 8.dp),
                fontWeight = FontWeight.Medium,
                lineHeight = 24.sp,
                color = MainActivity.appColors[6],
                text = "نام خروجی"
            )

        }

        Divider(
            modifier = Modifier.padding(top = 8.dp),
            color = MainActivity.appColors[6].copy(0.16f),
            thickness = 1.dp
        )

        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
            if (isDoodAtash) {

                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(42.dp)
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    ConstraintLayout(
                        Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .clip(MaterialTheme.shapes.small)
                            .background(
                                // if (selectedPart != OutputType.VabasteDoodAtash)
                                //    Color.Transparent
                                // else
                                MainActivity.appColors[1]
                            )

                    ) {
                        val (title, buttonGoNextPage) = createRefs()

                        Text(
                            modifier = Modifier
                                .constrainAs(title) {
                                    top.linkTo(parent.top)
                                    bottom.linkTo(parent.bottom)
                                    end.linkTo(buttonGoNextPage.start)
                                }
                                .padding(bottom = 4.dp),
                            text = "وابسته به سنسور دود و آتش",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            color = MainActivity.appColors[8]
                        )

                        Icon(modifier = Modifier
                            .constrainAs(buttonGoNextPage) {
                                top.linkTo(parent.top)
                                bottom.linkTo(parent.bottom)
                                end.linkTo(parent.end)
                            }
                            .padding(4.dp),
                            painter = painterResource(id = R.drawable.ic_fire),
                            tint = MainActivity.appColors[8],
                            contentDescription = null)

                    }

                }

            } else {

                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(42.dp)
                        .padding(4.dp)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            onChangeIconAndNameClicked.invoke()
                        },
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    ConstraintLayout(
                        Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .clip(MaterialTheme.shapes.small)
                            .background(
                                // if (selectedPart != OutputType.VabasteDoodAtash)
                                //    Color.Transparent
                                // else
                                MainActivity.appColors[1]
                            )
                    ) {
                        val (title, iconn, buttonGoNextPage) = createRefs()

                        Icon(modifier = Modifier
                            .constrainAs(iconn) {
                                top.linkTo(parent.top)
                                bottom.linkTo(parent.bottom)
                                start.linkTo(parent.start)
                            }
                            .padding(12.dp),
                            painter = painterResource(id = R.drawable.ic_arrow_left),
                            tint = MainActivity.appColors[6],
                            contentDescription = null)

                        Text(
                            modifier = Modifier
                                .constrainAs(title) {
                                    top.linkTo(parent.top)
                                    bottom.linkTo(parent.bottom)
                                    end.linkTo(buttonGoNextPage.start)
                                }
                                .padding(bottom = 4.dp),
                            text = name,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            color = MainActivity.appColors[8]
                        )

                        Icon(modifier = Modifier
                            .constrainAs(buttonGoNextPage) {
                                top.linkTo(parent.top)
                                bottom.linkTo(parent.bottom)
                                end.linkTo(parent.end)
                            }
                            .padding(4.dp),
                            painter = painterResource(id = icon),
                            tint = MainActivity.appColors[8],
                            contentDescription = null)
                    }

                }

            }
        }

    }

}

@Composable
fun OutputTypeWidgetId1(
    modifier: Modifier,
    value: OutputType,
    onValueChanged: (OutputType) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val currentState = remember { mutableStateOf(value) }

    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .clip(Shapes.medium)
            .background(MainActivity.appColors[4])
    ) {

        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .height(36.dp),
        ) {
            val (title) = createRefs()

            Text(
                modifier = Modifier
                    .constrainAs(title) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                    }
                    .padding(start = 8.dp, top = 8.dp),
                fontWeight = FontWeight.Medium,
                lineHeight = 24.sp,
                color = MainActivity.appColors[6],
                text = "نوع خروجی"
            )

        }

        Divider(
            modifier = Modifier.padding(top = 8.dp),
            color = MainActivity.appColors[6].copy(0.16f),
            thickness = 1.dp
        )

        Column(
            Modifier
                .fillMaxWidth()
                .height(141.dp)
                .padding(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(MaterialTheme.shapes.small)
                    .background(
                        if (currentState.value != OutputType.VabasteDoodAtash) Color.Transparent
                        else MainActivity.appColors[1]
                    )
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        currentState.value = OutputType.VabasteDoodAtash
                        onValueChanged.invoke(currentState.value)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "وابسته به سنسور دود و آتش",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = if (currentState.value != OutputType.VabasteDoodAtash) MainActivity.appColors[6]
                    else MainActivity.appColors[8]
                )
            }

            Box(
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(MaterialTheme.shapes.small)
                    .background(
                        if (currentState.value != OutputType.KhamooshRoshan) Color.Transparent
                        else MainActivity.appColors[1]
                    )
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        currentState.value = OutputType.KhamooshRoshan
                        onValueChanged.invoke(currentState.value)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "دائم",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = if (currentState.value != OutputType.KhamooshRoshan) MainActivity.appColors[6]
                    else MainActivity.appColors[8]
                )
            }

            Box(
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(MaterialTheme.shapes.small)
                    .background(
                        if (currentState.value != OutputType.Lahzeii) Color.Transparent
                        else MainActivity.appColors[1]
                    )
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        currentState.value = OutputType.Lahzeii
                        onValueChanged.invoke(currentState.value)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "لحظه\u200Cای",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = if (currentState.value != OutputType.Lahzeii) MainActivity.appColors[6]
                    else MainActivity.appColors[8]
                )
            }

        }

    }

}

@Composable
fun OutputTypeWidget(
    modifier: Modifier,
    value: OutputType,
    onValueChanged: (OutputType) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val currentState = remember { mutableStateOf(value) }

    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .clip(Shapes.medium)
            .background(MainActivity.appColors[4])
    ) {

        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .height(36.dp),
        ) {
            val (title) = createRefs()

            Text(
                modifier = Modifier
                    .constrainAs(title) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                    }
                    .padding(start = 8.dp, top = 8.dp),
                fontWeight = FontWeight.Medium,
                lineHeight = 24.sp,
                color = MainActivity.appColors[6],
                text = "نوع خروجی"
            )

        }

        Divider(
            modifier = Modifier.padding(top = 8.dp),
            color = MainActivity.appColors[6].copy(0.16f),
            thickness = 1.dp
        )

        Column(
            Modifier
                .fillMaxWidth()
                .height(92.dp)
                .padding(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(MaterialTheme.shapes.small)
                    .background(
                        if (currentState.value != OutputType.KhamooshRoshan) Color.Transparent
                        else MainActivity.appColors[1]
                    )
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        currentState.value = OutputType.KhamooshRoshan
                        onValueChanged.invoke(currentState.value)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "دائم",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = if (currentState.value != OutputType.KhamooshRoshan) MainActivity.appColors[6]
                    else MainActivity.appColors[8]
                )
            }

            Box(
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(MaterialTheme.shapes.small)
                    .background(
                        if (currentState.value != OutputType.Lahzeii) Color.Transparent
                        else MainActivity.appColors[1]
                    )
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        currentState.value = OutputType.Lahzeii
                        onValueChanged.invoke(currentState.value)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "لحظه\u200Cای",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = if (currentState.value != OutputType.Lahzeii) MainActivity.appColors[6]
                    else MainActivity.appColors[8]
                )
            }

        }
    }
}

@Composable
fun OutputTimeLahzeii(
    modifier: Modifier,
    isWorking: Boolean,
    value: Float,
    onValueChanged: (Float) -> Unit
) {
    var selectedValue by remember { mutableStateOf(value) }

    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .clip(Shapes.medium)
            .background(MainActivity.appColors[4])
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Row {
                Icon(
                    modifier = Modifier.padding(start = 10.dp, top = 10.dp),
                    painter = painterResource(id = R.drawable.ic_ring),
                    contentDescription = null,
                    tint = MainActivity.appColors[6]
                )
                Text(
                    modifier = Modifier.padding(start = 10.dp, top = 8.dp),
                    text = "زمان خروجی لحظه\u200Cای",
                    color = MainActivity.appColors[6],
                    style = MaterialTheme.typography.body1
                )
            }

            Text(
                modifier = Modifier.padding(end = 10.dp, top = 12.dp),
                text = selectedValue.toInt().toString() + " ثانیه",
                style = MaterialTheme.typography.body1,
                fontFamily = VazirFontDigits,
                color = MainActivity.appColors[6]
            )

        }

        Divider(
            modifier = Modifier.padding(top = 8.dp),
            color = MainActivity.appColors[6].copy(0.16f),
            thickness = 1.dp
        )

        Row(
            Modifier
                .fillMaxWidth()
                .height(42.dp)
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                Slider(
                    valueRange = 1f..120.9f,
                    value = selectedValue,
                    onValueChange = {

                        if (isWorking) {
                            selectedValue = it
                            onValueChanged.invoke(it)

                        }

                    },
                )
            }
        }
    }

}


//  -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -
// select output name ->

@Composable
fun SearchBar(onValueChanged: (String) -> Unit) {

    Surface(
        color = appColors[1], modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {

        Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
            SearchTextField {
                onValueChanged.invoke(it)
            }
        }
    }

}

@Composable
fun SearchTextField(
    onValueChanged: (String) -> Unit
) {
    val text = remember { mutableStateOf("") }

    Surface(
        color = appColors[4],
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(6.dp)),
    ) {
        BasicTextField(
            value = text.value,
            onValueChange = {
                text.value = it
                onValueChanged.invoke(text.value)
            },
            singleLine = true,
            cursorBrush = SolidColor(appColors[6]),
            textStyle = TextStyle(
                color = appColors[6],
                fontSize = 16.sp,
                lineHeight = 26.sp,
                fontFamily = VazirFont,
                fontWeight = FontWeight.W400
            ),
            decorationBox = { innerTextField ->

                Row(
                    Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(6.dp)),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // leading icon
                    Icon(
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = "",
                        modifier = Modifier.padding(12.dp),
                        tint = appColors[6]
                    )

                    Box(Modifier.weight(1f)) {
                        if (text.value.isEmpty()) Text(
                            "نام خروجی را جستجو کنید",
                            style = TextStyle(
                                color = appColors[6],
                                fontSize = 16.sp,
                                lineHeight = 26.sp,
                                fontFamily = VazirFont,
                                fontWeight = FontWeight.W400
                            )
                        )
                        innerTextField()
                    }

                    // trailing icon
                    if (text.value != "") {
                        IconButton(
                            onClick = {
                                text.value = ""
                            }
                        ) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "",
                                tint = appColors[6],
                                modifier = Modifier
                                    .padding(14.dp)
                            )
                        }
                    }
                }
            }
        )
    }


}

@Composable
fun OutputNamesList(list: SnapshotStateList<OutputName> , onItemSelected: (OutputName) -> Unit) {

    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(list.size) {

            OutputNameSearch(
                name = list[it].title,
                icon = list[it].icon,
            ) {
                onItemSelected.invoke(list[it])
            }

        }
    }

}

@Composable
fun OutputNameSearch(
    name: String,
    icon: Int,
    onChoose: () -> Unit
) {

    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(appColors[2])
            .clickable(

            ) {
                onChoose.invoke()
            })
    {
        val (title, iconn, downSpacer) = createRefs()

        Icon(modifier = Modifier
            .constrainAs(iconn) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
            }
            .padding(12.dp),
            painter = painterResource(id = icon),
            tint = appColors[6],
            contentDescription = null)

        Text(
            modifier = Modifier
                .constrainAs(title) {
                    top.linkTo(iconn.top)
                    bottom.linkTo(iconn.bottom)
                    start.linkTo(iconn.end)
                }
                .padding(start = 16.dp),
            text = name,
            fontSize = 16.sp,
            fontWeight = FontWeight.W400,
            lineHeight = 26.sp,
            fontFamily = VazirFont,
            color = appColors[8]
        )

        Divider(
            modifier = Modifier.constrainAs(downSpacer) {
                bottom.linkTo(parent.bottom)
                end.linkTo(parent.end)
                start.linkTo(parent.start)
            },
            color = appColors[6].copy(0.16f),
            thickness = 1.dp
        )

    }

}
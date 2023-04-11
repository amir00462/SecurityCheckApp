package ir.dunijet.securitycheckapp.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import ir.dunijet.securitycheckapp.R
import ir.dunijet.securitycheckapp.model.data.Output
import ir.dunijet.securitycheckapp.model.data.Zone
import ir.dunijet.securitycheckapp.ui.MainActivity
import ir.dunijet.securitycheckapp.ui.MemberId
import ir.dunijet.securitycheckapp.ui.MyMenu
import ir.dunijet.securitycheckapp.ui.theme.ColorFaal
import ir.dunijet.securitycheckapp.ui.theme.Shapes
import ir.dunijet.securitycheckapp.ui.theme.VazirFontDigits
import ir.dunijet.securitycheckapp.ui.theme.colorGheirFaal
import ir.dunijet.securitycheckapp.util.*
import java.util.*

@Composable
fun OutputList(
    outputs: SnapshotStateList<Output>,
    onVirayeshClicked: (Output) -> Unit,
    onDeleteClicked: (Output) -> Unit
) {

    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(outputs.size) {

            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {

                OutputWidget(
                    Modifier,
                    outputs[it],
                    onValueChanged = { itt ->

                        // change selected state
                        val valueToAdd = outputs[it].copy(outputType = itt)
                        outputs[it] = valueToAdd

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
                            selectedPart = OutputType.VabasteDoodAtash
                            onValueChanged.invoke(selectedPart)
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
                            selectedPart = OutputType.KhamooshRoshan
                            onValueChanged.invoke(selectedPart)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "خاموش / روشن",
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
                            selectedPart = OutputType.Lahzeii
                            onValueChanged.invoke(selectedPart)
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
fun DialogOutputAdd(
    id: Int,
    buttonIsLoading: MutableState<Boolean>,
    onDismiss: () -> Unit,
    onSubmit: (Output) -> Unit
) {

    val context = LocalContext.current
    val creatingOutput = remember { mutableStateOf(FAKE_OUTPUT) }

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
                        .height(420.dp)
                        .background(MainActivity.appColors[2])
                ) {

                    // name
                    OutputName(
                        modifier = Modifier.padding(top = 40.dp),
                        id = creatingOutput.value.outputId,
                        name = creatingOutput.value.title,
                        icon = creatingOutput.value.icon
                    ) { newName, newIcon ->

                        creatingOutput.value.title = newName
                        creatingOutput.value.icon = newIcon

                    }

                    // type
                    OutputTypeWidget(
                        modifier = Modifier.padding(top = 24.dp),
                        value = creatingOutput.value.outputType
                    ) {
                        creatingOutput.value.outputType = it
                    }

                    // zaman
                    OutputTimeLahzeii(
                        modifier = Modifier.padding(top = 24.dp, bottom = 40.dp),
                        value = if (creatingOutput.value.outputType == OutputType.Lahzeii) creatingOutput.value.outputLahzeiiZaman else 27f,
                        onValueChanged = {
                            creatingOutput.value.outputLahzeiiZaman = it
                        })

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
                                onSubmit.invoke(creatingOutput.value)

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
    onValueChanged: (String, Int) -> Unit
) {

}

@Composable
fun OutputTypeWidget(
    modifier: Modifier,
    value: OutputType,
    onValueChanged: (OutputType) -> Unit
) {

}

@Composable
fun OutputTimeLahzeii(
    modifier: Modifier,
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
                    valueRange = 1f..60.9f,
                    value = value,
                    onValueChange = {
                        selectedValue = it
                        onValueChanged.invoke(it)
                    },
                )
            }
        }
    }
}
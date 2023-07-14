package ir.dunijet.securitycheckapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import ir.dunijet.securitycheckapp.model.data.Member
import ir.dunijet.securitycheckapp.ui.MainActivity.Companion.appColors
import ir.dunijet.securitycheckapp.ui.theme.ColorIcons
import ir.dunijet.securitycheckapp.ui.theme.Shapes
import ir.dunijet.securitycheckapp.util.*
import ir.dunijet.securitycheckapp.R
import ir.dunijet.securitycheckapp.ui.theme.VazirFontDigits
import java.util.*

@Composable
fun Connecting() {

    val context = LocalContext.current
    context.showToast(" در حال ارتباط با دستگاه, صبر کنید...")

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
fun Zone(
    modifier: Modifier,
    title: String,
    icon: Int,
    type: ZoneType,
    twoTypeSteps: CheshmiTwoTypesStep?,
    value: EyeTypes,
    onValueChanged: (EyeTypes) -> Unit,
) {

    val interactionSource = remember { MutableInteractionSource() }
    var selectedPart by remember { mutableStateOf(value) }

    Column(

        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .clip(Shapes.medium)
            .background(MaterialTheme.colors.secondary)


    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.padding(start = 10.dp, top = 12.dp),
                painter = painterResource(id = icon), contentDescription = null, tint =
                MaterialTheme.colors.onSecondary
            )
            Text(modifier = Modifier.padding(start = 10.dp, top = 8.dp), text = title)
        }

        Divider(
            modifier = Modifier.padding(top = 8.dp),
            color = MaterialTheme.colors.onSecondary.copy(0.16f),
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

            // gheir faal
            Box(
                Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(MaterialTheme.shapes.small)
                    .background(
                        if (selectedPart != EyeTypes.GheirFaal) Color.Transparent
                        else MaterialTheme.colors.secondaryVariant
                    )
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        selectedPart = EyeTypes.GheirFaal
                        onValueChanged.invoke(selectedPart)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = when (twoTypeSteps) {
                        CheshmiTwoTypesStep.FaalGheirFaal -> "غیر فعال"
                        CheshmiTwoTypesStep.BaleKheir -> "بله"
                        CheshmiTwoTypesStep.KhamooshRoshanLahzeii -> "خاموش / روشن"
                        else -> "غیر فعال"
                    },
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = if (selectedPart != EyeTypes.GheirFaal) MaterialTheme.colors.onSecondary
                    else MaterialTheme.colors.onPrimary
                )
            }


            if (type == ZoneType.CheshmiFourTypes) {
                // nime faal
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(MaterialTheme.shapes.small)
                        .background(
                            if (selectedPart == EyeTypes.NimeFaal) MaterialTheme.colors.secondaryVariant
                            else Color.Transparent
                        )
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            selectedPart = EyeTypes.NimeFaal
                            onValueChanged.invoke(selectedPart)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "نیمه فعال", fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = if (selectedPart == EyeTypes.NimeFaal) MaterialTheme.colors.onPrimary
                        else MaterialTheme.colors.onSecondary
                    )
                }
            }

            // faal
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(MaterialTheme.shapes.small)
                    .background(
                        if (selectedPart == EyeTypes.Faal) MaterialTheme.colors.secondaryVariant
                        else Color.Transparent
                    )
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        selectedPart = EyeTypes.Faal
                        onValueChanged.invoke(selectedPart)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = when (twoTypeSteps) {
                        CheshmiTwoTypesStep.FaalGheirFaal -> "فعال"
                        CheshmiTwoTypesStep.BaleKheir -> "خیر"
                        CheshmiTwoTypesStep.KhamooshRoshanLahzeii -> "لحظه ای"
                        else -> "فعال"
                    },


                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = if (selectedPart == EyeTypes.Faal) MaterialTheme.colors.onPrimary
                    else MaterialTheme.colors.onSecondary
                )
            }

            if (type == ZoneType.CheshmiFourTypes) {
                // ding dong
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(MaterialTheme.shapes.small)
                        .background(
                            if (selectedPart == EyeTypes.DingDong) MaterialTheme.colors.secondaryVariant
                            else Color.Transparent
                        )
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            selectedPart = EyeTypes.DingDong
                            onValueChanged.invoke(selectedPart)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "دینگ دانگ", fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = if (selectedPart == EyeTypes.DingDong) MaterialTheme.colors.onPrimary
                        else MaterialTheme.colors.onSecondary
                    )
                }
            }

        }

    }

}

@Composable
fun Zone(
    modifier: Modifier,
    title: String,
    icon: Int,
    twoTypeSteps: CheshmiTwoTypesStep?,
    value: EyeTypes,
    onValueChanged: (EyeTypes) -> Unit,
    onArrowClicked: () -> Unit
) {

    val interactionSource = remember { MutableInteractionSource() }
    var selectedPart by remember { mutableStateOf(value) }

    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .clip(Shapes.medium)
            .background(MaterialTheme.colors.secondary)


    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Row {
                Icon(
                    modifier = Modifier.padding(start = 10.dp, top = 12.dp),
                    painter = painterResource(id = icon), contentDescription = null, tint =
                    MaterialTheme.colors.onSecondary
                )
                Text(modifier = Modifier.padding(start = 10.dp, top = 8.dp), text = title)
            }

            Icon(
                modifier = Modifier
                    .padding(end = 10.dp, top = 12.dp)
                    .clickable { onArrowClicked.invoke() },
                painter = painterResource(id = R.drawable.ic_arrow_down),
                contentDescription = null
            )


        }

        Divider(
            modifier = Modifier.padding(top = 8.dp),
            color = MaterialTheme.colors.onSecondary.copy(0.16f),
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

            // gheir faal
            Box(
                Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(MaterialTheme.shapes.small)
                    .background(
                        if (selectedPart != EyeTypes.GheirFaal) Color.Transparent
                        else MaterialTheme.colors.secondaryVariant
                    )
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        selectedPart = EyeTypes.GheirFaal
                        onValueChanged.invoke(selectedPart)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = when (twoTypeSteps) {
                        CheshmiTwoTypesStep.FaalGheirFaal -> "غیر فعال"
                        CheshmiTwoTypesStep.BaleKheir -> "بله"
                        CheshmiTwoTypesStep.KhamooshRoshanLahzeii -> "خاموش / روشن"
                        else -> "غیر فعال"
                    },
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = if (selectedPart != EyeTypes.GheirFaal) MaterialTheme.colors.onSecondary
                    else MaterialTheme.colors.onPrimary
                )
            }

            // faal
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(MaterialTheme.shapes.small)
                    .background(
                        if (selectedPart == EyeTypes.Faal) MaterialTheme.colors.secondaryVariant
                        else Color.Transparent
                    )
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        selectedPart = EyeTypes.Faal
                        onValueChanged.invoke(selectedPart)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = when (twoTypeSteps) {
                        CheshmiTwoTypesStep.FaalGheirFaal -> "فعال"
                        CheshmiTwoTypesStep.BaleKheir -> "خیر"
                        CheshmiTwoTypesStep.KhamooshRoshanLahzeii -> "لحظه ای"
                        else -> "فعال"
                    },
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = if (selectedPart == EyeTypes.Faal) MaterialTheme.colors.onPrimary
                    else MaterialTheme.colors.onSecondary
                )
            }

        }

    }


}

@Composable
fun Zone(
    modifier: Modifier,
    title: String,
    icon: Int,
    txtLeftAdder: String?,
    maxValue: Float,
    value: Float,
    onValueChanged: (Float) -> Unit
) {

    var selectedValue by remember { mutableStateOf(value) }

    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .clip(Shapes.medium)
            .background(MaterialTheme.colors.secondary)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Row {
                Icon(
                    modifier = Modifier.padding(start = 10.dp, top = 12.dp),
                    painter = painterResource(id = icon), contentDescription = null, tint =
                    MaterialTheme.colors.onSecondary
                )
                Text(modifier = Modifier.padding(start = 10.dp, top = 8.dp), text = title)
            }

            Text(
                modifier = Modifier.padding(end = 10.dp, top = 12.dp),
                text = if (txtLeftAdder != null) "${selectedValue.toInt()} $txtLeftAdder" else selectedValue.toInt()
                    .toString(),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.onSecondary
            )

        }

        Divider(
            modifier = Modifier.padding(top = 8.dp),
            color = MaterialTheme.colors.onSecondary.copy(0.16f),
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

            CompositionLocalProvider(LocalLayoutDirection provides androidx.compose.ui.unit.LayoutDirection.Ltr) {
                Slider(
                    valueRange = 1f..maxValue,
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

@Composable
fun TimingButton(
    buttonIsLoading: MutableState<Boolean>,
    ifStatement: Boolean = true,
    clicked: () -> Unit
) {

    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 24.dp, bottom = 24.dp),
        shape = MaterialTheme.shapes.large,
        onClick = {

            if (!buttonIsLoading.value) {

                clicked.invoke()

                if (ifStatement) {

                    buttonIsLoading.value = true

                    Timer().schedule(object : TimerTask() {
                        override fun run() {
                            buttonIsLoading.value = false
                        }
                    }, WaitingToReceiveSms)

                }
            }

        }) {

        if (buttonIsLoading.value) {
            CircularProgressIndicator(
                color = Color.White,
                modifier = Modifier.size(24.dp),
                strokeWidth = 2.dp
            )
        } else {
            Text(
                text = "ذخیره و ارسال",
                style = MaterialTheme.typography.h2,
                color = Color.White
            )
        }
    }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeableView(modifier: Modifier, changed: (String) -> Unit) {

    val parentBoxWidth = 320.dp
    val childBoxSides = 30.dp
    val swipeableState = rememberSwipeableState("Right")
    val widthPx = with(LocalDensity.current) { (parentBoxWidth - childBoxSides).toPx() }
    val anchors = mapOf(0f to "Right", widthPx to "Left")

    Surface(
        color = Color.Transparent,
        modifier = modifier
            .swipeable(
                state = swipeableState,
                anchors = anchors,
                orientation = Orientation.Horizontal,
            )
    ) {}

    if (swipeableState.isAnimationRunning) {
        if (swipeableState.currentValue != swipeableState.targetValue) {
            changed.invoke(swipeableState.targetValue)
        }
    }

}

@Composable
fun ToggleButtonGroup(
    modifier: Modifier,
    first: String,
    second: String,
    oldSelectedPart: Boolean? = null,
    changeSelection: (Boolean) -> Unit
) {

    val interactionSource = remember { MutableInteractionSource() }
    var selectedPart by remember { mutableStateOf(false) }

    if (oldSelectedPart != null) {
        selectedPart = !oldSelectedPart
    }

    Row(
        modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .height(42.dp)
            .clip(Shapes.medium)
            .background(MainActivity.appColors[4])
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            Modifier
                .weight(1f)
                .fillMaxHeight()
                .clip(MaterialTheme.shapes.small)
                .background(
                    if (selectedPart) Color.Transparent
                    else MaterialTheme.colors.secondaryVariant
                )
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    selectedPart = false
                    changeSelection.invoke(!selectedPart)
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = second, fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = if (selectedPart) MainActivity.appColors[6]
                else MainActivity.appColors[8]
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .clip(MaterialTheme.shapes.small)
                .background(
                    if (selectedPart) MaterialTheme.colors.secondaryVariant
                    else Color.Transparent
                )
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    selectedPart = true
                    changeSelection.invoke(!selectedPart)
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = first, fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = if (selectedPart) MainActivity.appColors[8]
                else MainActivity.appColors[6]
            )
        }
    }
}

@Composable
fun MainTextField(
    mainModifier: Modifier? = null,
    txtSubject: String,
    edtValue: String,
    onValueChanges: (String) -> Unit
) {

    Column(modifier = mainModifier ?: Modifier) {

        Text(
            modifier = Modifier.padding(start = 16.dp, top = 24.dp),
            text = txtSubject,
            style = MaterialTheme.typography.h3,
            color = appColors[6],
        )

        TextField(
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            value = edtValue,
            singleLine = true,
            onValueChange = onValueChanges,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp, start = 16.dp, end = 16.dp),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = appColors[4],
                disabledTextColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent

            ),
            textStyle = TextStyle(fontFamily = VazirFontDigits, textDirection = TextDirection.Ltr),
            shape = RoundedCornerShape(6.dp),
        )
    }
}

@Composable
fun RemoteTextField(
    mainModifier: Modifier? = null,
    txtSubject: String,
    edtValue: String,
    onValueChanges: (String) -> Unit
) {

    val context = LocalContext.current
    val pattern = remember { Regex("[a-zA-z\\s]*") }

    Column(modifier = mainModifier ?: Modifier) {

        Text(
            modifier = Modifier.padding(start = 16.dp, top = 24.dp),
            text = txtSubject,
            style = MaterialTheme.typography.h3,
            color = appColors[6],
        )

        TextField(
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next,
            ),
            value = edtValue,
            singleLine = true,
            onValueChange = {

                if (it.matches(pattern)) {
                    onValueChanges.invoke(it)
                } else {
                    context.showToast("فقط حروف انگلیسی")
                }

            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp, start = 16.dp, end = 16.dp),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = appColors[4],
                disabledTextColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent

            ),
            textStyle = TextStyle(fontFamily = VazirFontDigits, textDirection = TextDirection.Ltr),
            shape = RoundedCornerShape(6.dp),
        )
    }
}

@Composable
fun MemberDialog(
    buttonIsLoading: MutableState<Boolean>,
    task: MemberTask,
    member: Member,
    onDismiss: () -> Unit,
    onSubmit: (String, String) -> Unit
) {

    val context = LocalContext.current
    val numberEdt = remember { mutableStateOf("") }
    if (task == MemberTask.EditUser) {
        numberEdt.value = member.memberNumber
    }

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
                        .background(appColors[1])
                ) {
                    val (mainText) = createRefs()

                    val txt = when (task) {
                        MemberTask.AddUser -> {
                            if (member.typeIsModir) "تعریف مدیر جدید" else "تعریف کاربر جدید"
                        }

                        MemberTask.EditUser -> {
                            if (member.typeIsModir) "ویرایش شماره موبایل مدیر" else "ویرایش شماره موبایل کاربر"
                        }

                        MemberTask.DeleteUser -> {
                            if (member.typeIsModir) "حذف مدیر" else "حذف کاربر"
                        }
                    }
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
                        color = appColors[8],
                    )
                }

                Divider(color = appColors[4], thickness = 1.dp)

                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(if (task == MemberTask.DeleteUser) 115.dp else 149.dp)
                        .background(appColors[2])
                ) {

                    val (memberId, textField, txtDel1, txtDel2) = createRefs()

                    if (task == MemberTask.DeleteUser) {

                        Text(
                            modifier = Modifier
                                .constrainAs(txtDel1) {
                                    top.linkTo(parent.top)
                                    end.linkTo(parent.end)
                                }
                                .padding(top = 25.dp, start = 16.dp),
                            text = if (member.typeIsModir) "آیا از حذف مدیر اطمینان دارید؟" else "آیا از حذف کاربر اطمینان دارید؟",
                            style = MaterialTheme.typography.caption,
                            color = appColors[8],
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
                            color = appColors[10],
                        )


                    } else {

                        MemberId(modifier = Modifier
                            .constrainAs(memberId) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                            }
                            .padding(end = 16.dp, top = 26.dp), id = member.memberId , backColor = MainActivity.appColors[0])

                        MainTextField(mainModifier = Modifier
                            .constrainAs(textField) {
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                bottom.linkTo(parent.bottom)
                            }
                            .padding(bottom = 40.dp),
                            txtSubject = if (member.typeIsModir) "شماره موبایل مدیر" else "شماره موبایل کاربر",
                            edtValue = numberEdt.value,
                            onValueChanges = { numberEdt.value = it })
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                ) {

                    Box(modifier = Modifier
                        .background(appColors[4])
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
                            color = appColors[8],
                        )
                    }

                    Box(modifier = Modifier
                        .background(if (task == MemberTask.DeleteUser) appColors[10] else appColors[0])
                        .clickable {

                            if (numberEdt.value.count() == 11 || task == MemberTask.DeleteUser) {

                                if (!buttonIsLoading.value) {
                                    onSubmit.invoke(numberEdt.value, member.memberId)

                                    buttonIsLoading.value = true
                                    Timer().schedule(object : TimerTask() {
                                        override fun run() {
                                            buttonIsLoading.value = false
                                        }
                                    }, WaitingToReceiveSms)

                                }

                            } else {
                                context.showToast("لطفا شماره را وارد کنید")
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
                                text = if (task == MemberTask.AddUser) "ایجاد" else if (task == MemberTask.EditUser) "ویرایش" else "حذف",
                                style = MaterialTheme.typography.h2,
                                color = appColors[1],
                            )
                        }
                    }

                }
            }
        }
    }
}

// list
// add
// edit
// remove


//  -   -   -   -   -   -   -   -   -   -   -       -   -   -   -   -   -   -   -   -       -


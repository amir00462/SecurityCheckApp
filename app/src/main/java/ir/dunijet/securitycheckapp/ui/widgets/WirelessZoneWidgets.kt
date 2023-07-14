package ir.dunijet.securitycheckapp.ui.widgets

import android.util.Log
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
import ir.dunijet.securitycheckapp.model.data.Zone
import ir.dunijet.securitycheckapp.ui.MainActivity
import ir.dunijet.securitycheckapp.ui.MainActivity.Companion.appColors
import ir.dunijet.securitycheckapp.ui.MemberId
import ir.dunijet.securitycheckapp.ui.MyMenu
import ir.dunijet.securitycheckapp.ui.theme.ColorFaal
import ir.dunijet.securitycheckapp.ui.theme.Shapes
import ir.dunijet.securitycheckapp.ui.theme.colorGheirFaal
import ir.dunijet.securitycheckapp.ui.theme.colorNimeFaal
import ir.dunijet.securitycheckapp.util.*
import java.util.*

@Composable
fun WirelessZoneList(
    zones: SnapshotStateList<Zone>,
    onVirayeshClicked: (Zone) -> Unit,
    onDeleteClicked: (Zone) -> Unit
) {

    zones.sortBy { it.zoneId.toInt() }
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(zones.size) {

            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {

                FourStepWirelessZone(
                    Modifier,
                    zones[it],
                    onZoneValueChanged = { itt ->

                        // change selected state
                        val valueToAdd = zones[it].copy(zoneStatus = itt)
                        zones[it] = valueToAdd

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
fun FourStepWirelessZone(
    modifier: Modifier,
    zone: Zone,
    onZoneValueChanged: (ZoneType) -> Unit,
    onVirayeshClicked: (Zone) -> Unit,
    onDeleteClicked: (Zone) -> Unit
) {

    val interactionSource = remember { MutableInteractionSource() }
    var selectedPart by remember { mutableStateOf(zone.zoneStatus) }

    // three dot menu
    val items = mutableListOf<Pair<String, Int>>()
    items.add(Pair("ویرایش", R.drawable.ic_edit))
    items.add(Pair("حذف", R.drawable.ic_delete))

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
                            onVirayeshClicked.invoke(zone)
                        else
                            onDeleteClicked.invoke(zone)

                    }
                )
            }

            MemberId(modifier = Modifier
                .constrainAs(id) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(menu.start)
                }
                .padding(top = 8.dp), id = zone.zoneId, backColor = MainActivity.appColors[5])

            Icon(
                modifier = Modifier
                    .constrainAs(icon) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
                    .padding(start = 10.dp, top = 16.dp),
                painter = painterResource(id = zone.icon), contentDescription = null, tint =
                appColors[6]
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
                color = appColors[6],
                text = zone.title
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
                        if (selectedPart != ZoneType.GheirFaal) Color.Transparent
                        else appColors[1]
                    )
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        selectedPart = ZoneType.GheirFaal
                        onZoneValueChanged.invoke(selectedPart)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "غیر فعال",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = if (selectedPart != ZoneType.GheirFaal) appColors[6]
                    else colorGheirFaal
                )
            }

            // nime faal
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(MaterialTheme.shapes.small)
                    .background(
                        if (selectedPart == ZoneType.NimeFaal) MaterialTheme.colors.secondaryVariant
                        else Color.Transparent
                    )
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        selectedPart = ZoneType.NimeFaal
                        onZoneValueChanged.invoke(selectedPart)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "نیمه فعال", fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = if (selectedPart == ZoneType.NimeFaal) colorNimeFaal
                    else appColors[6]
                )
            }

            // faal
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(MaterialTheme.shapes.small)
                    .background(
                        if (selectedPart == ZoneType.Faal) appColors[1]
                        else Color.Transparent
                    )
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        selectedPart = ZoneType.Faal
                        onZoneValueChanged.invoke(selectedPart)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "فعال",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = if (selectedPart == ZoneType.Faal) ColorFaal
                    else appColors[6]
                )
            }

            // ding dong
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(MaterialTheme.shapes.small)
                    .background(
                        if (selectedPart == ZoneType.DingDong) MaterialTheme.colors.secondaryVariant
                        else Color.Transparent
                    )
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        selectedPart = ZoneType.DingDong
                        onZoneValueChanged.invoke(selectedPart)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "دینگ دانگ", fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = if (selectedPart == ZoneType.DingDong) MaterialTheme.colors.onPrimary
                    else appColors[6]
                )
            }

        }


    }


}

@Composable
fun DialogWirelessZoneAdd(
    id: Int,
    onDismiss: () -> Unit,
    onSubmit: (String, Int) -> Unit
) {

    val context = LocalContext.current
    val nameRemoteEdt = remember { mutableStateOf("") }

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
                        text = "تعریف زون جدید",
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.W500,
                        fontSize = 18.sp,
                        lineHeight = 36.sp,
                        color = MainActivity.appColors[8],
                    )
                }

                Divider(color = MainActivity.appColors[4], thickness = 1.dp)

                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .background(MainActivity.appColors[2])
                ) {

                    val (memberId, textField, status) = createRefs()


                        MemberId(modifier = Modifier
                            .constrainAs(memberId) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                            }
                            .padding(start = 16.dp, top = 41.dp), id = id.toString())


                    ZoneTextField(mainModifier = Modifier
                        .constrainAs(textField) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                        .padding(top = 17.dp),
                        txtSubject = "نام زون",
                        edtValue = nameRemoteEdt.value,
                        onValueChanges = { nameRemoteEdt.value = it })
                }}

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
                                onSubmit.invoke(nameRemoteEdt.value, id)
                            } else {
                                context.showToast("لطفا نام زون را وارد کنید")
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

@Composable
fun DialogWirelessZoneEdit(
    zone: Zone,
    onDismiss: () -> Unit,
    onSubmit: (Zone) -> Unit
) {

    val context = LocalContext.current
    val nameRemoteEdt = remember { mutableStateOf(zone.title) }

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
                        text = "تعریف زون جدید",
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.W500,
                        fontSize = 18.sp,
                        lineHeight = 36.sp,
                        color = MainActivity.appColors[8],
                    )
                }

                Divider(color = MainActivity.appColors[4], thickness = 1.dp)

                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .background(MainActivity.appColors[2])
                ) {

                    val (memberId, textField, status) = createRefs()


                    MemberId(modifier = Modifier
                        .constrainAs(memberId) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                        }
                        .padding(start = 16.dp, top = 41.dp), id = zone.zoneId)

                    ZoneTextField(mainModifier = Modifier
                        .constrainAs(textField) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                        .padding(top = 17.dp),
                        txtSubject = "نام زون",
                        edtValue = nameRemoteEdt.value,
                        onValueChanges = { nameRemoteEdt.value = it })
                }}

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
                                onSubmit.invoke(zone.copy(title = nameRemoteEdt.value))
                            } else {
                                context.showToast("لطفا نام زون را وارد کنید")
                            }
                        }
                        .weight(1f)
                        .fillMaxHeight(), contentAlignment = Alignment.Center
                    ) {
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


@Composable
fun DialogWirelessZoneDelete(
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
                        .background(appColors[1])
                ) {
                    val (mainText) = createRefs()

                    val txt = "حذف زون"
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
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(115.dp)
                        .background(appColors[2])
                ) {
                    val (txtDel1, txtDel2) = createRefs()

                    Text(
                        modifier = Modifier
                            .constrainAs(txtDel1) {
                                top.linkTo(parent.top)
                                end.linkTo(parent.end)
                            }
                            .padding(top = 25.dp, end = 16.dp),
                        text = "آیا از حذف زون اطمینان دارید؟",
                        style = MaterialTheme.typography.caption,
                        color = appColors[8],
                    )
                    Text(
                        modifier = Modifier
                            .constrainAs(txtDel2) {
                                top.linkTo(txtDel1.bottom)
                                end.linkTo(parent.end)
                            }
                            .padding(top = 4.dp, end = 16.dp),
                        text = "این عملیات قابل بازگشت نیست.",
                        style = MaterialTheme.typography.body2,
                        color = appColors[10],
                    )
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
                        .background(appColors[10])
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
                                color = appColors[1],
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ZoneDialogWirelessTest(
    buttonIsLoading: MutableState<Boolean>,
    zone: Zone,
    onDismiss: () -> Unit,
    onSubmit: (String, ZoneNooe) -> Unit
) {

    val vaziatRemote = remember { mutableStateOf(zone.zoneNooe) }
    val context = LocalContext.current
    val nameRemoteEdt = remember { mutableStateOf(zone.title) }

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

                    Text(
                        modifier = Modifier
                            .wrapContentSize()
                            .constrainAs(mainText) {
                                top.linkTo(parent.top)
                                bottom.linkTo(parent.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            },
                        text = "ویرایش زون",
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
                        .height(280.dp)
                        .background(appColors[2])
                ) {

                    val (memberId, textField, status) = createRefs()


                    MemberId(modifier = Modifier
                        .constrainAs(memberId) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                        }
                        .padding(end = 16.dp, top = 41.dp), id = zone.zoneId)

                    ZoneTextField(mainModifier = Modifier
                        .constrainAs(textField) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                        .padding(top = 17.dp),
                        txtSubject = "نام زون",
                        edtValue = nameRemoteEdt.value,
                        onValueChanges = { nameRemoteEdt.value = it })

                    ZoneNoeTest(modifier = Modifier
                        .constrainAs(status) {
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            top.linkTo(textField.bottom)
                        }
                        .padding(top = 24.dp), zone = zone) {
                        vaziatRemote.value = it
                    }

                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
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
                        .background(appColors[0])
                        .clickable {

                            if (nameRemoteEdt.value.count() > 0) {
                                onSubmit.invoke(nameRemoteEdt.value, vaziatRemote.value!!)
                                onDismiss.invoke()
                            } else {
                                context.showToast("لطفا نام زون را وارد کنید")
                            }

                        }
                        .weight(1f)
                        .fillMaxHeight(), contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "ویرایش",
                            style = MaterialTheme.typography.h2,
                            color = appColors[1],
                        )
                    }

                }


            }


        }
    }
}

@Composable
fun ZoneNoeTest(
    modifier: Modifier,
    zone: Zone,
    onZoneValueChanged: (ZoneNooe) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    var selectedPart by remember { mutableStateOf(zone.zoneNooe) }

    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .clip(Shapes.medium)
            .background(appColors[4])
    ) {

        Text(
            modifier = Modifier.padding(start = 10.dp, top = 8.dp),
            fontWeight = FontWeight.Medium,
            lineHeight = 24.sp,
            color = appColors[6],
            text = "نوع زون"
        )

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
                        if (selectedPart == ZoneNooe.AtashDood)
                            appColors[1]
                        else
                            Color.Transparent
                    )
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        selectedPart = ZoneNooe.AtashDood
                        onZoneValueChanged.invoke(selectedPart!!)

                        Log.i("tag", "dood va atash clicked")
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "غیر فعال",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color =
                    if (selectedPart == ZoneNooe.AtashDood)
                        appColors[8]
                    else
                        appColors[6]
                )
            }


            // nime Faal
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(MaterialTheme.shapes.small)
                    .background(
                        if (selectedPart == ZoneNooe.AtashDood)
                            Color.Transparent
                        else
                            appColors[1]
                    )
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        selectedPart = ZoneNooe.Cheshmi
                        onZoneValueChanged.invoke(selectedPart!!)

                        Log.i("tag", "cheshmi clicked")
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "نیمه فعال",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = if (selectedPart == ZoneNooe.AtashDood)
                        appColors[6]
                    else
                        appColors[8]
                )
            }
        }

        Row(
            Modifier
                .fillMaxWidth()
                .height(42.dp)
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // faal
            Box(
                Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(MaterialTheme.shapes.small)
                    .background(
                        if (selectedPart == ZoneNooe.AtashDood)
                            appColors[1]
                        else
                            Color.Transparent
                    )
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        selectedPart = ZoneNooe.AtashDood
                        onZoneValueChanged.invoke(selectedPart!!)

                        Log.i("tag", "dood va atash clicked")
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "فعال",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color =
                    if (selectedPart == ZoneNooe.AtashDood)
                        appColors[8]
                    else
                        appColors[6]
                )
            }


            // ding dong
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(MaterialTheme.shapes.small)
                    .background(
                        if (selectedPart == ZoneNooe.AtashDood)
                            Color.Transparent
                        else
                            appColors[1]
                    )
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        selectedPart = ZoneNooe.Cheshmi
                        onZoneValueChanged.invoke(selectedPart!!)

                        Log.i("tag", "cheshmi clicked")
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "دینگ دانگ",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = if (selectedPart == ZoneNooe.AtashDood)
                        appColors[6]
                    else
                        appColors[8]
                )
            }
        }

    }
}
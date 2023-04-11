package ir.dunijet.securitycheckapp.ui.widgets

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import ir.dunijet.securitycheckapp.model.data.Zone
import ir.dunijet.securitycheckapp.ui.MainActivity
import ir.dunijet.securitycheckapp.ui.MainActivity.Companion.appColors
import ir.dunijet.securitycheckapp.ui.theme.*
import ir.dunijet.securitycheckapp.util.*
import ir.dunijet.securitycheckapp.R
import ir.dunijet.securitycheckapp.ui.MemberId
import ir.dunijet.securitycheckapp.ui.MyMenu
import ir.dunijet.securitycheckapp.ui.theme.VazirFontDigits
import java.util.*

// -    -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   --      -   -
// Zones ->

@Composable
fun WiredZoneList(zones: SnapshotStateList<Zone>, onVirayeshClicked: (Zone) -> Unit) {

    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(zones.size) {

            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {

                when (zones[it].zoneType) {

                    // four nime faal
                    1 -> {

                        FourStepZoneWire1(Modifier, zones[it], { itt ->

                            // change selected state
                            val valueToAdd = zones[it].copy(zoneStatus = itt)
                            zones[it] = valueToAdd

//                            zones.remove(zones[it])
//                            zones.add(valueToAdd)

                        }, {

                            // virayesh
                            onVirayeshClicked.invoke(it)

                        })

                    }

                    // four 24 hour
                    2 -> {

                        FourStepZoneWire2(Modifier, zones[it], { itt ->

                            // change selected state
                            val valueToAdd = zones[it].copy(zoneStatus = itt)
                            zones[it] = valueToAdd

//                            zones.remove(zones[it])
//                            zones.add( , valueToAdd)

                        }, {

                            // virayesh
                            onVirayeshClicked.invoke(it)

                        })

                    }

                    // two full
                    3 -> {

                        TwoStepZoneWire(Modifier, zones[it], { itt ->

                            // change selected state
                            val valueToAdd = zones[it].copy(zoneStatus = itt)
                            zones[it] = valueToAdd
//                            zones.remove(zones[it])
//                            zones.add(valueToAdd)

                        }, {
                            // virayesh
                            onVirayeshClicked.invoke(it)
                        })

                    }

                }

            }
        }
    }

}

@Composable
fun TwoStepZoneWire(
    modifier: Modifier,
    zone: Zone,
    onZoneValueChanged: (ZoneType) -> Unit,
    onVirayeshClicked: (Zone) -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    var selectedPart by remember { mutableStateOf(zone.zoneStatus) }

    // three dot menu
    val items = mutableListOf<Pair<String, Int>>()
    items.add(Pair("ویرایش", R.drawable.ic_edit))

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
                    itemClicked = { onVirayeshClicked.invoke(zone) }
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


        }


    }
}

@Composable
fun FourStepZoneWire1(
    modifier: Modifier,
    zone: Zone,
    onZoneValueChanged: (ZoneType) -> Unit,
    onVirayeshClicked: (Zone) -> Unit,
) {

    val interactionSource = remember { MutableInteractionSource() }
    var selectedPart by remember { mutableStateOf(zone.zoneStatus) }

    // three dot menu
    val items = mutableListOf<Pair<String, Int>>()
    items.add(Pair("ویرایش", R.drawable.ic_edit))

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
                    itemClicked = { onVirayeshClicked.invoke(zone) }
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
fun FourStepZoneWire2(
    modifier: Modifier,
    zone: Zone,
    onZoneValueChanged: (ZoneType) -> Unit,
    onVirayeshClicked: (Zone) -> Unit,
) {

    val interactionSource = remember { MutableInteractionSource() }
    var selectedPart by remember { mutableStateOf(zone.zoneStatus) }

    // three dot menu
    val items = mutableListOf<Pair<String, Int>>()
    items.add(Pair("ویرایش", R.drawable.ic_edit))

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
                    itemClicked = { onVirayeshClicked.invoke(zone) }
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
                    text = "24 ساعته", fontSize = 14.sp,
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
fun ZoneNoe(
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
                    text = "سنسور دود و آتش",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color =
                    if (selectedPart == ZoneNooe.AtashDood)
                        appColors[8]
                    else
                        appColors[6]
                )
            }


            // faal
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
                    text = "سنسور چشمی",
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

@Composable
fun ZoneDialog(
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
                        .height(if (zone.zoneNooe == ZoneNooe.Cheshmi) 140.dp else 280.dp)
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

                    if (zone.zoneId == "5") {

                        ZoneNoe(modifier = Modifier
                            .constrainAs(status) {
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                top.linkTo(textField.bottom)
                            }
                            .padding(top = 24.dp), zone = zone) {
                            vaziatRemote.value = it
                        }
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

                                if (!buttonIsLoading.value) {
                                    onSubmit.invoke(nameRemoteEdt.value, vaziatRemote.value!!)

                                    buttonIsLoading.value = true
                                    Timer().schedule(object : TimerTask() {
                                        override fun run() {
                                            buttonIsLoading.value = false
                                        }
                                    }, WaitingToReceiveSms)

                                }

                            } else {
                                context.showToast("لطفا نام زون را وارد کنید")
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
fun ZoneTextField(
    mainModifier: Modifier? = null,
    txtSubject: String,
    edtValue: String,
    onValueChanges: (String) -> Unit
) {
    val context = LocalContext.current

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
                onValueChanges.invoke(it)
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
            textStyle = TextStyle(fontFamily = VazirFontDigits, textDirection = TextDirection.Rtl),
            shape = RoundedCornerShape(6.dp),
        )
    }
}

// - - - - - - - - - - - - - - - - - - - - - -

@Composable
fun TwoStepNormalZone(
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
fun FourStepZoneWireless(
    modifier: Modifier,
    nameZone: String,
    idZone: String,
    status: ZoneType,
    onVirayeshClicked: (Unit) -> Unit,
    onDeleteClicked: (Unit) -> Unit,
    onStatusChange: (ZoneType) -> Unit,
) {


}

@Composable
fun ZoneTimingButton(
    modifier: Modifier ,
    buttonIsLoading: MutableState<Boolean>,
    clicked: () -> Unit
) {

    Button(
        modifier = modifier.fillMaxWidth(),
        shape = RectangleShape,
        onClick = {

            if (!buttonIsLoading.value) {

                clicked.invoke()

                buttonIsLoading.value = true

                Timer().schedule(object : TimerTask() {
                    override fun run() {
                        buttonIsLoading.value = false
                    }
                }, WaitingToReceiveSms)

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


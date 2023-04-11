package ir.dunijet.securitycheckapp.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import ir.dunijet.securitycheckapp.model.data.Zone
import ir.dunijet.securitycheckapp.ui.MainActivity
import ir.dunijet.securitycheckapp.ui.MainActivity.Companion.appColors
import ir.dunijet.securitycheckapp.ui.MemberId
import ir.dunijet.securitycheckapp.util.MemberTask
import ir.dunijet.securitycheckapp.util.WaitingToReceiveSms
import ir.dunijet.securitycheckapp.util.ZoneNooe
import ir.dunijet.securitycheckapp.util.showToast
import java.util.*


@Composable
fun WirelessZoneList(zones: SnapshotStateList<Zone>, onVirayeshClicked: (Zone) -> Unit) {

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
fun WirelessZoneAdd(
    id: Int,
    buttonIsLoading: MutableState<Boolean>,
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
                        .padding(end = 16.dp, top = 41.dp), id = id.toString())

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

                            if (nameRemoteEdt.value.count() > 0) {

                                if (!buttonIsLoading.value) {
                                    onSubmit.invoke(nameRemoteEdt.value, id)

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
fun WirelessZoneEdit(
    zone: Zone,
    buttonIsLoading: MutableState<Boolean>,
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

                            if (nameRemoteEdt.value.count() > 0) {

                                if (!buttonIsLoading.value) {
                                    onSubmit.invoke(zone.copy(title = nameRemoteEdt.value))

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
fun WirelessZoneDelete(
    buttonIsLoading: MutableState<Boolean>,
    zone: Zone,
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
                            .padding(top = 25.dp, start = 16.dp),
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
                            .padding(top = 4.dp, start = 16.dp),
                        text = "این عملیات قابل بازگشت نیست.",
                        style = MaterialTheme.typography.body2,
                        color = appColors[10],
                    )
                }
            }
        }
    }
}


package ir.dunijet.securitycheckapp.ui.widgets

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
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
import ir.dunijet.securitycheckapp.model.data.Remote
import ir.dunijet.securitycheckapp.ui.MainActivity
import ir.dunijet.securitycheckapp.ui.MainActivity.Companion.appColors
import ir.dunijet.securitycheckapp.ui.theme.Shapes
import ir.dunijet.securitycheckapp.ui.RemoteTextField
import ir.dunijet.securitycheckapp.util.*
import ir.dunijet.securitycheckapp.R
import ir.dunijet.securitycheckapp.ui.MemberId
import ir.dunijet.securitycheckapp.ui.MyMenu
import ir.dunijet.securitycheckapp.ui.theme.VazirFontDigits
import java.util.*

// Remote Screen ->
@Composable
fun RemoteList(list: List<Remote>, menuClicked: (Remote, String) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        items(list.size) {

            // kalame
            Divider(color = MainActivity.appColors[4], thickness = 1.dp)

            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                RemoteItem(remote = list[it], menuClicked = { itt ->
                    menuClicked.invoke(list[it], itt)
                })
            }
        }
    }
}

@Composable
fun RemoteItem(remote: Remote, menuClicked: (String) -> Unit) {

    // three dot menu
    val items = mutableListOf<Pair<String, Int>>()
    items.add(Pair("ویرایش", R.drawable.ic_edit))
    items.add(Pair("حذف", R.drawable.ic_delete))


    Surface(color = MainActivity.appColors[2]) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .height(66.dp),
        ) {
            val (id, number, menu, status) = createRefs()


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
                .padding(top = 12.dp, start = 18.dp), id = remote.remoteId)

            RemoteStatus(
                Modifier
                    .constrainAs(status) {
                        start.linkTo(id.end)
                        top.linkTo(parent.top)
                    }
                    .padding(start = 8.dp, top = 15.dp), remote.remoteStatus
            )

            Text(
                modifier = Modifier
                    .constrainAs(number) {
                        start.linkTo(parent.start)
                        top.linkTo(id.bottom)
                        bottom.linkTo(parent.bottom)
                    }
                    .padding(start = 16.dp),
                text = remote.remoteName,
                fontFamily = VazirFontDigits,
                fontSize = 16.sp,
                lineHeight = 16.sp,
                fontWeight = FontWeight.W400,
                color = MainActivity.appColors[8]
            )

        }
    }

}

@Composable
fun RemoteStatus(modifier: Modifier, isActive: Boolean) {

    Canvas(modifier = modifier.size(10.dp), onDraw = {
        drawCircle(color = if (isActive) MainActivity.appColors[12] else MainActivity.appColors[10])
    })

}

@Composable
fun RemoteDialog(
    buttonIsLoading: MutableState<Boolean>,
    task: MemberTask,
    remote: Remote,
    onDismiss: () -> Unit,
    onSubmit: (String , String, Boolean) -> Unit
) {

    val vaziatRemote = remember { mutableStateOf(remote.remoteStatus) }
    val context = LocalContext.current
    val nameRemoteEdt = remember { mutableStateOf("") }
    if (task == MemberTask.EditUser) {
        nameRemoteEdt.value = remote.remoteName
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
                        .background(MainActivity.appColors[1])
                ) {
                    val (mainText) = createRefs()

                    val txt = when (task) {
                        MemberTask.AddUser -> {
                            "تعریف ریموت جدید"
                        }

                        MemberTask.EditUser -> {
                            "ویرایش ریموت"
                        }

                        MemberTask.DeleteUser -> {
                            "حذف ریموت"
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
                        color = MainActivity.appColors[8],
                    )
                }

                Divider(color = MainActivity.appColors[4], thickness = 1.dp)

                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(if (task == MemberTask.DeleteUser) 115.dp else 280.dp)
                        .background(MainActivity.appColors[2])
                ) {

                    val (memberId, textField, txtDel1, txtDel2, status) = createRefs()

                    if (task == MemberTask.DeleteUser) {

                        Text(
                            modifier = Modifier
                                .constrainAs(txtDel1) {
                                    top.linkTo(parent.top)
                                    end.linkTo(parent.end)
                                }
                                .padding(top = 25.dp, start = 16.dp),
                            text = "آیا از حذف ریموت اطمینان دارید؟",
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


                    } else {

                        MemberId(modifier = Modifier
                            .constrainAs(memberId) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                            }
                            .padding(end = 16.dp, top = 41.dp), id = remote.remoteId)

                        RemoteTextField(mainModifier = Modifier
                            .constrainAs(textField) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                            .padding(top = 17.dp),
                            txtSubject = "نام ریموت",
                            edtValue = nameRemoteEdt.value,
                            onValueChanges = { nameRemoteEdt.value = it })

                        RemoteVaziat(
                            modifier = Modifier
                                .constrainAs(status) {
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                    top.linkTo(textField.bottom)
                                }
                                .padding(top = 24.dp),
                            title = "وضعیت ریموت",
                            icon = R.drawable.ic_remote,
                            status = vaziatRemote.value) { vaziatRemote.value = it }

                    }
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
                        .background(if (task == MemberTask.DeleteUser) MainActivity.appColors[10] else MainActivity.appColors[0])
                        .clickable {

                            if (nameRemoteEdt.value.count() > 0 || task == MemberTask.DeleteUser) {

                                if (!buttonIsLoading.value) {
                                    onSubmit.invoke(remote.remoteId , nameRemoteEdt.value, vaziatRemote.value)

                                    buttonIsLoading.value = true
                                    Timer().schedule(object : TimerTask() {
                                        override fun run() {
                                            buttonIsLoading.value = false
                                        }
                                    }, WaitingToReceiveSms)

                                }

                            } else {
                                context.showToast("لطفا نام ریموت را وارد کنید")
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
                                color = MainActivity.appColors[1],
                            )
                        }
                    }

                }
            }
        }
    }
}

fun isNameRemoteTrue() {

    // check to be just english characters

}

@Composable
fun RemoteVaziat(
    modifier: Modifier,
    title: String,
    icon: Int,
    status: Boolean,
    onStatusChange: (Boolean) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    var selectedPart by remember { mutableStateOf(status) }

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
                        if (selectedPart != false) Color.Transparent
                        else MaterialTheme.colors.secondaryVariant
                    )
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        selectedPart = false
                        onStatusChange.invoke(selectedPart)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "غیر فعال",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = if (selectedPart != false) MaterialTheme.colors.onSecondary
                    else appColors[10]
                )
            }


            // faal
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(MaterialTheme.shapes.small)
                    .background(
                        if (selectedPart == true) MaterialTheme.colors.secondaryVariant
                        else Color.Transparent
                    )
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        selectedPart = true
                        onStatusChange.invoke(selectedPart)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "فعال",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = if (selectedPart == true) appColors[12]
                    else MaterialTheme.colors.onSecondary
                )
            }

        }

    }

}
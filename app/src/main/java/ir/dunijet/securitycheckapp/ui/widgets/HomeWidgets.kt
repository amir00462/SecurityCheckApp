package ir.dunijet.securitycheckapp.ui.widgets

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import dev.burnoo.cokoin.navigation.getNavController
import ir.dunijet.securitycheckapp.R
import ir.dunijet.securitycheckapp.model.data.Output
import ir.dunijet.securitycheckapp.ui.MainActivity
import ir.dunijet.securitycheckapp.ui.MainActivity.Companion.appColors
import ir.dunijet.securitycheckapp.ui.MemberId
import ir.dunijet.securitycheckapp.ui.theme.*
import ir.dunijet.securitycheckapp.util.*
import java.util.*

// todo check height of the outputVaziatList item

@Composable
fun OutputVaziatList(
    list: List<Output>,
    changeChecked: (String, Boolean) -> Unit,
    updateThis: (String) -> Unit
) {

    val config = LocalConfiguration.current
    val heightOfScreen = config.screenHeightDp.dp
    LazyColumn(
        modifier = Modifier
            .defaultMinSize(minHeight = 100.dp )
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {

        items(list.size) { it ->

            OutputVaziatItem(
                outputId = list[it].outputId,
                iconInput = list[it].icon,
                titleMain = list[it].title,
                lastUpdated = list[it].lastUpdatedIsEnabledInHome,
                isCheckedNow = list[it].isEnabledInHome,
                onCheckedChange = { id, isEnabled ->
                    changeChecked.invoke(id, isEnabled)
                },
                onUpdateClicked = { id ->
                    updateThis.invoke(id)
                }
            )

        }
    }

//    Column(
//        Modifier
//            .fillMaxWidth()
//            .verticalScroll(rememberScrollState())
//            .padding(bottom = 16.dp)
//    ) {
//
//        OutputVaziatItem(
//            outputId = "1",
//            iconInput = R.drawable.ic_home,
//            titleMain = "درب اصلی ساختمان",
//            lastUpdated = "دیروز ساعت 16:45",
//            isCheckedNow = true,
//            onCheckedChange = { id, isEnabled ->
//                changeChecked.invoke(id, isEnabled)
//            },
//            onUpdateClicked = { id ->
//                updateThis.invoke(id)
//            }
//        )
//
//    }
}

@Composable
fun OutputVaziatItem(
    outputId: String,
    iconInput: Int,
    titleMain: String,
    lastUpdated: String,
    isCheckedNow: Boolean,
    onCheckedChange: (String, Boolean) -> Unit,
    onUpdateClicked: (String) -> Unit
) {
    val context = LocalContext.current
    val isChecked = remember { mutableStateOf(isCheckedNow) }
    val interactionSource = remember { MutableInteractionSource() }

    // rotation
    val lastThisUpdated = remember { mutableStateOf(context.correctDate(lastUpdated.toLong())) }
    val canSwitchChange = remember { mutableStateOf(true) }
    var rotationAngle by remember { mutableStateOf(0f) }
    val animatedRotationAngle by animateFloatAsState(
        targetValue = rotationAngle,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Box(modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)) {

        Card(
            elevation = 12.dp,
            shape = RoundedCornerShape(8.dp)
        ) {

            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(113.dp)
                    .background(appColors[1])
            ) {
                val (switch, dorIcon, iconInside, mainTitle, detailTitle, divider, timeOfUpdated) = createRefs()

                HomeDrawerSwitch(
                    canChange = canSwitchChange,
                    modifier = Modifier.constrainAs(switch) {
                        top.linkTo(parent.top, 14.dp)
                        end.linkTo(parent.end, 14.dp)
                    },
                    width = 36.dp,
                    height = 20.dp,
                    scale = 1f,
                    checkedTrackColor = appColors[0],
                    uncheckedTrackColor = Color(0xFFAFB2B8),
                    currentState = isChecked.value,
                    onChangeValueClicked = {

                        if (lastThisUpdated.value != "در حال بروز رسانی ...") {
                            isChecked.value = it

                            lastThisUpdated.value = "در حال بروز رسانی ..."
                            canSwitchChange.value = false
                            rotationAngle += 360f

                            onCheckedChange.invoke(outputId, isChecked.value)
                        } else {
                            context.showToast("لطفا صبر کنید")
                        }

                    }
                )

                Box(modifier = Modifier
                    .size(40.dp)
                    .clip(Shapes.medium)
                    .background(color = if (isChecked.value) appColors[0].copy(alpha = 0.2f) else appColors[4])
                    .constrainAs(dorIcon) {
                        start.linkTo(parent.start, 14.dp)
                        top.linkTo(parent.top, 14.dp)
                    })

                Icon(
                    modifier = Modifier.constrainAs(iconInside) {
                        top.linkTo(dorIcon.top)
                        bottom.linkTo(dorIcon.bottom)
                        start.linkTo(dorIcon.start)
                        end.linkTo(dorIcon.end)
                    },
                    painter = painterResource(id = iconInput),
                    contentDescription = null,
                    tint = if (isChecked.value) appColors[0] else Color(0xFF7F808A)
                )

                Text(
                    modifier = Modifier.constrainAs(mainTitle) {
                        top.linkTo(dorIcon.top)
                        start.linkTo(dorIcon.end, 12.dp)
                    },
                    text = titleMain,
                    fontWeight = FontWeight.W500,
                    fontFamily = VazirFontDigits,
                    fontSize = 14.sp,
                    lineHeight = 24.sp,
                    color = appColors[8],
                )

                Text(
                    modifier = Modifier.constrainAs(detailTitle) {
                        top.linkTo(mainTitle.bottom)
                        start.linkTo(mainTitle.start)
                    },
                    text = "خروجی شماره " + outputId + "#",
                    fontFamily = VazirFontDigits,
                    fontWeight = FontWeight.W400,
                    fontSize = 12.sp,
                    lineHeight = 20.sp,
                    color = Color(0xFF7F808A),
                )

                Divider(modifier = Modifier.constrainAs(divider) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(detailTitle.bottom, 12.dp)
                }, color = appColors[6].copy(alpha = 0.16f), thickness = 1.dp)


                Row(
                    modifier = Modifier
                        .wrapContentSize()
                        .constrainAs(timeOfUpdated) {
                            start.linkTo(parent.start, 14.dp)
                            top.linkTo(divider.bottom, 12.dp)
                        }
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {

                            // clicked on update ->
                            if (lastThisUpdated.value != "در حال بروز رسانی ...") {
                                lastThisUpdated.value = "در حال بروز رسانی ..."
                                canSwitchChange.value = false
                                rotationAngle += 360f
                                onUpdateClicked.invoke(outputId)
                            } else {
                                context.showToast("لطفا صبر کنید")
                            }

                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        modifier = Modifier.rotate(animatedRotationAngle),
                        painter = painterResource(id = R.drawable.ic_refresh),
                        contentDescription = null,
                        tint = Color(0xFF7F808A)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "آخرین بروزرسانی:",
                        fontSize = 12.sp,
                        lineHeight = 20.sp,
                        fontWeight = FontWeight.W400,
                        color = Color(0xFF7F808A)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = lastThisUpdated.value,
                        fontSize = 12.sp,
                        lineHeight = 20.sp,
                        fontFamily = VazirFontDigits,
                        fontWeight = FontWeight.W400,
                        color = Color(0xFF7F808A)
                    )

                }


            }
        }

    }

}


@Composable
fun HomeDrawer(
    themeData: ThemeData,
    onCloseDrawer: () -> Unit,
    onChangeTheme: (ThemeData) -> Unit
) {
    val navigation = getNavController()
    val context = LocalContext.current

    BackHandler(onBack = onCloseDrawer)
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
            val (icon, title) = createRefs()

            IconButton(
                modifier = Modifier
                    .constrainAs(icon) {
                        top.linkTo(parent.top, 16.dp)
                        bottom.linkTo(parent.bottom, 16.dp)
                        end.linkTo(parent.end, 16.dp)
                    },
                onClick = {
                    onCloseDrawer.invoke()
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_close),
                    contentDescription = null,
                    tint = appColors[6]
                )
            }

            Text(
                modifier = Modifier.constrainAs(title) {
                    top.linkTo(parent.top, 10.dp)
                    bottom.linkTo(parent.bottom, 10.dp)
                    start.linkTo(parent.start, 16.dp)
                },
                text = "فاطر الکترونیک",
                fontSize = 18.sp,
                lineHeight = 36.sp,
                fontWeight = FontWeight.W500,
                fontFamily = VazirFontDigits,
                color = appColors[8]
            )

        }

        Divider(color = appColors[4], thickness = 1.dp)

        DrawerItem(title = "اعضای دستگاه", icon = R.drawable.ic_person) {
            context.showToast("اعضای")
        }

        DrawerItem(title = "ریموت\u200Cهای دستگاه", icon = R.drawable.ic_remote) {
            context.showToast("ریموت")
        }

        DrawerItem(title = "زون\u200Cهای سیم\u200Cدار", icon = R.drawable.ic_eye) {
            navigation.navigate(MyScreens.WiredZoneScreen.route)
        }

        DrawerItem(title = "زون\u200Cهای بی\u200Cسیم", icon = R.drawable.ic_eye) {
            navigation.navigate(MyScreens.WirelessZoneScreen.route)
        }

        DrawerItem(title = "آژیرها", icon = R.drawable.ic_ring) {
            navigation.navigate(MyScreens.AlarmScreen.route)
        }

        DrawerItem(title = "خروجی\u200Cهای دستگاه", icon = R.drawable.ic_output) {
            navigation.navigate(MyScreens.OutputScreen.route)
        }

        Divider(color = appColors[4], thickness = 1.dp)

        DarkThemeToggle(themeData) {
            onChangeTheme.invoke(if (it) ThemeData.DarkTheme else ThemeData.LightTheme)
        }

    }

}

@Composable
fun HomeVaziat(
    homeVaziat: HomeVaziat,
    lastUpdated: String,
    onChangeVaziatClicked: (HomeVaziat) -> Unit,
    onUpdateClicked: () -> Unit
) {
    val context = LocalContext.current
    val state = remember { mutableStateOf(homeVaziat) }
    val interactionSource = remember { MutableInteractionSource() }

    // rotation
    val lastThisUpdated = remember { mutableStateOf( if(lastUpdated == "نیاز به بروز رسانی") "نیاز به بروز رسانی" else context.correctDate(lastUpdated.toLong()) ) }
    var rotationAngle by remember { mutableStateOf(0f) }
    val animatedRotationAngle by animateFloatAsState(
        targetValue = rotationAngle,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Card(
        elevation = 12.dp,
        shape = RoundedCornerShape(8.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .background(if (state.value == HomeVaziat.Faal) ColorFaal else if (state.value == HomeVaziat.NimeFaal) colorNimeFaal else colorGheirFaal)
        ) {

            Text(
                modifier = Modifier.padding(top = 12.dp, start = 18.dp),
                text = "وضعیت دستگاه",
                fontWeight = FontWeight.W500,
                fontFamily = VazirFontDigits,
                fontSize = 12.sp,
                lineHeight = 20.sp,
                color = appColors[1],
            )

            Spacer(modifier = Modifier.height(4.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .padding(start = 14.dp, end = 14.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(color = Color(0xFFFFFFFF).copy(alpha = 0.2f)),

                contentAlignment = Alignment.Center

            ) {

                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(38.dp)
                        .padding(horizontal = 5.dp, vertical = 2.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // faal
                    Box(
                        Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clip(MaterialTheme.shapes.medium)
                            .background(
                                if (state.value != HomeVaziat.Faal) Color.Transparent
                                else appColors[1]
                            )
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {

                                if (lastThisUpdated.value != "در حال بروز رسانی ...") {

                                    state.value = HomeVaziat.Faal
                                    onChangeVaziatClicked.invoke(state.value)

                                    lastThisUpdated.value = "در حال بروز رسانی ..."
                                    rotationAngle += 360f

                                } else {
                                    context.showToast("لطفا صبر کنید")
                                }

                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "فعال",
                            fontSize = 16.sp,
                            lineHeight = 26.sp,
                            fontWeight = FontWeight.W500,
                            color = if (state.value != HomeVaziat.Faal) appColors[1]
                            else appColors[8]
                        )
                    }

                    // nime faal
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clip(MaterialTheme.shapes.small)
                            .background(
                                if (state.value != HomeVaziat.NimeFaal) Color.Transparent
                                else appColors[1]
                            )
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {

                                if (lastThisUpdated.value != "در حال بروز رسانی ...") {
                                    state.value = HomeVaziat.NimeFaal
                                    onChangeVaziatClicked.invoke(state.value)

                                    lastThisUpdated.value = "در حال بروز رسانی ..."
                                    rotationAngle += 360f
                                } else {
                                    context.showToast("لطفا صبر کنید")
                                }


                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "نیمه فعال",
                            fontSize = 16.sp,
                            lineHeight = 26.sp,
                            fontWeight = FontWeight.W500,
                            color = if (state.value != HomeVaziat.NimeFaal) appColors[1]
                            else appColors[8]
                        )
                    }

                    // gheir faal
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clip(MaterialTheme.shapes.small)
                            .background(
                                if (state.value != HomeVaziat.GheirFaal) Color.Transparent
                                else appColors[1]
                            )
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {

                                if (lastThisUpdated.value != "در حال بروز رسانی ...") {
                                    state.value = HomeVaziat.GheirFaal
                                    onChangeVaziatClicked.invoke(state.value)

                                    lastThisUpdated.value = "در حال بروز رسانی ..."
                                    rotationAngle += 360f
                                } else {
                                    context.showToast("لطفا صبر کنید")
                                }

                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "غیر فعال",
                            fontSize = 16.sp,
                            lineHeight = 26.sp,
                            fontWeight = FontWeight.W500,
                            color = if (state.value != HomeVaziat.GheirFaal) appColors[1]
                            else appColors[8]
                        )
                    }

                }

            }

            Spacer(modifier = Modifier.height(14.dp))
            Divider(color = appColors[1].copy(alpha = 0.32f), thickness = 1.dp)

            Row(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(top = 12.dp, bottom = 12.dp, start = 16.dp)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {

                        if (lastThisUpdated.value != "در حال بروز رسانی ...") {
                            onUpdateClicked.invoke()
                            lastThisUpdated.value = "در حال بروز رسانی ..."
                            rotationAngle += 360f
                        } else {
                            context.showToast("لطفا صبر کنید")
                        }

                    },
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    modifier = Modifier.rotate(animatedRotationAngle),
                    painter = painterResource(id = R.drawable.ic_refresh),
                    contentDescription = null,
                    tint = Color(0xFFFFFFFF)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "آخرین بروزرسانی:",
                    fontSize = 12.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.W400,
                    color = appColors[1]
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = lastThisUpdated.value,
                    fontSize = 12.sp,
                    lineHeight = 20.sp,
                    fontFamily = VazirFontDigits,
                    fontWeight = FontWeight.W400,
                    color = appColors[1]
                )


            }

        }

    }

}

@Composable
fun DrawerItem(title: String, icon: Int, itemClicked: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(modifier = Modifier.clickable {
        itemClicked.invoke()
    }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)

        ) {

            Icon(
                tint = appColors[8],
                modifier = Modifier.padding(end = 16.dp),
                painter = painterResource(id = icon),
                contentDescription = null
            )
            Spacer(modifier = Modifier.padding(end = 16.dp))
            Text(
                text = title,
                fontSize = 14.sp,
                lineHeight = 24.sp,
                fontWeight = FontWeight.W500,
                color = appColors[8]
            )

        }
    }

}

@Composable
fun DarkThemeToggle(currentTheme: ThemeData, changeThemeClicked: (Boolean) -> Unit) {

    Box {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            val (icon, text, switch) = createRefs()

            Icon(
                tint = appColors[8],
                modifier = Modifier
                    .constrainAs(icon) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start, 16.dp)
                    }
                    .padding(end = 16.dp),
                painter = painterResource(id = R.drawable.ic_moon),
                contentDescription = null
            )

            Text(
                modifier = Modifier.constrainAs(text) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(icon.end, 16.dp)
                },
                text = "حالت شب",
                fontSize = 14.sp,
                lineHeight = 24.sp,
                fontWeight = FontWeight.W500,
                color = appColors[8]
            )

            HomeDrawerSwitch(
                modifier = Modifier.constrainAs(switch) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end, 16.dp)
                },
                currentState = currentTheme == ThemeData.DarkTheme,
                checkedTrackColor = appColors[0],
                uncheckedTrackColor = Color(0xFFAFB2B8),
                width = 36.dp,
                height = 20.dp,
                scale = 1f
            ) {
                changeThemeClicked.invoke(it)
            }

        }
    }
}

@Composable
fun HomeDrawerSwitch(
    canChange: MutableState<Boolean> = mutableStateOf(true),
    modifier: Modifier,
    currentState: Boolean,
    scale: Float = 2f,
    width: Dp = 36.dp,
    height: Dp = 20.dp,
    checkedTrackColor: Color = Color(0xFF35898F),
    uncheckedTrackColor: Color = Color(0xFFe0e0e0),
    thumbColor: Color = Color.White,
    gapBetweenThumbAndTrackEdge: Dp = 4.dp,
    onChangeValueClicked: (Boolean) -> Unit
) {
    val context = LocalContext.current
    val switchON = remember { mutableStateOf(currentState) }
    val thumbRadius = (height / 2) - gapBetweenThumbAndTrackEdge
    val animatePosition =
        animateFloatAsState(targetValue = if (switchON.value) with(LocalDensity.current) { (width - thumbRadius - gapBetweenThumbAndTrackEdge).toPx() } else with(
            LocalDensity.current
        ) { (thumbRadius + gapBetweenThumbAndTrackEdge).toPx() })

    Canvas(
        modifier = modifier
            .size(width = width, height = height)
            .scale(scale = scale)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {

                        if (canChange.value) {
                            switchON.value = !switchON.value
                            onChangeValueClicked.invoke(switchON.value)
                        } else {
                            context.showToast("لطفا صبر کنید")
                        }

                    }
                )
            }
    ) {

        // Track
        drawRoundRect(
            color = if (switchON.value) checkedTrackColor else uncheckedTrackColor,
            cornerRadius = CornerRadius(x = 10.dp.toPx(), y = 10.dp.toPx())
        )

        // Thumb
        drawCircle(
            color = thumbColor,
            radius = thumbRadius.toPx(),
            center = Offset(
                x = animatePosition.value,
                y = size.height / 2
            )
        )

    }

}


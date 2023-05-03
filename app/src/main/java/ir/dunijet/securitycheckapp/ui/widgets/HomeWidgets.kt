package ir.dunijet.securitycheckapp.ui.widgets

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import ir.dunijet.securitycheckapp.R
import ir.dunijet.securitycheckapp.ui.MainActivity
import ir.dunijet.securitycheckapp.ui.MainActivity.Companion.appColors
import ir.dunijet.securitycheckapp.ui.MemberId
import ir.dunijet.securitycheckapp.ui.theme.*
import ir.dunijet.securitycheckapp.util.*
import java.util.*

@Composable
fun HomeDrawer(
    themeData: ThemeData,
    onCloseDrawer: () -> Unit,
    onChangeTheme: (ThemeData) -> Unit
) {

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

        }

        DrawerItem(title = "ریموت\u200Cهای دستگاه", icon = R.drawable.ic_remote) {

        }

        DrawerItem(title = "زون\u200Cهای سیم\u200Cدار", icon = R.drawable.ic_eye) {

        }

        DrawerItem(title = "زون\u200Cهای بی\u200Cسیم", icon = R.drawable.ic_eye) {

        }

        DrawerItem(title = "آژیرها", icon = R.drawable.ic_ring) {

        }

        DrawerItem(title = "خروجی\u200Cهای دستگاه", icon = R.drawable.ic_output) {

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
    val state = remember { mutableStateOf(homeVaziat) }
    val interactionSource = remember { MutableInteractionSource() }

    Card(
        elevation = 12.dp,
        shape = RoundedCornerShape(8.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .background(appColors[12])
        ) {

            Text(
                modifier = Modifier.padding(top = 12.dp, start = 18.dp),
                text = "وضعیت دستگاه",
                fontWeight = FontWeight.W500,
                fontSize = 12.sp,
                lineHeight = 20.sp,
                color = appColors[1],
            )

            Row(
                Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .padding(4.dp),
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
                            state.value = HomeVaziat.Faal
                            onChangeVaziatClicked.invoke(state.value)
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
                            state.value = HomeVaziat.NimeFaal
                            onChangeVaziatClicked.invoke(state.value)
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
                            state.value = HomeVaziat.GheirFaal
                            onChangeVaziatClicked.invoke(state.value)
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

            Divider(color = appColors[1].copy(alpha = 0.32f), thickness = 1.dp)

            Row(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(top = 12.dp, bottom = 12.dp, start = 16.dp)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        onUpdateClicked.invoke()
                    },
            ) {

                Icon(painter = painterResource(id = R.drawable.ic_refresh), contentDescription = "")
                Text(
                    text = "آخرین بروزرسانی:",
                    fontSize = 12.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.W400,
                    color = appColors[1]
                )
                Text(
                    text = lastUpdated,
                    fontSize = 12.sp,
                    lineHeight = 20.sp,
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
                color = drawerItemColor
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
                color = drawerItemColor
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
                        switchON.value = !switchON.value
                        onChangeValueClicked.invoke(switchON.value)
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


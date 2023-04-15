package ir.dunijet.securitycheckapp.ui.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import ir.dunijet.securitycheckapp.R
import ir.dunijet.securitycheckapp.ui.MainActivity
import ir.dunijet.securitycheckapp.ui.MainActivity.Companion.appColors
import ir.dunijet.securitycheckapp.ui.MemberId
import ir.dunijet.securitycheckapp.ui.theme.ColorFaal
import ir.dunijet.securitycheckapp.ui.theme.colorGheirFaal
import ir.dunijet.securitycheckapp.ui.theme.colorNimeFaal
import ir.dunijet.securitycheckapp.util.*
import java.util.*

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
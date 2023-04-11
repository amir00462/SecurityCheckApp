package ir.dunijet.securitycheckapp.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import ir.dunijet.securitycheckapp.R
import ir.dunijet.securitycheckapp.model.data.Zone
import ir.dunijet.securitycheckapp.ui.MainActivity
import ir.dunijet.securitycheckapp.ui.MemberId
import ir.dunijet.securitycheckapp.ui.MyMenu
import ir.dunijet.securitycheckapp.ui.theme.*
import ir.dunijet.securitycheckapp.util.ZoneType

@Composable
fun AlarmZaman(
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
                    painter = painterResource(id = R.drawable.ic_clock),
                    contentDescription = null,
                    tint = MainActivity.appColors[6]
                )
                Text(
                    modifier = Modifier.padding(start = 10.dp, top = 8.dp),
                    text = "زمان صدای آژیرها",
                    style = MaterialTheme.typography.body1,
                    color = MainActivity.appColors[6]
                )
            }

            Text(
                modifier = Modifier.padding(end = 10.dp, top = 8.dp),
                text = selectedValue.toInt().toString() + " دقیقه" ,
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
                    valueRange = 1f..9.9f,
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
fun AlarmChangeOnOff(
    modifier: Modifier,
    titlee: String,
    value: Boolean,
    onValueChanged: (Boolean) -> Unit
) {

    val interactionSource = remember { MutableInteractionSource() }
    var selectedPart by remember { mutableStateOf(value) }

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
            val (title, icon) = createRefs()

            Icon(
                modifier = Modifier
                    .constrainAs(icon) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
                    .padding(start = 10.dp, top = 10.dp),
                painter = painterResource(id = R.drawable.ic_warning),
                contentDescription = null,
                tint =
                MainActivity.appColors[6]
            )

            Text(
                modifier = Modifier
                    .constrainAs(title) {
                        top.linkTo(icon.top)
                        start.linkTo(icon.end)
                        bottom.linkTo(icon.bottom)
                    }
                    .padding(start = 10.dp, top = 8.dp),
                fontWeight = FontWeight.Medium,
                lineHeight = 24.sp,
                color = MainActivity.appColors[6],
                text = titlee
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

            // gheir faal
            Box(
                Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(MaterialTheme.shapes.small)
                    .background(
                        if (selectedPart != false) Color.Transparent
                        else MainActivity.appColors[1]
                    )
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        selectedPart = false
                        onValueChanged.invoke(selectedPart)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "غیر فعال",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = if (selectedPart != false) MainActivity.appColors[6]
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
                        if (selectedPart) MainActivity.appColors[1]
                        else Color.Transparent
                    )
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        selectedPart = true
                        onValueChanged.invoke(selectedPart)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "فعال",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = if (selectedPart) ColorFaal
                    else MainActivity.appColors[6]
                )
            }


        }


    }

}

@Composable
fun AlarmBolandi(
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
                    text = "بلندی صدای آژیر داخلی",
                    color = MainActivity.appColors[6] ,
                    style = MaterialTheme.typography.body1
                )
            }

            Text(
                modifier = Modifier.padding(end = 10.dp, top = 12.dp),
                text = selectedValue.toInt().toString(),
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
                    valueRange = 0f..99.9f,
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

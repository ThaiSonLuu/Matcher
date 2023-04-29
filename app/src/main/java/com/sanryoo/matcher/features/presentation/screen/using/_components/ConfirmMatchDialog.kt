package com.sanryoo.matcher.features.presentation.screen.using._components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import coil.compose.rememberAsyncImagePainter
import com.sanryoo.matcher.R
import com.sanryoo.matcher.features.domain.model.User
import com.sanryoo.matcher.ui.theme.DeepSkyBlue1
import com.sanryoo.matcher.ui.theme.SecondBackgroundLight
import kotlinx.coroutines.delay

@Composable
fun ConfirmMatchDialog(
    others: User = User(), confirm: () -> Unit = {}, cancel: () -> Unit = {}
) {
    val context = LocalContext.current
    val listStatus = listOf(
        context.getString(R.string.single),
        context.getString(R.string.single_mom_dad),
        context.getString(R.string.divorced_and_no_children),
        context.getString(R.string.divorced_and_have_children)
    )
    var currentTime by remember { mutableStateOf(60000L) }
    var isConfirmed by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        while (currentTime > 0) {
            if (!isConfirmed) {
                delay(100L)
                currentTime -= 100L
                if (currentTime <= 0) {
                    cancel()
                }
            } else {
                currentTime = 60000L
                return@LaunchedEffect
            }
        }
    }
    AlertDialog(
        onDismissRequest = {
            if (!isConfirmed) {
                cancel()
            }
        },
        properties = DialogProperties(usePlatformDefaultWidth = false),
        backgroundColor = MaterialTheme.colors.background,
        buttons = {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                if (isConfirmed) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .fillMaxHeight(0.5f)
                                .aspectRatio(1f),
                            strokeWidth = 3.dp
                        )
                        Spacer(modifier = Modifier.width(15.dp))
                        Text(text = "${context.getString(R.string.waiting_for)} ${others.fullname}")
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(modifier = Modifier.padding(20.dp)) {
                        Image(
                            painter = rememberAsyncImagePainter(model = others.avatar),
                            contentDescription = "Other's avatar",
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(MaterialTheme.colors.surface)
                                .size(100.dp)
                                .border(
                                    width = 3.dp, color = DeepSkyBlue1, shape = CircleShape
                                ),
                            contentScale = ContentScale.Crop
                        )
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .align(Alignment.BottomEnd)
                                .background(MaterialTheme.colors.background, CircleShape)
                                .clip(CircleShape)
                                .border(
                                    width = 2.dp, color = DeepSkyBlue1, shape = CircleShape
                                ), contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = if (others.sex == 1) R.drawable.male else R.drawable.female),
                                contentDescription = "Sex icon",
                                contentScale = ContentScale.Inside,
                                modifier = Modifier.size(17.dp)
                            )
                        }
                    }
                    Text(
                        text = others.fullname,
                        fontSize = 22.sp,
                        style = TextStyle(fontWeight = FontWeight.Medium)
                    )
                    Row(
                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 10.dp),
                    ) {
                        Text(
                            text = "${others.age} ${context.getString(R.string.years_old)}",
                            fontSize = 16.sp,
                            style = TextStyle(fontWeight = FontWeight.Normal)
                        )
                        Text(
                            text = "-",
                            fontSize = 16.sp,
                            style = TextStyle(fontWeight = FontWeight.Normal),
                            modifier = Modifier.padding(horizontal = 10.dp)
                        )
                        Image(
                            painter = painterResource(id = R.drawable.distance),
                            contentDescription = "Icon distance",
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "${String.format("%.1f", others.currentdistance)} km",
                            fontSize = 16.sp,
                            style = TextStyle(fontWeight = FontWeight.Normal)
                        )
                    }
                    Row {
                        Image(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(3 / 5f)
                                .padding(7.dp)
                                .clip(RoundedCornerShape(5.dp)),
                            painter = rememberAsyncImagePainter(model = others.image1),
                            contentDescription = "",
                            contentScale = ContentScale.Crop
                        )
                        Image(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(3 / 5f)
                                .padding(7.dp)
                                .clip(RoundedCornerShape(5.dp)),
                            painter = rememberAsyncImagePainter(model = others.image2),
                            contentDescription = "",
                            contentScale = ContentScale.Crop
                        )
                        Image(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(3 / 5f)
                                .padding(7.dp)
                                .clip(RoundedCornerShape(5.dp)),
                            painter = rememberAsyncImagePainter(model = others.image3),
                            contentDescription = "",
                            contentScale = ContentScale.Crop
                        )
                    }
                    ItemInformation(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 30.dp, vertical = 10.dp)
                            .height(50.dp),
                        icon = R.drawable.alone,
                        text = "${context.getString(R.string.status)}: ${listStatus[others.status - 1]}",
                        showIconNext = false
                    )
                    ItemInformation(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 30.dp, vertical = 10.dp)
                            .height(50.dp),
                        icon = R.drawable.height,
                        text = "${context.getString(R.string.height)}: " + if (others.height > 0) "${others.height} cm" else "",
                        showIconNext = false
                    )
                    ItemInformation(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 30.dp, vertical = 10.dp)
                            .height(50.dp),
                        icon = R.drawable.weight,
                        text = "${context.getString(R.string.weight)}: " + if (others.weight > 0) "${others.weight} kg" else "",
                        showIconNext = false
                    )
                    ItemInformation(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 30.dp, vertical = 10.dp)
                            .height(50.dp),
                        icon = R.drawable.income,
                        text = "${context.getString(R.string.income)}: " + if (others.income > 0) "${others.income},000,000 đ" else context.getString(R.string.no_income),
                        showIconNext = false
                    )
                    ItemInformation(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 30.dp, vertical = 10.dp)
                            .height(50.dp),
                        icon = R.drawable.appearance,
                        text = "${context.getString(R.string.appearance)}: " + if (others.appearance > 0) "${others.appearance} / 10" else "",
                        showIconNext = false
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .background(MaterialTheme.colors.background),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = cancel,
                        shape = RoundedCornerShape(10.dp),
                        enabled = !isConfirmed,
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = context.getString(R.string.cancel))
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxHeight(0.7f)
                            .aspectRatio(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            drawArc(
                                color = SecondBackgroundLight,
                                startAngle = 0f,
                                sweepAngle = 360f,
                                useCenter = false,
                                style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                            )
                            drawArc(
                                color = Color.Green,
                                startAngle = 270f - currentTime.toFloat() / 60000f * 360,
                                sweepAngle = currentTime.toFloat() / 60000f * 360,
                                useCenter = false,
                                style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                            )
                        }
                        if (!isConfirmed) {
                            Text(text = (currentTime / 1000).toString())
                        } else {
                            Image(
                                painter = painterResource(id = R.drawable.tick_confirm),
                                contentDescription = "Tick",
                                modifier = Modifier.size(25.dp)
                            )
                        }
                    }
                    Button(
                        onClick = {
                            confirm()
                            isConfirmed = true
                        },
                        shape = RoundedCornerShape(10.dp),
                        enabled = !isConfirmed,
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = context.getString(R.string.confirm))
                    }
                }
            }
        },
    )
}
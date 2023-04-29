package com.sanryoo.matcher.features.presentation.screen.using.profile.tag_profile

import androidx.compose.animation.*
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sanryoo.matcher.R
import com.sanryoo.matcher.features.presentation.screen.using._components.ItemCard
import com.sanryoo.matcher.features.presentation.screen.using._components.ItemInformation
import com.sanryoo.matcher.features.presentation.screen.using.profile.ProfileViewModel

@Composable
fun TabRequire(viewModel: ProfileViewModel) {
    val context = LocalContext.current
    val listStatus = listOf(
        context.getString(R.string.single),
        context.getString(R.string.single_mom_dad),
        context.getString(R.string.divorced_and_no_children),
        context.getString(R.string.divorced_and_have_children)
    )
    val user = viewModel.user.collectAsState().value
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        ItemInformation(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp, vertical = 10.dp)
                .height(50.dp), icon = R.drawable.distance, annotatedText = buildAnnotatedString {
                append("${context.getString(R.string.distance_within)}: ")
                withStyle(
                    style = SpanStyle(
                        fontSize = 22.sp, fontWeight = FontWeight.SemiBold
                    )
                ) {
                    append("${user.distance} km")
                }
            }, onClick = viewModel::showChooseDistance
        )
        ItemCard(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp, vertical = 10.dp)
            .height(50.dp),
            icon = if (user.easy == 1) R.drawable.checked else R.drawable.unchecked,
            value = if (user.sex == 2) context.getString(R.string.just_a_boy) else context.getString(
                R.string.just_a_girl
            ),
            onClick = { viewModel.chooseEasy(1) })
        ItemCard(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp, vertical = 10.dp)
            .height(50.dp),
            icon = if (user.easy == 0) R.drawable.checked else R.drawable.unchecked,
            value = context.getString(R.string.other_requirements),
            onClick = { viewModel.chooseEasy(0) })
        AnimatedVisibility(
            visible = user.easy == 0, enter = slideIn(
                initialOffset = {
                    IntOffset(0, it.height)
                }, animationSpec = tween(durationMillis = 500, easing = LinearEasing)
            ) + fadeIn(), exit = slideOut(
                targetOffset = {
                    IntOffset(0, it.height)
                }, animationSpec = tween(durationMillis = 500, easing = LinearEasing)
            ) + fadeOut()
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ItemInformation(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp, vertical = 10.dp)
                    .height(50.dp),
                    icon = R.drawable.alone,
                    annotatedText = buildAnnotatedString {
                        append("${context.getString(R.string.your_are_finding)}: ")
                        withStyle(
                            style = SpanStyle(
                                fontSize = 22.sp, fontWeight = FontWeight.SemiBold
                            )
                        ) {
                            append(listStatus[user.status1 - 1])
                        }
                    },
                    onClick = { viewModel.showChooseStatus(1) })
                ItemInformation(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp, vertical = 10.dp)
                    .height(50.dp),
                    icon = R.drawable.age,
                    annotatedText = buildAnnotatedString {
                        append("${context.getString(R.string.age)}: ${context.getString(R.string.from)} ")
                        withStyle(
                            style = SpanStyle(
                                fontSize = 22.sp, fontWeight = FontWeight.SemiBold
                            )
                        ) {
                            append(user.age1.toString())
                        }
                        append(" ${context.getString(R.string.to)} ")
                        withStyle(
                            style = SpanStyle(
                                fontSize = 22.sp, fontWeight = FontWeight.SemiBold
                            )
                        ) {
                            append(user.age2.toString())
                        }
                        append(" ${context.getString(R.string.years_old)}")
                    },
                    onClick = { viewModel.showChooseAge(1) })
                ItemInformation(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp, vertical = 10.dp)
                    .height(50.dp),
                    icon = R.drawable.height,
                    annotatedText = buildAnnotatedString {
                        append("${context.getString(R.string.height)}: ${context.getString(R.string.from)} ")
                        withStyle(
                            style = SpanStyle(
                                fontSize = 22.sp, fontWeight = FontWeight.SemiBold
                            )
                        ) {
                            append(user.height1.toString())
                        }
                        append(" ${context.getString(R.string.to)} ")
                        withStyle(
                            style = SpanStyle(
                                fontSize = 22.sp, fontWeight = FontWeight.SemiBold
                            )
                        ) {
                            append(user.height2.toString())
                        }
                        append(" cm")
                    },
                    onClick = { viewModel.showChooseHeight(1) })
                ItemInformation(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp, vertical = 10.dp)
                    .height(50.dp),
                    icon = R.drawable.weight,
                    annotatedText = buildAnnotatedString {
                        append("${context.getString(R.string.weight)}: ${context.getString(R.string.from)} ")
                        withStyle(
                            style = SpanStyle(
                                fontSize = 22.sp, fontWeight = FontWeight.SemiBold
                            )
                        ) {
                            append(user.weight1.toString())
                        }
                        append(" ${context.getString(R.string.to)} ")
                        withStyle(
                            style = SpanStyle(
                                fontSize = 22.sp, fontWeight = FontWeight.SemiBold
                            )
                        ) {
                            append(user.weight2.toString())
                        }
                        append(" kg")
                    },
                    onClick = { viewModel.showChooseWeight(1) })
                ItemInformation(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp, vertical = 10.dp)
                    .height(50.dp),
                    icon = R.drawable.income,
                    annotatedText = buildAnnotatedString {
                        append("${context.getString(R.string.income)}: ${context.getString(R.string.more_than)} ")
                        withStyle(
                            style = SpanStyle(
                                fontSize = 22.sp, fontWeight = FontWeight.SemiBold
                            )
                        ) {
                            append(user.income1.toString())
                            if (user.income1 > 0) append(",000,000")
                        }
                        append(" đ")
                    },
                    onClick = { viewModel.showChooseIncome(1) })
                ItemInformation(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp, vertical = 10.dp)
                    .height(50.dp),
                    icon = R.drawable.appearance,
                    annotatedText = buildAnnotatedString {
                        append("${context.getString(R.string.appearance)}: ${context.getString(R.string.more_than)} ")
                        withStyle(
                            style = SpanStyle(
                                fontSize = 22.sp, fontWeight = FontWeight.SemiBold
                            )
                        ) {
                            append(user.appearance1.toString())
                        }
                        append("/ 10")
                    },
                    onClick = { viewModel.showChooseAppearance(1) })
            }
        }
    }
}
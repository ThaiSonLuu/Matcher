package com.sanryoo.matcher.features.presentation.screen.using._bottomsheet.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sanryoo.matcher.R
import com.sanryoo.matcher.features.presentation.screen.using.profile.ProfileViewModel
import com.sanryoo.matcher.ui.theme.Primary

@Composable
fun ChooseIncome(
    viewModel: ProfileViewModel
) {
    val context = LocalContext.current
    val chooseIncome = viewModel.chooseIncome.collectAsState().value
    val user = viewModel.user.collectAsState().value
    val current = when (chooseIncome) {
        0 -> user.income
        else -> user.income1
    }
    val state = rememberLazyListState(initialFirstVisibleItemIndex = current)
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(Primary),
            contentAlignment = Alignment.Center
        ) {
            IconButton(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 15.dp)
                    .size(20.dp),
                onClick = {
                    viewModel.onUiEvent(ProfileViewModel.UiEvent.BottomSheet(false))
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.close),
                    contentDescription = "Icon Close",
                    tint = Color.White
                )
            }
            Text(
                text = when (chooseIncome) {
                    0 -> context.getString(R.string.income)
                    else -> context.getString(R.string.more_than)
                },
                fontSize = 26.sp,
                modifier = Modifier.padding(10.dp),
                color = Color.White
            )
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            state = state
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp)
                        .clickable(
                            interactionSource = MutableInteractionSource(),
                            indication = null,
                            onClick = { viewModel.chooseIncome(0) }
                        ),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = context.getString(R.string.no_income),
                        modifier = Modifier.padding(vertical = 15.dp),
                    )
                    if (current == 0) {
                        Image(
                            painter = painterResource(id = R.drawable.tick),
                            contentDescription = "Icon Tick",
                            modifier = Modifier
                                .padding(horizontal = 10.dp)
                                .size(30.dp)
                        )
                    }
                }
                Divider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 0.3.dp,
                    color = Color.Gray
                )
            }
            for (i in 1..30) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp)
                            .clickable(
                                interactionSource = MutableInteractionSource(),
                                indication = null,
                                onClick = { viewModel.chooseIncome(i) }
                            ),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "$i,000,000 đ",
                            modifier = Modifier.padding(vertical = 15.dp),
                        )
                        if (current == i) {
                            Image(
                                painter = painterResource(id = R.drawable.tick),
                                contentDescription = "Icon Tick",
                                modifier = Modifier
                                    .padding(horizontal = 10.dp)
                                    .size(30.dp)
                            )
                        }
                    }
                    Divider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = 0.3.dp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}
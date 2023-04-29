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
fun ChooseWeight(
    viewModel: ProfileViewModel
) {
    val context = LocalContext.current
    val chooseWeight = viewModel.chooseWeight.collectAsState().value
    val user = viewModel.user.collectAsState().value
    val current = when (chooseWeight) {
        0 -> user.weight
        1 -> user.weight1
        else -> user.weight2
    }
    val state = rememberLazyListState(
        initialFirstVisibleItemIndex = when (chooseWeight) {
            0 -> if (user.weight > 44) user.weight - 44 else 0
            1 -> if (user.weight1 > 44) user.weight1 - 44 else 0
            else -> if (user.weight2 > user.weight1) user.weight2 - user.weight1 - 1 else 0
        }
    )
    Column(modifier = Modifier.fillMaxSize()) {
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
                    contentDescription = "",
                    tint = Color.White
                )
            }
            Text(
                text = when (chooseWeight) {
                    0 -> context.getString(R.string.weight)
                    1 -> context.getString(R.string.from)
                    else -> context.getString(R.string.to)
                },
                fontSize = 26.sp,
                modifier = Modifier.padding(10.dp),
                color = Color.White
            )
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = state
        ) {
            for (i in 40..120) {
                if (chooseWeight == 2) {
                    if (i <= user.weight1)
                        continue
                }
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp)
                            .clickable(
                                interactionSource = MutableInteractionSource(),
                                indication = null,
                                onClick = {viewModel.chooseWeight(i)}
                            ),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "$i kg",
                            modifier = Modifier.padding(vertical = 15.dp),
                        )
                        if (current == i) {
                            Image(
                                painter = painterResource(id = R.drawable.tick),
                                contentDescription = "",
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
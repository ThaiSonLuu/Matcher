package com.sanryoo.matcher.features.presentation.screen.using._bottomsheet.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
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

@ExperimentalMaterialApi
@Composable
fun ChooseStatus(viewModel: ProfileViewModel) {
    val context = LocalContext.current
    val listStatus = listOf(
        context.getString(R.string.single),
        context.getString(R.string.single_mom_dad),
        context.getString(R.string.divorced_and_no_children),
        context.getString(R.string.divorced_and_have_children)
    )
    val chooseStatus = viewModel.chooseStatus.collectAsState().value
    val user = viewModel.user.collectAsState().value
    val current = when (chooseStatus) {
        0 -> user.status
        else -> user.status1
    }
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
            IconButton(modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 15.dp)
                .size(20.dp),
                onClick = {
                    viewModel.onUiEvent(ProfileViewModel.UiEvent.BottomSheet(false))
                }) {
                Icon(
                    painter = painterResource(id = R.drawable.close),
                    contentDescription = "Icon Close",
                    tint = Color.White
                )
            }
            Text(
                text = when (chooseStatus) {
                    0 -> context.getString(R.string.you_are)
                    else -> context.getString(R.string.your_are_finding)
                }, fontSize = 26.sp, modifier = Modifier.padding(10.dp), color = Color.White
            )
        }
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(listStatus.size) { index ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp)
                        .clickable(interactionSource = MutableInteractionSource(),
                            indication = null,
                            onClick = { viewModel.chooseStatus(index + 1) }),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = listStatus[index],
                        modifier = Modifier.padding(vertical = 15.dp),
                    )
                    if (current == index + 1) {
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
                    modifier = Modifier.fillMaxWidth(), thickness = 0.3.dp, color = Color.Gray
                )
            }
        }
    }
}
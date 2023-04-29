package com.sanryoo.matcher.features.presentation.screen.using.menu

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.sanryoo.matcher.R
import com.sanryoo.matcher.features.presentation.screen.using._components.ItemMenu
import com.sanryoo.matcher.features.util.Screen
import kotlinx.coroutines.flow.collectLatest

@Composable
fun MenuScreen(
    rootNavController: NavHostController,
    navController: NavHostController,
    viewModel: MenuViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is MenuViewModel.UiEvent.ChangePassword -> {
                    navController.navigate(Screen.Password.route)
                }
                is MenuViewModel.UiEvent.About -> {
                    navController.navigate(Screen.AboutScreen.route)
                }
                is MenuViewModel.UiEvent.LogOut -> {
                    rootNavController.navigate(Screen.OptionLogin.route) {
                        launchSingleTop = true
                    }
                }
            }
        }
    }
    MenuContent(viewModel)
}

@Composable
fun MenuContent(viewModel: MenuViewModel) {
    val context = LocalContext.current
    val user = viewModel.user.collectAsState().value
    Column(modifier = Modifier.fillMaxSize()) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                if (user.idgoogle == "" && user.idfacebook == "") {
                    ItemMenu(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        icon = R.drawable.password,
                        text = context.getString(R.string.change_password)
                    ) { viewModel.onUiEvent(MenuViewModel.UiEvent.ChangePassword) }
                    Divider(modifier = Modifier.padding(start = 60.dp, end = 10.dp))
                }
                ItemMenu(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    icon = R.drawable.about,
                    text = context.getString(R.string.about)
                ) {
                    viewModel.onUiEvent(MenuViewModel.UiEvent.About)
                }
            }
        }
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            shape = RoundedCornerShape(20.dp)
        ) {
            ItemMenu(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                icon = R.drawable.logout,
                text = context.getString(R.string.log_out),
                onClick = viewModel::logOut,
                contentColor = Color.Red
            )
        }
    }
}
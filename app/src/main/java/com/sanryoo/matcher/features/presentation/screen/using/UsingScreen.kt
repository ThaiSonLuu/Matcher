package com.sanryoo.matcher.features.presentation.screen.using

import android.Manifest
import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.sanryoo.matcher.R
import com.sanryoo.matcher.features.presentation.screen._components.SnackBar
import com.sanryoo.matcher.features.presentation.screen.using._bottomsheet.BottomSheet
import com.sanryoo.matcher.features.presentation.screen.using._components.CustomBottomNavigation
import com.sanryoo.matcher.features.presentation.screen.using.about.AboutScreen
import com.sanryoo.matcher.features.presentation.screen.using.change_password.ChangePassword
import com.sanryoo.matcher.features.presentation.screen.using.home.HomeScreen
import com.sanryoo.matcher.features.presentation.screen.using.menu.MenuScreen
import com.sanryoo.matcher.features.presentation.screen.using.message.MessageScreen
import com.sanryoo.matcher.features.presentation.screen.using.others.OthersScreen
import com.sanryoo.matcher.features.presentation.screen.using.profile.ProfileScreen
import com.sanryoo.matcher.features.presentation.screen.using.profile.ProfileViewModel
import com.sanryoo.matcher.features.util.Screen
import com.sanryoo.matcher.ui.theme.Primary
import com.sanryoo.matcher.ui.theme.SecondContentDark
import com.sanryoo.matcher.ui.theme.SecondContentLight
import kotlinx.coroutines.flow.collectLatest

@ExperimentalPermissionsApi
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun UsingScreen(
    rootNavController: NavHostController,
    viewModel: UsingViewModel = hiltViewModel()
) {
    //Profile screen need to use bottom sheet
    val profileViewModel: ProfileViewModel = hiltViewModel()
    val permissionLocation = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        ),
        onPermissionsResult = {
            if (
                it[Manifest.permission.ACCESS_COARSE_LOCATION] == true &&
                it[Manifest.permission.ACCESS_FINE_LOCATION] == true
            ) {
                viewModel.updateLocation()
            }
        }
    )
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        animationSpec = tween(500),
        confirmStateChange = { it != ModalBottomSheetValue.HalfExpanded }
    )
    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState()
    navController.enableOnBackPressed(!sheetState.isVisible)
    LaunchedEffect(Unit) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UsingViewModel.UiEvent.LaunchLocationPermission -> {
                    if (!permissionLocation.allPermissionsGranted) {
                        permissionLocation.launchMultiplePermissionRequest()
                    } else if (permissionLocation.allPermissionsGranted) {
                        viewModel.updateLocation()
                    }
                }
                is UsingViewModel.UiEvent.ShowSnackBar -> {
                    scaffoldState.snackbarHostState.showSnackbar(event.message, event.actionLabel)
                }
                is UsingViewModel.UiEvent.NavigateHome -> {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) {
                            inclusive = true
                        }
                    }
                }
            }
        }
    }
    BottomSheet(
        sheetState = sheetState,
        profileViewModel = profileViewModel
    ) {
        Scaffold(
            scaffoldState = scaffoldState,
            snackbarHost = { SnackbarHost(it) { data -> SnackBar(data) } },
            bottomBar = { CustomBottomNavigation(navController) }
        ) {
            NavHost(
                navController = navController,
                startDestination = Screen.Home.route
            ) {
                composable(Screen.Home.route) {
                    HomeScreen(navController, scaffoldState)
                }
                composable(Screen.Profile.route) {
                    ProfileScreen(sheetState, scaffoldState, profileViewModel)
                }
                composable(Screen.Menu.route) {
                    MenuScreen(rootNavController, navController)
                }
                composable(Screen.Message.route) {
                    MessageScreen(navController, scaffoldState)
                }
                composable(Screen.OtherInformation.route) {
                    OthersScreen(navController)
                }
                composable(Screen.Password.route) {
                    ChangePassword(navController, scaffoldState)
                }
                composable(Screen.AboutScreen.route) {
                    AboutScreen(navController)
                }
            }
        }
        BackHandler(
            enabled = sheetState.isVisible,
            onBack = { profileViewModel.onUiEvent(ProfileViewModel.UiEvent.BottomSheet(false)) }
        )
    }
    val reLogging = viewModel.reLogging.collectAsState().value
    val dialogReLogIn = viewModel.dialogReLogin.collectAsState().value
    val inputPassword = viewModel.inputPassword.collectAsState().value

    if (reLogging) {
        AlertDialog(
            onDismissRequest = {},
            backgroundColor = Color.Transparent,
            buttons = {
                CircularProgressIndicator(
                    modifier = Modifier.size(50.dp),
                    strokeWidth = 4.dp,
                    color = Primary
                )
            }
        )
    }

    if (dialogReLogIn) {
        InputPasswordForReLogin(
            inputPassword = inputPassword,
            onInputPasswordChange = viewModel::onInputPasswordChange,
            onReLogin = {
                viewModel.hideDialogReLogin()
                viewModel.reLogIn(inputPassword)
                viewModel.onInputPasswordChange("")
            }
        )
    }
}

@ExperimentalComposeUiApi
@Composable
fun InputPasswordForReLogin(
    inputPassword: String = "",
    onInputPasswordChange: (String) -> Unit = {},
    onReLogin: () -> Unit = {}
) {
    val context = LocalContext.current
    AlertDialog(
        backgroundColor = MaterialTheme.colors.surface,
        shape = RoundedCornerShape(20.dp),
        onDismissRequest = {},
        buttons = {
            val keyboardController = LocalSoftwareKeyboardController.current
            val focusManager = LocalFocusManager.current
            val focusRequester = remember { FocusRequester() }
            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = context.getString(R.string.re_log_in),
                    fontSize = 20.sp,
                    color = Color.Blue,
                    modifier = Modifier.padding(20.dp),
                    style = TextStyle(textAlign = TextAlign.Center)
                )
                Box(
                    modifier = Modifier
                        .padding(horizontal = 40.dp)
                        .width(180.dp)
                        .height(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colors.background
                        )
                        .border(
                            width = 2.dp,
                            color = Color.Blue,
                            shape = RoundedCornerShape(10.dp)
                        ),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    if (inputPassword.isEmpty()) {
                        Text(
                            text = context.getString(R.string.password),
                            color = if (isSystemInDarkTheme()) SecondContentDark else SecondContentLight,
                            modifier = Modifier
                                .padding(8.dp)
                                .align(Alignment.CenterStart)
                        )
                    }
                    BasicTextField(
                        value = inputPassword,
                        onValueChange = onInputPasswordChange,
                        textStyle = TextStyle(
                            color = MaterialTheme.colors.onBackground
                        ),
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .focusRequester(focusRequester),
                        cursorBrush = SolidColor(Primary),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                keyboardController?.hide()
                                focusManager.clearFocus()
                            }
                        ),
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true
                    )
                }
                Row(
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                        .width(260.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = onReLogin,
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.Blue,
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = context.getString(R.string.log_in))
                    }
                }
            }
        }
    )
}
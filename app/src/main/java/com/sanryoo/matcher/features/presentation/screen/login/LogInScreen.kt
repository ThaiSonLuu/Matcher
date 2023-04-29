package com.sanryoo.matcher.features.presentation.screen.login

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.facebook.*
import com.sanryoo.matcher.R
import com.sanryoo.matcher.features.presentation.screen._components.CustomTextField
import com.sanryoo.matcher.features.presentation.screen._components.SnackBar
import com.sanryoo.matcher.features.util.Screen
import com.sanryoo.matcher.ui.theme.Primary
import com.sanryoo.matcher.ui.theme.SecondPrimary
import kotlinx.coroutines.flow.collectLatest
import java.util.*


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@ExperimentalComposeUiApi
@Composable
fun LogInScreen(
    navController: NavHostController,
    viewModel: LogInViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    LaunchedEffect(Unit) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is LogInViewModel.UiEvent.ShowSnackBar -> {
                    scaffoldState.snackbarHostState.showSnackbar(message = event.message)
                }
                is LogInViewModel.UiEvent.HideKeyBoard -> {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                }
                is LogInViewModel.UiEvent.LogInSuccess -> {
                    navController.navigate(Screen.Using.route) {
                        launchSingleTop = true
                    }
                }
                is LogInViewModel.UiEvent.NavigateBack -> {
                    navController.popBackStack()
                }
            }
        }
    }
    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = { SnackbarHost(it) { data -> SnackBar(data) } },
        content = { LogInContent(viewModel) }
    )
}

@Composable
fun LogInContent(viewModel: LogInViewModel) {
    val context = LocalContext.current
    val inputUser = viewModel.inputUser.collectAsState().value
    val passwordVisible = viewModel.passwordVisible.collectAsState().value
    val loading = viewModel.loading.collectAsState().value
    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.3f)
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Primary,
                            SecondPrimary
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = context.getString(R.string.log_in_with_matcher_account),
                color = Color.White,
                style = TextStyle(
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    textAlign = TextAlign.Center
                ),
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            CustomTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                value = inputUser.username,
                onValueChang = viewModel::onUsernameChanged,
                placeHolder = context.getString(R.string.username),
                icon = R.drawable.close,
                onClickIcon = { viewModel.onUsernameChanged("") },
                imeAction = ImeAction.Next
            )
            CustomTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                value = inputUser.password,
                onValueChang = viewModel::onPasswordChanged,
                placeHolder = context.getString(R.string.password),
                icon = if (passwordVisible) R.drawable.visible_password else R.drawable.invisible_password,
                onClickIcon = viewModel::toggleVisiblePassword,
                keyboardType = KeyboardType.Password,
                onClickImeAction = { viewModel.onUiEvent(LogInViewModel.UiEvent.HideKeyBoard) },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation()
            )
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(vertical = 10.dp),
                enabled = !loading && inputUser.username.isNotBlank() && inputUser.password.isNotBlank(),
                onClick = viewModel::logIn,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                )
            ) {
                if (!loading) {
                    Text(text = context.getString(R.string.log_in))
                } else {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                }
            }
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Divider(
                    color = MaterialTheme.colors.onBackground,
                    thickness = 1.dp,
                    modifier = Modifier.fillMaxWidth(0.6f)
                )
                Text(
                    text = " ${context.getString(R.string.or)} ",
                    modifier = Modifier.background(MaterialTheme.colors.background)
                )
            }
            Button(
                onClick = { viewModel.onUiEvent(LogInViewModel.UiEvent.NavigateBack) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(vertical = 10.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                    disabledBackgroundColor = Primary
                )
            ) {
                Text(text = context.getString(R.string.back))
            }
        }
    }
}
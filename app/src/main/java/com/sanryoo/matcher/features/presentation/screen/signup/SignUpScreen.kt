package com.sanryoo.matcher.features.presentation.screen.signup

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
import com.sanryoo.matcher.R
import com.sanryoo.matcher.features.presentation.screen._components.CustomTextField
import com.sanryoo.matcher.features.presentation.screen._components.SnackBar
import com.sanryoo.matcher.ui.theme.Primary
import com.sanryoo.matcher.ui.theme.SecondPrimary
import kotlinx.coroutines.flow.collectLatest

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@ExperimentalComposeUiApi
@Composable
fun SignUpScreen(
    navController: NavHostController,
    viewModel: SignUpViewModel = hiltViewModel(),
) {
    val scaffoldState = rememberScaffoldState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is SignUpViewModel.UiEvent.ShowSnackBar -> {
                    scaffoldState.snackbarHostState.showSnackbar(message = event.message)
                }
                is SignUpViewModel.UiEvent.NavigateBack -> {
                    navController.popBackStack()
                }
                is SignUpViewModel.UiEvent.HideKeyBoard -> {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                }
            }
        }
    }
    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = { SnackbarHost(it) { data -> SnackBar(data) } },
        content = { SignUpContent(viewModel) }
    )
}

@Composable
fun SignUpContent(viewModel: SignUpViewModel) {

    val context = LocalContext.current
    val signup = viewModel.signup.collectAsState().value
    val state = viewModel.inputSignUp.collectAsState().value
    val loading = viewModel.loading.collectAsState().value

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(if (signup == 0) 0.2f else 0.4f)
                .background(Brush.verticalGradient(listOf(Primary,
                    SecondPrimary
                ))),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = context.getString(R.string.sig_up_an_account),
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
            if (signup == 0) {
                CustomTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    value = state.username,
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
                    value = state.password,
                    onValueChang = viewModel::onPasswordChanged,
                    placeHolder = context.getString(R.string.password),
                    icon = if (state.passwordVisible) R.drawable.visible_password else R.drawable.invisible_password,
                    onClickIcon = viewModel::togglePasswordVisible,
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next,
                    visualTransformation =
                    if (state.passwordVisible) VisualTransformation.None else PasswordVisualTransformation()
                )
                CustomTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    value = state.confirmPassword,
                    onValueChang = viewModel::onConfirmPasswordChanged,
                    placeHolder = context.getString(R.string.confirm_password),
                    icon = if (state.confirmPasswordVisible) R.drawable.visible_password else R.drawable.invisible_password,
                    onClickIcon = viewModel::toggleConfirmPasswordVisible,
                    keyboardType = KeyboardType.Password,
                    onClickImeAction = { viewModel.onUiEvent(SignUpViewModel.UiEvent.HideKeyBoard) },
                    visualTransformation = if (state.confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation()
                )
                Button(
                    onClick = viewModel::signUp,
                    enabled = !loading && state.username.isNotBlank() && state.password.isNotBlank() && state.confirmPassword.isNotBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .padding(vertical = 10.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White,
                    ),
                ) {
                    if (!loading) {
                        Text(text = context.getString(R.string.sign_up))
                    } else {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
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
            } else {
                Text(
                    text = context.getString(R.string.you_can_not_register_a_second_account),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Medium,
                    style = TextStyle(textAlign = TextAlign.Center),
                )
                Spacer(modifier = Modifier.height(100.dp))
            }
            Button(
                onClick = { viewModel.onUiEvent(SignUpViewModel.UiEvent.NavigateBack) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(vertical = 10.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(contentColor = Color.White)
            ) {
                Text(text = context.getString(R.string.back))
            }
        }
    }
}
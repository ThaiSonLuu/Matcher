package com.sanryoo.matcher.features.presentation.screen.using.change_password

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.sanryoo.matcher.R
import com.sanryoo.matcher.features.presentation.screen._components.CustomTextField
import com.sanryoo.matcher.ui.theme.Primary
import kotlinx.coroutines.flow.collectLatest

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ChangePassword(
    navController: NavHostController,
    scaffoldState: ScaffoldState,
    viewModel: ChangePasswordViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is ChangePasswordViewModel.UiEvent.ShowSnackBar -> {
                    scaffoldState.snackbarHostState.showSnackbar(event.message)
                }
                is ChangePasswordViewModel.UiEvent.NavigateBack -> {
                    navController.popBackStack()
                }
            }
        }
    }
    ChangePasswordContent(viewModel)
}

@Composable
fun ChangePasswordContent(viewModel: ChangePasswordViewModel) {
    val context = LocalContext.current
    val state = viewModel.inputState.collectAsState().value
    val loading = viewModel.loading.collectAsState().value
    Column(modifier = Modifier.fillMaxSize()) {
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
                onClick = { viewModel.onUiEvent(ChangePasswordViewModel.UiEvent.NavigateBack) }) {
                Icon(
                    painter = painterResource(id = R.drawable.back),
                    contentDescription = "",
                    tint = Color.White
                )
            }
            Text(
                text = context.getString(R.string.change_password),
                fontSize = 26.sp,
                modifier = Modifier.padding(10.dp),
                color = Color.White
            )
        }
        CustomTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, bottom = 10.dp, start = 40.dp, end = 40.dp),
            value = state.oldPassword,
            onValueChang = viewModel::onOldPasswordChanged,
            placeHolder = context.getString(R.string.old_password),
            icon = if (state.oldPasswordVisible) R.drawable.visible_password else R.drawable.invisible_password,
            onClickIcon = viewModel::toggleOldPasswordVisible,
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Next,
            onClickImeAction = {},
            visualTransformation = if (state.oldPasswordVisible) VisualTransformation.None else PasswordVisualTransformation()
        )
        CustomTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 40.dp),
            value = state.newPassword,
            onValueChang = viewModel::onNewPasswordChanged,
            placeHolder = context.getString(R.string.new_password),
            icon = if (state.newPasswordVisible) R.drawable.visible_password else R.drawable.invisible_password,
            onClickIcon = viewModel::toggleNewPasswordVisible,
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Next,
            onClickImeAction = {},
            visualTransformation = if (state.newPasswordVisible) VisualTransformation.None else PasswordVisualTransformation()
        )
        CustomTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 40.dp),
            value = state.confirmPassword,
            onValueChang = viewModel::onConfirmPasswordChanged,
            placeHolder = context.getString(R.string.confirm_password),
            icon = if (state.confirmPasswordVisible) R.drawable.visible_password else R.drawable.invisible_password,
            onClickIcon = viewModel::toggleConfirmPasswordVisible,
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done,
            onClickImeAction = {},
            visualTransformation = if (state.confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation()
        )
        Button(
            onClick = { viewModel.changePassword() },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Primary,
                contentColor = Color.White,
                disabledBackgroundColor = Primary,
                disabledContentColor = Color.White
            ),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 40.dp)
                .height(50.dp),
            enabled = !loading && state.oldPassword.isNotBlank() && state.newPassword.isNotBlank() && state.confirmPassword.isNotBlank()
        ) {
            if (!loading) {
                Text(text = context.getString(R.string.change))
            } else {
                CircularProgressIndicator(
                    modifier = Modifier.size(25.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            }
        }
    }
}

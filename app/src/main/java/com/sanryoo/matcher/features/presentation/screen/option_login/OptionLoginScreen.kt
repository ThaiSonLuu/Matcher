package com.sanryoo.matcher.features.presentation.screen.option_login

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.facebook.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.sanryoo.matcher.R
import com.sanryoo.matcher.features.presentation.screen._components.SnackBar
import com.sanryoo.matcher.features.util.Screen
import com.sanryoo.matcher.ui.theme.Primary
import com.sanryoo.matcher.ui.theme.SecondPrimary
import kotlinx.coroutines.flow.collectLatest
import java.util.*


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@ExperimentalComposeUiApi
@Composable
fun OptionLoginScreen(
    navController: NavHostController,
    viewModel: OptionLoginViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()
    LaunchedEffect(Unit) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is OptionLoginViewModel.UiEvent.ShowSnackBar -> {
                    scaffoldState.snackbarHostState.showSnackbar(message = event.message)
                }
                is OptionLoginViewModel.UiEvent.LogInSuccess -> {
                    navController.navigate(Screen.Using.route) {
                        launchSingleTop = true
                    }
                }
                is OptionLoginViewModel.UiEvent.Navigate -> {
                    navController.navigate(event.route)
                }
            }
        }
    }
    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = { SnackbarHost(it) { data -> SnackBar(data) } },
        content = { OptionLogInContent(viewModel) }
    )
}

@Composable
fun OptionLogInContent(viewModel: OptionLoginViewModel) {
    val context = LocalContext.current
    val logInWithGoogleLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            if (result.resultCode == ComponentActivity.RESULT_OK) {
                val account = GoogleSignIn.getSignedInAccountFromIntent(result.data).result
                viewModel.logInWithGoogle(account)
            }
        }
    )
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
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_matcher_foreground),
                    contentDescription = "Logo Matcher",
                    modifier = Modifier.size(150.dp)
                )
                Text(
                    text = "Matcher",
                    color = Color.White,
                    style = MaterialTheme.typography.h1
                )
            }

        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Button(
                onClick = { logInWithGoogleLauncher.launch(viewModel.signInClient.signInIntent) },
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
                Row {
                    Image(
                        painter = painterResource(id = R.drawable.logo_google),
                        contentDescription = "Logo Google",
                        modifier = Modifier.size(35.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(text = context.getString(R.string.log_in_with_google))
                }
            }
            Button(
                onClick = {
                    viewModel.logInWithFacebook(context)
                },
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
                Row {
                    Image(
                        painter = painterResource(id = R.drawable.logo_facebook),
                        contentDescription = "Logo Facebook",
                        modifier = Modifier.size(35.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(text = context.getString(R.string.log_in_with_facebook))
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
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(vertical = 10.dp),
                onClick = { viewModel.onUiEvent(OptionLoginViewModel.UiEvent.Navigate(Screen.LogIn.route)) },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                )
            ) {
                Text(text = context.getString(R.string.log_in))
            }
            Button(
                onClick = { viewModel.onUiEvent(OptionLoginViewModel.UiEvent.Navigate(Screen.SignUp.route)) },
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
                Text(text = context.getString(R.string.sign_up))
            }
        }
    }
}
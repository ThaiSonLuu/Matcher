package com.sanryoo.matcher.features.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.core.app.ActivityCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.sanryoo.matcher.features.presentation.screen.login.LogInScreen
import com.sanryoo.matcher.features.presentation.screen.option_login.OptionLoginScreen
import com.sanryoo.matcher.features.presentation.screen.signup.SignUpScreen
import com.sanryoo.matcher.features.presentation.screen.using.UsingScreen
import com.sanryoo.matcher.features.util.Screen
import com.sanryoo.matcher.ui.theme.ToiFATheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@ExperimentalPermissionsApi
@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@AndroidEntryPoint
class MatcherActivity : ComponentActivity() {

    private val viewModel: MatcherViewModal by viewModels()

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepOnScreenCondition {
            viewModel.visibleSplashScreen.value
        }
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this@MatcherActivity, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 0
            )
        }
        setContent {
            ToiFATheme {
                val navController = rememberNavController()
                val user = viewModel.user.collectAsState().value
                NavHost(
                    navController = navController,
                    startDestination = if (user.id > 0) Screen.Using.route else Screen.OptionLogin.route
                ) {
                    composable(Screen.OptionLogin.route) {
                        OptionLoginScreen(navController)
                    }
                    composable(Screen.LogIn.route) {
                        LogInScreen(navController)
                    }
                    composable(Screen.SignUp.route) {
                        SignUpScreen(navController)
                    }
                    composable(Screen.Using.route) {
                        UsingScreen(navController)
                    }
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.saveData()
    }
}
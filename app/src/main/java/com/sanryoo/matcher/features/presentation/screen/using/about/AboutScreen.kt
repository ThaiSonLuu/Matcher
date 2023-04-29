package com.sanryoo.matcher.features.presentation.screen.using.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.sanryoo.matcher.R
import com.sanryoo.matcher.ui.theme.Primary
import kotlinx.coroutines.launch

@Composable
fun AboutScreen(navController: NavHostController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
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
                onClick = { scope.launch { navController.popBackStack() } }) {
                Icon(
                    painter = painterResource(id = R.drawable.back),
                    contentDescription = "Icon Back",
                    tint = Color.White
                )
            }
            Text(
                text = context.getString(R.string.about),
                fontSize = 26.sp,
                modifier = Modifier.padding(10.dp),
                color = Color.White
            )
        }
        Text(
            text = context.getString(R.string.about_app),
            fontSize = 18.sp,
            style = TextStyle(textAlign = TextAlign.Center),
            modifier = Modifier.padding(horizontal = 10.dp)
        )
        Text(
            text = context.getString(R.string.about_me), fontSize = 18.sp,
            style = TextStyle(textAlign = TextAlign.Center),
            modifier = Modifier.padding(horizontal = 10.dp)
        )
        Text(
            text = "${context.getString(R.string.donate)}: ",
            fontSize = 18.sp,
            style = TextStyle(textAlign = TextAlign.Center)
        )
        Image(
            painter = painterResource(id = R.drawable.qr_bidv_luu_thai_son),
            contentDescription = "Qr code",
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .aspectRatio(1f)
        )
    }
}

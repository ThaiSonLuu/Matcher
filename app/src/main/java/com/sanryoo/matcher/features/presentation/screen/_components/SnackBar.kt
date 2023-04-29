package com.sanryoo.matcher.features.presentation.screen._components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.SnackbarData
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sanryoo.matcher.R
import com.sanryoo.matcher.features.util.MyString
import com.sanryoo.matcher.ui.theme.SecondBackgroundDark

@Composable
fun SnackBar(snackBarData: SnackbarData) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 30.dp, horizontal = 20.dp),
        shape = RoundedCornerShape(10.dp),
        color = Color(0xFF444547)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if(snackBarData.actionLabel == MyString.AVAILABLE) {
                Icon(
                    painter = painterResource(id = R.drawable.wifi),
                    contentDescription = "Available",
                    modifier = Modifier.size(40.dp).padding(start =  15.dp),
                    tint = Color.Green
                )
            } else if (snackBarData.actionLabel == MyString.NOT_AVAILABLE) {
                Icon(
                    painter = painterResource(id = R.drawable.no_wifi),
                    contentDescription = "Not Available",
                    modifier = Modifier.size(40.dp).padding(start = 15.dp),
                    tint = Color.Red
                )
            }
            Text(
                text = snackBarData.message,
                color = Color.White,
                modifier = Modifier
                    .padding(15.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}
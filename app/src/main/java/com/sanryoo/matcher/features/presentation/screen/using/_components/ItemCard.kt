package com.sanryoo.matcher.features.presentation.screen.using._components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sanryoo.matcher.R

@Composable
fun ItemCard(
    modifier: Modifier = Modifier,
    icon: Int,
    value: String = "",
    onClick: () -> Unit = {}
) {
    Surface(
        modifier = modifier.clickable(
            interactionSource = MutableInteractionSource(),
            indication = null,
            onClick = onClick
        ),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colors.surface,
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = "",
                modifier = Modifier
                    .fillMaxHeight(0.5f)
                    .padding(horizontal = 10.dp)
                    .aspectRatio(1f),
                tint =
                if (icon == R.drawable.logout)
                    Color.Red
                else {
                    MaterialTheme.colors.onBackground
                }
            )
            Text(
                text = value,
                color =
                if (icon == R.drawable.logout)
                    Color.Red
                else {
                    MaterialTheme.colors.onBackground
                }
            )
        }
    }
}
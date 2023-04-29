package com.sanryoo.matcher.features.presentation.screen.using._components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ItemMenu(
    modifier: Modifier = Modifier,
    icon: Int,
    text: String = "",
    iconSize: Dp = 30.dp,
    contentColor: Color = MaterialTheme.colors.onBackground,
    onClick: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .clickable(
                onClick = onClick,
                interactionSource = MutableInteractionSource(),
                indication = null
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = "",
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .size(iconSize),
            tint = contentColor
        )
        Text(text = text, color = contentColor)
    }
}
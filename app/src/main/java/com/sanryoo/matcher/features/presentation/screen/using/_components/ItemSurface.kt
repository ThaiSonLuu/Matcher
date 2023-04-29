package com.sanryoo.matcher.features.presentation.screen.using._components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape

@Composable
fun ItemSurface(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    color: Color,
    shape: Shape,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null,
                onClick = onClick
            ),
        color = color,
        shape = shape,
    ) {
        content()
    }
}
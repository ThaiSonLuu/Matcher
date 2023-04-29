package com.sanryoo.matcher.features.presentation.screen.using._components

import androidx.compose.foundation.Image
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.sanryoo.matcher.R

@Composable
fun ItemInformation(
    modifier: Modifier = Modifier,
    icon: Int,
    text: String = "",
    annotatedText: AnnotatedString = AnnotatedString(""),
    onClick: () -> Unit = {},
    showIconNext: Boolean = true
) {
    Surface(
        modifier = modifier.clickable(
            interactionSource = MutableInteractionSource(),
            indication = null,
            onClick = onClick
        ),
        shape = RoundedCornerShape(20.dp),
        contentColor = MaterialTheme.colors.onBackground
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = icon),
                    contentDescription = "",
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .fillMaxHeight(0.5f)
                        .aspectRatio(1f)
                )
                if (text != "") {
                    Text(text = text)
                } else {
                    Text(text = annotatedText)
                }
            }
            if (showIconNext) {
                Icon(
                    painter = painterResource(id = R.drawable.next),
                    contentDescription = "Icon next",
                    modifier = Modifier
                        .padding(end = 5.dp)
                        .size(20.dp),
                    tint = Color.Gray
                )
            }
        }
    }
}
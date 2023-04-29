package com.sanryoo.matcher.features.presentation.screen.using._components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.sanryoo.matcher.ui.theme.Primary

@Composable
fun Searching() {
    val transition = rememberInfiniteTransition()
    val redPoint = transition.animateFloat(
        initialValue = -130f,
        targetValue = 230f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 2000,
                easing = CubicBezierEasing(0.5f, 0.2f, 0f, 1f)
            )
        )
    )
    val bluePoint = transition.animateFloat(
        initialValue = -110f,
        targetValue = 250f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 2000,
                easing = CubicBezierEasing(0.4f, 0.2f, 0f, 1f)
            )
        )
    )
    val yellowPoint = transition.animateFloat(
        initialValue = -90f,
        targetValue = 270f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 2000,
                easing = CubicBezierEasing(0.3f, 0.2f, 0f, 1f)
            )
        )
    )
    val greenPoint = transition.animateFloat(
        initialValue = -70f,
        targetValue = 290f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 2000,
                easing = CubicBezierEasing(0.2f, 0.2f, 0f, 1f)
            )
        )
    )
    val pinkPoint = transition.animateFloat(
        initialValue = -50f,
        targetValue = 310f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 2000,
                easing = CubicBezierEasing(0.1f, 0.2f, 0f, 1f)
            )
        )
    )
    Canvas(modifier = Modifier.size(100.dp)) {
        drawArc(
            color = Color.Red,
            startAngle = redPoint.value,
            sweepAngle = 1f,
            useCenter = false,
            style = Stroke(width = 10.dp.toPx(), cap = StrokeCap.Round)
        )
        drawArc(
            color = Color.Blue,
            startAngle = bluePoint.value,
            sweepAngle = 1f,
            useCenter = false,
            style = Stroke(width = 10.dp.toPx(), cap = StrokeCap.Round)
        )
        drawArc(
            color = Color.Yellow,
            startAngle = yellowPoint.value,
            sweepAngle = 1f,
            useCenter = false,
            style = Stroke(width = 10.dp.toPx(), cap = StrokeCap.Round)
        )
        drawArc(
            color = Color.Green,
            startAngle = greenPoint.value,
            sweepAngle = 1f,
            useCenter = false,
            style = Stroke(width = 10.dp.toPx(), cap = StrokeCap.Round)
        )
        drawArc(
            color = Primary,
            startAngle = pinkPoint.value,
            sweepAngle = 1f,
            useCenter = false,
            style = Stroke(width = 10.dp.toPx(), cap = StrokeCap.Round)
        )
    }
}
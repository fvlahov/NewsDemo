package hr.vlahov.newsdemo.utils

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Composable
fun shimmerBrush(
    showShimmer: Boolean,
    shimmerColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    speed: Float = 1f,
    delayBetweenEachShimmerMillis: Int = 0,
): Brush {
    return if (showShimmer) {
        val shimmerColors = listOf(
            shimmerColor.copy(alpha = 0.6f),
            shimmerColor.copy(alpha = 0.2f),
            shimmerColor.copy(alpha = 0.6f),
        )

        val transition = rememberInfiniteTransition(label = "")
        val translateAnimation = transition.animateFloat(
            initialValue = 0f,
            targetValue = 1000f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = (800 * (1 / speed)).toInt(),
                    delayMillis = delayBetweenEachShimmerMillis
                ),
                repeatMode = RepeatMode.Reverse
            ), label = ""
        )
        Brush.linearGradient(
            colors = shimmerColors,
            start = Offset.Zero,
            end = Offset(x = translateAnimation.value, y = translateAnimation.value)
        )
    } else {
        Brush.linearGradient(
            colors = listOf(Color.Transparent, Color.Transparent),
            start = Offset.Zero,
            end = Offset.Zero
        )
    }
}

fun Modifier.shimmerBackground(
    showShimmer: Boolean,
    shape: Shape = RoundedCornerShape(32.dp),
    speed: Float = 1f,
    delayBetweenEachShimmerMillis: Int = 0,
    alpha: Float = 1f,
): Modifier = composed {
    this.background(
        brush = shimmerBrush(
            showShimmer = showShimmer,
            speed = speed,
            delayBetweenEachShimmerMillis = delayBetweenEachShimmerMillis
        ),
        shape = shape,
        alpha = alpha
    )
}
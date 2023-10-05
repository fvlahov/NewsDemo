package hr.vlahov.newsdemo.presentation.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import hr.vlahov.newsdemo.R
import hr.vlahov.newsdemo.ui.theme.LightBlue
import hr.vlahov.newsdemo.utils.ErrorHandler
import kotlinx.coroutines.delay

@Composable
fun SplashScreen() {
    val viewModel = hiltViewModel<SplashScreenViewModel>()
    val error = viewModel.errors.collectAsStateWithLifecycle().value
    ErrorHandler(error = error)
    AnimatedTextScreen(
        text = stringResource(id = R.string.news)
    )
}

@Composable
private fun AnimatedTextScreen(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 40.sp,
    startBottomMargin: Dp = 80.dp,
    speed: Float = 1.0f,
) {
    val horizontalTextWidth = remember { mutableIntStateOf(0) }
    val verticalTextHeight = remember { mutableIntStateOf(0) }

    val horizontalAnimation = remember { Animatable(0f) }
    val verticalAnimation = remember { Animatable(0f) }

    val repeatingText = (0 until 10).joinToString(separator = "       ") { text }
    val repeatingSecondText = (0 until 10).joinToString(separator = "     ") { "Please hire me" }

    LaunchedEffect(horizontalAnimation) {
        delay(100)
        horizontalAnimation.animateTo(
            targetValue = -1f, animationSpec = infiniteRepeatable(
                animation = tween((4000 * speed).toInt(), easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            )
        )
    }

    LaunchedEffect(verticalAnimation) {
        verticalAnimation.animateTo(
            targetValue = -1f, animationSpec = infiniteRepeatable(
                animation = tween((5000 * speed).toInt(), easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            )
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .then(modifier)
    ) {
        Text(
            text = repeatingText,
            modifier = Modifier
                .padding(
                    0.dp, 0.dp, 0.dp, startBottomMargin
                )
                .onGloballyPositioned {
                    horizontalTextWidth.intValue = it.size.height
                }
                .offset((horizontalTextWidth.intValue * horizontalAnimation.value).dp)
                .fillMaxWidth()
                .zIndex(1f)
                .align(Alignment.BottomCenter),
            fontSize = fontSize,
            overflow = TextOverflow.Visible,
            softWrap = false,
            color = LightBlue
        )
        Text(
            text = repeatingSecondText,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .onGloballyPositioned {
                    verticalTextHeight.intValue = it.size.height
                }
                .rotate(-90f)
                .layout { measurable, constraints ->
                    val placeable = measurable.measure(constraints)
                    layout(placeable.height, placeable.width) {
                        placeable.place(
                            -placeable.height,
                            (placeable.width - placeable.height + startBottomMargin.roundToPx()) / 2
                        )
                    }
                }
                .fillMaxWidth()
                .offset((verticalTextHeight.intValue * verticalAnimation.value).dp),
            fontSize = fontSize,
            overflow = TextOverflow.Visible,
            softWrap = false,
            color = Color(0xFF304255)
        )
    }
}
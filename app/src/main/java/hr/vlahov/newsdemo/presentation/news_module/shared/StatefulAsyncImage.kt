package hr.vlahov.newsdemo.presentation.news_module.shared

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import hr.vlahov.newsdemo.R

@Composable
fun StatefulAsyncImage(
    model: Any?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.FillWidth,
    error: @Composable () -> Unit = {
        Icon(
            painter = painterResource(id = R.drawable.ic_newspaper),
            contentDescription = "Newspaper icon",
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp)
        )
    },
    onState: (AsyncImagePainter.State) -> Unit = {},
) {
    Box(modifier = modifier) {
        val imageLoadingState =
            remember { mutableStateOf<AsyncImagePainter.State>(AsyncImagePainter.State.Empty) }

        if (LocalInspectionMode.current) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Preview")
            }
        }

        AsyncImage(
            model = model,
            contentDescription = contentDescription,
            contentScale = contentScale,
            onState = {
                imageLoadingState.value = it
                onState(it)
            },
            modifier = Modifier.fillMaxSize()
        )

        AnimatedVisibility(
            visible = imageLoadingState.value is AsyncImagePainter.State.Loading,
            modifier = Modifier.align(Alignment.Center)
        ) {
            CircularProgressIndicator()
        }

        AnimatedVisibility(
            visible = imageLoadingState.value is AsyncImagePainter.State.Error,
            modifier = Modifier.align(Alignment.Center)
        ) {
            error()
        }
    }
}
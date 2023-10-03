package hr.vlahov.newsdemo.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import coil.request.ImageRequest
import coil.size.Size

@Composable
fun buildAsyncImageModel(
    data: Any?,
    size: Size = Size.ORIGINAL,
    crossFade: Boolean = true,
): ImageRequest {
    return ImageRequest.Builder(LocalContext.current)
        .data(data)
        .size(size)
        .crossfade(crossFade)
        .build()
}
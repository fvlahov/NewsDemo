package hr.vlahov.newsdemo.presentation.news_module.shared

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.paging.compose.LazyPagingItems
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import hr.vlahov.domain.models.news.NewsArticle
import hr.vlahov.newsdemo.R
import hr.vlahov.newsdemo.utils.isLoading
import hr.vlahov.newsdemo.utils.isRefreshing
import hr.vlahov.newsdemo.utils.items
import hr.vlahov.newsdemo.utils.pull_to_refresh.PullRefreshIndicator
import hr.vlahov.newsdemo.utils.pull_to_refresh.pullRefresh
import hr.vlahov.newsdemo.utils.pull_to_refresh.rememberPullRefreshState

@Composable
fun NewsArticlesList(
    items: LazyPagingItems<NewsArticle>,
    onItemClick: (NewsArticle) -> Unit,
    modifier: Modifier = Modifier,
) {
    val pullRefreshState = rememberPullRefreshState(
        refreshing = items.isRefreshing(),
        onRefresh = { items.refresh() }
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
    ) {
        item {
            AnimatedVisibility(visible = items.isLoading()) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
        }
        item {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .zIndex(5f)
            ) {
                PullRefreshIndicator(
                    refreshing = items.isRefreshing(),
                    state = pullRefreshState,
                )
            }
        }
        items(items, key = { it.originalArticleUrl }) { newsArticle ->
            NewsArticleItem(
                newsArticle = newsArticle,
                onItemClick = { onItemClick(newsArticle) },
                modifier = Modifier.padding(16.dp, 8.dp)
            )
        }
    }
}

@Composable
private fun NewsArticleItem(
    newsArticle: NewsArticle,
    onItemClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Card(
            onClick = onItemClick,
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onBackground
            ),
            modifier = Modifier.padding(0.dp, 8.dp, 0.dp, 0.dp)
        ) {
            val imageLoadingState =
                remember { mutableStateOf<AsyncImagePainter.State>(AsyncImagePainter.State.Empty) }

            val model = ImageRequest.Builder(LocalContext.current)
                .data(newsArticle.imageUrl)
                .size(Size(800, 300))
                .crossfade(true)
                .build()

            Box(modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()) {
                AsyncImage(
                    model = model,
                    contentDescription = newsArticle.title,
                    contentScale = ContentScale.Crop,
                    onState = { imageLoadingState.value = it },
                    modifier = Modifier.fillMaxSize()
                )

                this@Card.AnimatedVisibility(
                    visible = imageLoadingState.value is AsyncImagePainter.State.Loading,
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    CircularProgressIndicator()
                }

                this@Card.AnimatedVisibility(
                    visible = imageLoadingState.value is AsyncImagePainter.State.Error,
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_newspaper),
                        contentDescription = "Newspaper icon",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Text(
                text = newsArticle.title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 16.dp, 16.dp, 0.dp)
            )
            Text(
                text = newsArticle.description.orEmpty(),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }
}
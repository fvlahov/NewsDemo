package hr.vlahov.newsdemo.presentation.news_module.shared

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.size.Size
import hr.vlahov.domain.models.news.NewsArticle
import hr.vlahov.newsdemo.R
import hr.vlahov.newsdemo.ui.theme.NewsDemoTheme
import hr.vlahov.newsdemo.utils.buildAsyncImageModel
import hr.vlahov.newsdemo.utils.dummyNewsArticles
import hr.vlahov.newsdemo.utils.exceptionOrNull
import hr.vlahov.newsdemo.utils.getFormattedDate
import hr.vlahov.newsdemo.utils.isLoading
import hr.vlahov.newsdemo.utils.isRefreshing
import hr.vlahov.newsdemo.utils.items
import hr.vlahov.newsdemo.utils.pull_to_refresh.PullRefreshIndicator
import hr.vlahov.newsdemo.utils.pull_to_refresh.pullRefresh
import hr.vlahov.newsdemo.utils.pull_to_refresh.rememberPullRefreshState
import kotlinx.coroutines.flow.flowOf

@Composable
fun NewsArticlesList(
    items: LazyPagingItems<NewsArticle>,
    onItemClick: (NewsArticle) -> Unit,
    onNewsArticleLikedChanged: (NewsArticle, isLiked: Boolean) -> Unit,
    combinedNewsFilters: NewsFilters.CombinedNewsFilters?,
    modifier: Modifier = Modifier,
) {
    val didInitialize = remember { mutableStateOf(false) }
    val orderBy = remember {
        derivedStateOf {
            combinedNewsFilters?.orderBy ?: NewsFilters.OrderBy.DESCENDING
        }
    }.value

    if (combinedNewsFilters != null)
        LaunchedEffect(combinedNewsFilters) {
            if (didInitialize.value)
                items.refresh()
            else
                didInitialize.value = true
        }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = items.isRefreshing(),
        onRefresh = { items.refresh() }
    )

    val lazyListState = rememberLazyListState()

    LaunchedEffect(orderBy) {
        val scrollToItemIndex = if (orderBy == NewsFilters.OrderBy.DESCENDING)
            0
        else
            (items.itemCount.takeIf { it > 0 } ?: 1) - 1
        lazyListState.scrollToItem(scrollToItemIndex)
    }

    Box {
        PullRefreshIndicator(
            refreshing = items.isRefreshing(),
            state = pullRefreshState,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .zIndex(5f)
        )

        HandleLoadStateError(loadStates = items.loadState.source)

        LazyColumn(
            state = lazyListState,
            modifier = modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState),
            reverseLayout = orderBy == NewsFilters.OrderBy.ASCENDING
        ) {
            item {
                AnimatedVisibility(visible = items.isLoading()) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }
            }
            items(items, key = { it.originalArticleUrl }) { newsArticle ->
                NewsArticleItem(
                    newsArticle = newsArticle,
                    onItemClick = { onItemClick(newsArticle) },
                    onNewsArticleLikeChanged = { isLiked ->
                        onNewsArticleLikedChanged(
                            newsArticle,
                            isLiked
                        )
                    },
                    modifier = Modifier.padding(16.dp, 8.dp)
                )
            }
        }
    }
}

@Composable
private fun BoxScope.HandleLoadStateError(
    loadStates: LoadStates,
) {
    val exception = loadStates.exceptionOrNull()
    if (exception is TooManyRequestsException)
        Row(
            modifier = Modifier
                .padding(32.dp)
                .align(Alignment.TopCenter),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(id = R.string.too_many_requests),
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
}

@Composable
private fun NewsArticleItem(
    newsArticle: NewsArticle,
    onItemClick: () -> Unit,
    onNewsArticleLikeChanged: (isLiked: Boolean) -> Unit,
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

            Box(
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
            ) {
                AsyncImage(
                    model = buildAsyncImageModel(
                        data = newsArticle.imageUrl,
                        size = Size(800, 300)
                    ),
                    contentDescription = newsArticle.title,
                    contentScale = ContentScale.Crop,
                    onState = { imageLoadingState.value = it },
                    modifier = Modifier.fillMaxSize()
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color.Black.copy(),
                                    Color.Transparent
                                )
                            ), alpha = 0.5f
                        )
                ) {
                    Text(
                        text = newsArticle.getFormattedDate(LocalContext.current),
                        modifier = Modifier.padding(8.dp, 8.dp, 8.dp, 32.dp)
                    )
                }

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
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp)
                    )
                }

                val isLiked = rememberSaveable { mutableStateOf(newsArticle.isLiked) }

                IconButton(
                    onClick = {
                        isLiked.value = isLiked.value.not()
                        onNewsArticleLikeChanged(isLiked.value)
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                        .size(48.dp)
                        .zIndex(10f),
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.8f)
                    )
                ) {
                    val iconModifier = Modifier.size(32.dp)

                    this@Card.AnimatedVisibility(
                        visible = isLiked.value
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Liked article",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = iconModifier
                        )
                    }

                    this@Card.AnimatedVisibility(
                        visible = isLiked.value.not()
                    ) {
                        Icon(
                            imageVector = Icons.Default.FavoriteBorder,
                            contentDescription = "Liked article",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = iconModifier
                        )
                    }
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

@Composable
@Preview
private fun NewsArticleListPreview() {
    NewsDemoTheme {
        val items = flowOf(PagingData.from(dummyNewsArticles)).collectAsLazyPagingItems()

        NewsArticlesList(
            items = items,
            onItemClick = {},
            combinedNewsFilters = NewsFilters.CombinedNewsFilters.default(),
            onNewsArticleLikedChanged = { _, _ -> }
        )

    }
}
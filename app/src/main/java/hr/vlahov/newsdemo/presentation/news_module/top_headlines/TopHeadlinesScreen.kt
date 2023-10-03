package hr.vlahov.newsdemo.presentation.news_module.top_headlines

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import hr.vlahov.newsdemo.presentation.news_module.shared.NewsArticlesList

@Composable
fun TopHeadlinesScreen(
    viewModel: TopHeadlinesViewModel = hiltViewModel(),
) {
    val combinedNewsFilters = viewModel.combinedNewsFilters.collectAsStateWithLifecycle(null).value
    val topHeadlines = viewModel.topHeadlines.collectAsLazyPagingItems()

    LaunchedEffect(combinedNewsFilters) {
        topHeadlines.refresh()
    }

    NewsArticlesList(
        items = topHeadlines,
        onItemClick = viewModel::navigateToSingleNewsArticle,
        onNewsArticleLikedChanged = viewModel::toggleLikeNewsArticle
    )
}
package hr.vlahov.newsdemo.presentation.news_module.top_headlines

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import hr.vlahov.newsdemo.presentation.news_module.shared.NewsArticlesList

@Composable
fun TopHeadlinesScreen(
    viewModel: TopHeadlinesViewModel = hiltViewModel(),
) {
    val topHeadlines = viewModel.topHeadlines.collectAsLazyPagingItems()

    NewsArticlesList(
        items = topHeadlines,
        onItemClick = viewModel::navigateToSingleNewsArticle,
        onNewsArticleLikedChanged = viewModel::toggleLikeNewsArticle
    )
}
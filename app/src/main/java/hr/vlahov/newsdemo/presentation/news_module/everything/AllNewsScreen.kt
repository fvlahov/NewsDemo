package hr.vlahov.newsdemo.presentation.news_module.everything

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import hr.vlahov.newsdemo.presentation.news_module.shared.NewsArticlesList

@Composable
fun AllNewsScreen(
    viewModel: AllNewsViewModel = hiltViewModel(),
) {
    val combinedNewsFilters = viewModel.combinedNewsFilters.collectAsStateWithLifecycle(null).value
    val items = viewModel.allNewsArticles.collectAsLazyPagingItems()

    LaunchedEffect(combinedNewsFilters) {
        items.refresh()
    }

    NewsArticlesList(
        items = items,
        onItemClick = viewModel::navigateToSingleNewsArticle,
        onNewsArticleLikedChanged = viewModel::toggleLikeNewsArticle
    )
}
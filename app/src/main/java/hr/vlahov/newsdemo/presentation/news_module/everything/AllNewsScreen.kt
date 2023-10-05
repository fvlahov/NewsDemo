package hr.vlahov.newsdemo.presentation.news_module.everything

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import hr.vlahov.newsdemo.presentation.news_module.shared.NewsArticlesList
import hr.vlahov.newsdemo.utils.ErrorHandler

@Composable
fun AllNewsScreen(
    viewModel: AllNewsViewModel = hiltViewModel(),
) {
    val combinedNewsFilters = viewModel.combinedNewsFilters.collectAsStateWithLifecycle(null).value
    val orderBy = viewModel.orderByFilter.collectAsStateWithLifecycle().value
    val items = viewModel.allNewsArticles.collectAsLazyPagingItems()
    val error = viewModel.errors.collectAsStateWithLifecycle().value

    LaunchedEffect(combinedNewsFilters) {
        items.refresh()
    }

    ErrorHandler(error = error)

    NewsArticlesList(
        items = items,
        onItemClick = viewModel::navigateToSingleNewsArticle,
        onNewsArticleLikedChanged = viewModel::toggleLikeNewsArticle,
        orderBy = orderBy
    )
}
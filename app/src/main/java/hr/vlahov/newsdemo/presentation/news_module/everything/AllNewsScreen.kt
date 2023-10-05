package hr.vlahov.newsdemo.presentation.news_module.everything

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import hr.vlahov.newsdemo.presentation.news_module.shared.NewsArticlesList
import hr.vlahov.newsdemo.utils.ErrorHandler
import hr.vlahov.newsdemo.utils.isInErrorOrLoading

@Composable
fun AllNewsScreen(
    viewModel: AllNewsViewModel = hiltViewModel(),
) {
    val combinedNewsFilters = viewModel.combinedNewsFilters.collectAsStateWithLifecycle(null).value
    val items = viewModel.allNewsArticles.collectAsLazyPagingItems()
    val error = viewModel.errors.collectAsStateWithLifecycle().value

    if (items.isInErrorOrLoading().not())
        ErrorHandler(error = error)

    NewsArticlesList(
        items = items,
        onItemClick = viewModel::navigateToSingleNewsArticle,
        onNewsArticleLikedChanged = viewModel::toggleLikeNewsArticle,
        combinedNewsFilters = combinedNewsFilters
    )
}
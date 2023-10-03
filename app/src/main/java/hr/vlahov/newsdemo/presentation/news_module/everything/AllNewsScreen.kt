package hr.vlahov.newsdemo.presentation.news_module.everything

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import hr.vlahov.newsdemo.presentation.news_module.shared.NewsArticlesList

@Composable
fun AllNewsScreen(
    viewModel: AllNewsViewModel = hiltViewModel(),
) {
    val items = viewModel.allNewsArticles.collectAsLazyPagingItems()

    NewsArticlesList(
        items = items,
        onItemClick = viewModel::navigateToSingleNewsArticle,
        onNewsArticleLikedChanged = viewModel::toggleLikeNewsArticle
    )
}
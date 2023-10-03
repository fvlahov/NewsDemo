package hr.vlahov.newsdemo.presentation.news_module.liked_news_articles

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import hr.vlahov.newsdemo.presentation.news_module.shared.NewsArticlesList

@Composable
fun LikedNewsArticlesScreen(
    viewModel: LikedNewsArticlesViewModel = hiltViewModel(),
) {
    val items = viewModel.likedNewsArticles.collectAsLazyPagingItems()

    NewsArticlesList(
        items = items,
        onItemClick = viewModel::navigateToSingleNewsArticle,
        onNewsArticleLikedChanged = viewModel::toggleLikeNewsArticle
    )
}
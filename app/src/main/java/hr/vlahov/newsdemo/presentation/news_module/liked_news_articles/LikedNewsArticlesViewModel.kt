package hr.vlahov.newsdemo.presentation.news_module.liked_news_articles

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import hr.vlahov.domain.models.news.NewsArticle
import hr.vlahov.domain.usecases.NewsUseCase
import hr.vlahov.newsdemo.base.BaseViewModel
import hr.vlahov.newsdemo.navigation.NavTarget
import hr.vlahov.newsdemo.navigation.navigator.Navigator
import javax.inject.Inject

@HiltViewModel
class LikedNewsArticlesViewModel @Inject constructor(
    private val navigator: Navigator,
    private val newsUseCase: NewsUseCase,
) : BaseViewModel() {

    val likedNewsArticles =
        newsUseCase.getLikedNewsArticlesPagingData(pagingConfig = PagingConfig(pageSize = 20))
            .cachedIn(viewModelScope)

    fun navigateToSingleNewsArticle(newsArticle: NewsArticle) {
        navigator.navigateNewsTo(
            NavTarget.NewsModule.SingleNewsArticle.WithNewsArticleUrl(
                newsArticle.originalArticleUrl
            )
        )
    }

    fun toggleLikeNewsArticle(newsArticle: NewsArticle, isLiked: Boolean) {
        launchIn {
            newsUseCase.toggleLikeNewsArticle(newsArticle, isLiked)
        }
    }
}
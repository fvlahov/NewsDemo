package hr.vlahov.newsdemo.presentation.news_module.liked_news_articles

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import hr.vlahov.data.database.dao.LikedNewsArticleDao
import hr.vlahov.data.models.converters.toNewsArticle
import hr.vlahov.domain.models.news.NewsArticle
import hr.vlahov.domain.usecases.NewsUseCase
import hr.vlahov.domain.usecases.ProfileUseCase
import hr.vlahov.newsdemo.base.BaseViewModel
import hr.vlahov.newsdemo.navigation.navigator.Navigator
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class LikedNewsArticlesViewModel @Inject constructor(
    private val navigator: Navigator,
    private val newsUseCase: NewsUseCase,
    private val profileUseCase: ProfileUseCase,
    private val likedNewsArticleDao: LikedNewsArticleDao,
) : BaseViewModel() {

    val likedNewsArticles = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = {
            runBlocking {
                likedNewsArticleDao
                    .getLikedNewsArticlesForProfilePagingSource(profileName = profileUseCase.fetchCurrentProfile()!!.name)
            }
        }
    )
        .flow
        .map { pagingData -> pagingData.map { it.toNewsArticle() } }
        .cachedIn(viewModelScope)

    fun navigateToSingleNewsArticle(newsArticle: NewsArticle) {

    }

    fun toggleLikeNewsArticle(newsArticle: NewsArticle, isLiked: Boolean) {
        launchIn {
            newsUseCase.toggleLikeNewsArticle(newsArticle, isLiked)
        }
    }
}
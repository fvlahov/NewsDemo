package hr.vlahov.newsdemo.presentation.news_module.top_headlines

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import hr.vlahov.data.database.dao.LikedNewsArticleDao
import hr.vlahov.domain.models.news.NewsArticle
import hr.vlahov.domain.models.news.NewsArticlePage
import hr.vlahov.domain.usecases.NewsUseCase
import hr.vlahov.domain.usecases.ProfileUseCase
import hr.vlahov.newsdemo.base.BaseViewModel
import hr.vlahov.newsdemo.navigation.NavTarget
import hr.vlahov.newsdemo.navigation.navigator.Navigator
import hr.vlahov.newsdemo.presentation.news_module.shared.NewsArticlePagingSource
import hr.vlahov.newsdemo.presentation.news_module.shared.NewsFilters
import javax.inject.Inject

@HiltViewModel
class TopHeadlinesViewModel @Inject constructor(
    private val navigator: Navigator,
    private val newsUseCase: NewsUseCase,
    private val newsFilters: NewsFilters,
    private val likedNewsArticleDao: LikedNewsArticleDao,
    private val profileUseCase: ProfileUseCase,
) : BaseViewModel() {

    val combinedNewsFilters = newsFilters.combinedFilters

    val topHeadlines = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = {
            NewsArticlePagingSource { currentPage ->
                newsUseCase.fetchTopHeadlines(
                    keyword = newsFilters.searchQuery.value,
                    sources = newsFilters.selectedNewsSources.value,
                    page = currentPage
                ).filterArticlesByDate()
            }
        }
    ).flow.cachedIn(viewModelScope)

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

    private fun NewsArticlePage.filterArticlesByDate() =
        this.copy(items = this.items.filter {
            it.publishedAt > (newsFilters.dateFrom.value ?: 0) &&
                    it.publishedAt < (newsFilters.dateTo.value ?: Long.MAX_VALUE)
        })

}
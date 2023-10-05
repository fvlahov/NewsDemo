package hr.vlahov.newsdemo.presentation.news_module.everything

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import hr.vlahov.domain.models.news.NewsArticle
import hr.vlahov.domain.usecases.NewsUseCase
import hr.vlahov.newsdemo.R
import hr.vlahov.newsdemo.base.BaseViewModel
import hr.vlahov.newsdemo.base.errors.BaseError
import hr.vlahov.newsdemo.navigation.NavTarget
import hr.vlahov.newsdemo.navigation.navigator.Navigator
import hr.vlahov.newsdemo.presentation.news_module.shared.NewsArticlePagingSource
import hr.vlahov.newsdemo.presentation.news_module.shared.NewsFilters
import hr.vlahov.newsdemo.utils.randomId
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class AllNewsViewModel @Inject constructor(
    private val navigator: Navigator,
    private val newsUseCase: NewsUseCase,
    private val newsFilters: NewsFilters,
) : BaseViewModel() {

    val combinedNewsFilters = newsFilters.combinedFilters
    val orderByFilter = newsFilters.orderBy

    val allNewsArticles = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = {
            NewsArticlePagingSource { currentPage ->
                newsUseCase.fetchEverything(
                    keyword = newsFilters.searchQuery.value,
                    dateFrom = newsFilters.dateFrom.value,
                    dateTo = newsFilters.dateTo.value,
                    sources = newsFilters.selectedNewsSources.value,
                    page = currentPage
                )
            }
        }
    ).flow.cachedIn(viewModelScope)

    init {
        newsFilters.combinedFilters.onEach {
            if (it.searchQuery.isNullOrEmpty() && it.newsSources.isEmpty())
                localErrors.emit(InvalidFilters())
        }.launchIn(viewModelScope)
    }

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

    data class InvalidFilters(
        override val id: String = randomId,
        override val displayType: BaseError.ErrorDisplayType = BaseError.ErrorDisplayType.SNACKBAR,
        override val messageRes: Int = R.string.invalid_filters,
    ) : BaseError
}
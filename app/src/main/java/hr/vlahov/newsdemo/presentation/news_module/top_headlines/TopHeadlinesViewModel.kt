package hr.vlahov.newsdemo.presentation.news_module.top_headlines

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import hr.vlahov.domain.models.news.NewsArticle
import hr.vlahov.domain.models.news.NewsCategory
import hr.vlahov.domain.usecases.NewsUseCase
import hr.vlahov.newsdemo.base.BaseViewModel
import hr.vlahov.newsdemo.navigation.navigator.Navigator
import hr.vlahov.newsdemo.presentation.news_module.shared.NewsArticlePagingSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class TopHeadlinesViewModel @Inject constructor(
    private val navigator: Navigator,
    private val newsUseCase: NewsUseCase,
) : BaseViewModel() {

    private var keyword: String? = null
    private var selectedCategory: NewsCategory? = null

    private val _headlinesCategories = MutableStateFlow(NewsCategory.values().toList())
    val headlinesCategories = _headlinesCategories.asStateFlow()

    val topHeadlines = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = {
            NewsArticlePagingSource { currentPage ->
                newsUseCase.fetchTopHeadlines(
                    keyword = keyword,
                    category = selectedCategory,
                    page = currentPage
                )
            }
        }
    ).flow.cachedIn(viewModelScope)

    fun setKeyword(keyword: String?) {
        this.keyword = keyword
    }

    fun setSelectedCategory(category: NewsCategory) {
        this.selectedCategory = category
    }

    fun navigateToSingleNewsArticle(newsArticle: NewsArticle) {

    }

    fun toggleLikeNewsArticle(newsArticle: NewsArticle, isLiked: Boolean) {
        launchIn {
            newsUseCase.toggleLikeNewsArticle(newsArticle, isLiked)
        }
    }

    private suspend fun <T> MutableStateFlow<List<T>>.merge(items: List<T>) {
        this.emit(this.value + items)
    }
}
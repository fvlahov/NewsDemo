package hr.vlahov.newsdemo.presentation.news_module.top_headlines

import dagger.hilt.android.lifecycle.HiltViewModel
import hr.vlahov.domain.models.news.NewsArticle
import hr.vlahov.domain.usecases.NewsUseCase
import hr.vlahov.newsdemo.base.BaseViewModel
import hr.vlahov.newsdemo.navigation.navigator.Navigator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class TopHeadlinesViewModel @Inject constructor(
    private val navigator: Navigator,
    private val newsUseCase: NewsUseCase,
) : BaseViewModel() {

    private val _uiState = MutableStateFlow<TopHeadlinesUIState>(TopHeadlinesUIState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _topHeadlines = MutableStateFlow<List<NewsArticle>>(emptyList())
    val topHeadlines = _topHeadlines.asStateFlow()

    init {
        launchIn {
            newsUseCase.fetchTopHeadlines(keyword = null).also {
                _topHeadlines.merge(it.items)
            }
            _uiState.emit(TopHeadlinesUIState.Idle)
        }
    }

    fun navigateToSingleNewsArticle(newsArticle: NewsArticle) {

    }

    sealed class TopHeadlinesUIState {
        object Loading : TopHeadlinesUIState()
        object Idle : TopHeadlinesUIState()
    }

    private suspend fun <T> MutableStateFlow<List<T>>.merge(items: List<T>) {
        this.emit(this.value + items)
    }
}
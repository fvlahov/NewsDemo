package hr.vlahov.newsdemo.presentation.news_module.single_news_article

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import hr.vlahov.domain.models.news.NewsArticle
import hr.vlahov.domain.usecases.NewsUseCase
import hr.vlahov.newsdemo.base.BaseViewModel
import hr.vlahov.newsdemo.navigation.NavTarget
import hr.vlahov.newsdemo.navigation.navigator.Navigator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SingleNewsArticleViewModel @Inject constructor(
    private val navigator: Navigator,
    private val newsUseCase: NewsUseCase,
    private val savedStateHandle: SavedStateHandle,
) : BaseViewModel() {

    private val _newsArticle = MutableStateFlow<NewsArticle?>(null)
    val newsArticle = _newsArticle.asStateFlow()

    init {
        launchWithProgressIn {
            val originalArticleUrl =
                savedStateHandle.get<String>(NavTarget.NewsModule.SingleNewsArticle.Info.OriginalNewsArticleParam)
            originalArticleUrl?.let { articleUrl ->
                newsUseCase.fetchNewsArticleByUrl(articleUrl).also { _newsArticle.emit(it) }
            }
        }
    }

    fun navigateBack() {
        navigator.popNewsBackStack()
    }

    fun toggleNewsArticleLiked(isLiked: Boolean) {
        launchIn {
            newsArticle.value?.let { newsArticle ->
                newsUseCase.toggleLikeNewsArticle(newsArticle, isLiked)
            }
        }
    }

    fun navigateToFullArticle() {

    }
}
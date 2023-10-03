package hr.vlahov.newsdemo.presentation.news_module

import dagger.hilt.android.lifecycle.HiltViewModel
import hr.vlahov.domain.models.news.NewsSource
import hr.vlahov.domain.usecases.NewsUseCase
import hr.vlahov.newsdemo.base.BaseViewModel
import hr.vlahov.newsdemo.navigation.navigator.Navigator
import hr.vlahov.newsdemo.presentation.news_module.shared.NewsFilters
import javax.inject.Inject

@HiltViewModel
class NewsMainViewModel @Inject constructor(
    val navigator: Navigator,
    private val newsFilters: NewsFilters,
    newsUseCase: NewsUseCase,
) : BaseViewModel() {

    val newsSources = newsUseCase.newsSources
    val selectedNewsSources = newsFilters.selectedNewsSources

    fun setSearchQuery(searchQuery: String?) {
        launchIn { newsFilters.setSearchQuery(searchQuery) }
    }

    fun setDateRange(dateFrom: Long?, dateTo: Long?) {
        launchIn { newsFilters.setDateRange(dateFrom, dateTo) }
    }

    fun setNewsOrder(orderBy: NewsFilters.OrderBy) {
        launchIn { newsFilters.setNewsOrder(orderBy) }
    }

    fun selectNewsSources(newsSources: List<NewsSource>) {
        launchIn { newsFilters.selectNewsSources(newsSources) }
    }
}
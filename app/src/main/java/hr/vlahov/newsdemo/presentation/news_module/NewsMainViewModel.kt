package hr.vlahov.newsdemo.presentation.news_module

import dagger.hilt.android.lifecycle.HiltViewModel
import hr.vlahov.newsdemo.base.BaseViewModel
import hr.vlahov.newsdemo.navigation.navigator.Navigator
import hr.vlahov.newsdemo.presentation.news_module.shared.NewsFilters
import javax.inject.Inject

@HiltViewModel
class NewsMainViewModel @Inject constructor(
    val navigator: Navigator,
    private val newsFilters: NewsFilters,
) : BaseViewModel() {

    fun setSearchQuery(searchQuery: String?) {
        launchIn { newsFilters.setSearchQuery(searchQuery) }
    }

    fun setDateRange(dateFrom: Long?, dateTo: Long?) {
        launchIn { newsFilters.setDateRange(dateFrom, dateTo) }
    }

    fun setNewsOrder(orderBy: NewsFilters.OrderBy) {
        launchIn { newsFilters.setNewsOrder(orderBy) }
    }
}
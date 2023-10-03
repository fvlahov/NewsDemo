package hr.vlahov.newsdemo.presentation.news_module.shared

import hr.vlahov.domain.models.news.NewsSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

interface NewsFilters {
    val searchQuery: StateFlow<String?>
    val dateFrom: StateFlow<Long?>
    val dateTo: StateFlow<Long?>
    val orderBy: StateFlow<OrderBy>
    val selectedNewsSources: StateFlow<List<NewsSource>>

    /**
     * Orders the news ASCENDING or DESCENDING by publish date
     * ASCENDING - Oldest news article will be first
     * DESCENDING - Newest news article will be first
     */
    enum class OrderBy {
        ASCENDING,
        DESCENDING;

        fun toggle() = when (this) {
            ASCENDING -> DESCENDING
            DESCENDING -> ASCENDING
        }
    }

    suspend fun setSearchQuery(searchQuery: String?)
    suspend fun setDateRange(dateFrom: Long?, dateTo: Long?)
    suspend fun setNewsOrder(orderBy: OrderBy)
    suspend fun selectNewsSources(newsSources: List<NewsSource>)
}

class NewsFiltersImpl @Inject constructor() : NewsFilters {
    override val searchQuery = MutableStateFlow<String?>(null)

    override val dateFrom = MutableStateFlow<Long?>(null)

    override val dateTo = MutableStateFlow<Long?>(null)

    override val orderBy = MutableStateFlow(NewsFilters.OrderBy.DESCENDING)

    override val selectedNewsSources = MutableStateFlow<List<NewsSource>>(emptyList())

    override suspend fun setSearchQuery(searchQuery: String?) {
        this.searchQuery.emit(searchQuery)
    }

    override suspend fun setDateRange(dateFrom: Long?, dateTo: Long?) {
        this.dateFrom.emit(dateFrom)
        this.dateTo.emit(dateTo)
    }

    override suspend fun setNewsOrder(orderBy: NewsFilters.OrderBy) {
        this.orderBy.emit(orderBy)
    }

    override suspend fun selectNewsSources(newsSources: List<NewsSource>) {
        this.selectedNewsSources.emit(newsSources)
    }
}
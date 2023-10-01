package hr.vlahov.newsdemo.presentation.news_module.shared

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

interface NewsFilters {
    val searchQuery: StateFlow<String?>
    val dateFrom: StateFlow<Long?>
    val dateTo: StateFlow<Long?>
    val orderBy: StateFlow<OrderBy>

    /**
     * Orders the news ASCENDING or DESCENDING by publish date
     * ASCENDING - Oldest news article will be first
     * DESCENDING - Newest news article will be first
     */
    enum class OrderBy {
        ASCENDING,
        DESCENDING
    }

    suspend fun setSearchQuery(searchQuery: String?)
    suspend fun setDateRange(dateFrom: Long?, dateTo: Long?)
    suspend fun setNewsOrder(orderBy: OrderBy)
}

class NewsFiltersImpl @Inject constructor() : NewsFilters {
    override val searchQuery = MutableStateFlow<String?>(null)

    override val dateFrom = MutableStateFlow<Long?>(null)

    override val dateTo = MutableStateFlow<Long?>(null)

    override val orderBy = MutableStateFlow(NewsFilters.OrderBy.DESCENDING)

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
}
package hr.vlahov.newsdemo.presentation.news_module.shared

import hr.vlahov.domain.models.news.NewsSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton

interface NewsFilters {
    val searchQuery: StateFlow<String?>
    val dateFrom: StateFlow<Long?>
    val dateTo: StateFlow<Long?>
    val orderBy: StateFlow<OrderBy>
    val selectedNewsSources: StateFlow<List<NewsSource>>

    val combinedFilters: StateFlow<CombinedNewsFilters>

    /**
     * Orders the news ASCENDING or DESCENDING by publish date;
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

    data class CombinedNewsFilters(
        val searchQuery: String?,
        val dateFrom: Long?,
        val dateTo: Long?,
        val orderBy: OrderBy,
        val newsSources: List<NewsSource>,
    ) {
        companion object {
            fun default() = CombinedNewsFilters(
                searchQuery = null,
                dateFrom = null,
                dateTo = null,
                orderBy = OrderBy.DESCENDING,
                newsSources = emptyList()
            )
        }
    }
}

@Singleton
class NewsFiltersImpl @Inject constructor() : NewsFilters {
    override val searchQuery = MutableStateFlow<String?>(null)

    override val dateFrom = MutableStateFlow<Long?>(null)

    override val dateTo = MutableStateFlow<Long?>(null)

    override val orderBy = MutableStateFlow(NewsFilters.OrderBy.DESCENDING)

    override val selectedNewsSources = MutableStateFlow<List<NewsSource>>(emptyList())

    override val combinedFilters: StateFlow<NewsFilters.CombinedNewsFilters> = combine(
        searchQuery,
        dateFrom,
        dateTo,
        orderBy,
        selectedNewsSources,
        initialValue = NewsFilters.CombinedNewsFilters.default()
    ) { searchQuery, dateFrom, dateTo, orderBy, selectedNewsSources ->
        NewsFilters.CombinedNewsFilters(
            searchQuery = searchQuery,
            dateFrom = dateFrom,
            dateTo = dateTo,
            orderBy = orderBy,
            newsSources = selectedNewsSources
        )
    }

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

    private fun <T1, T2, T3, T4, T5, R> combine(
        flow: Flow<T1>,
        flow2: Flow<T2>,
        flow3: Flow<T3>,
        flow4: Flow<T4>,
        flow5: Flow<T5>,
        initialValue: R,
        transform: suspend (a: T1, b: T2, c: T3, d: T4, e: T5) -> R,
    ): StateFlow<R> {
        return object : StateFlow<R> {
            private val mutex = Mutex()
            override var value: R = initialValue

            override val replayCache: List<R> get() = listOf(value)

            override suspend fun collect(collector: FlowCollector<R>): Nothing {
                combine(flow, flow2, flow3, flow4, flow5, transform)
                    .onStart { emit(initialValue) }
                    .collect {
                        mutex.withLock {
                            value = it
                            collector.emit(it)
                        }
                    }
                error("This exception is needed to 'return' Nothing. It won't be thrown (collection of StateFlow will never end)")
            }
        }
    }
}
package hr.vlahov.domain.usecases

import hr.vlahov.domain.models.news.NewsArticle
import hr.vlahov.domain.models.news.NewsArticlePage
import hr.vlahov.domain.models.news.NewsSource
import kotlinx.coroutines.flow.Flow

interface NewsUseCase {
    val newsSources: Flow<List<NewsSource>>

    suspend fun fetchTopHeadlines(
        keyword: String?,
        sources: List<NewsSource>,
        page: Int,
        pageSize: Int = 20,
        country: String = "us",
    ): NewsArticlePage

    suspend fun fetchEverything(
        keyword: String?,
        sources: List<NewsSource>,
        page: Int,
        dateFrom: Long?,
        dateTo: Long?,
        pageSize: Int = 20,
    ): NewsArticlePage

    suspend fun toggleLikeNewsArticle(newsArticle: NewsArticle, isLiked: Boolean)

    suspend fun preFetchNewsSources(language: String = "en")

    suspend fun fetchNewsArticleByUrl(originalArticleUrl: String): NewsArticle?
}
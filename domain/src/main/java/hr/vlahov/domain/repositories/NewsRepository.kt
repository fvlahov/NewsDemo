package hr.vlahov.domain.repositories

import hr.vlahov.domain.models.news.NewsArticle
import hr.vlahov.domain.models.news.NewsArticlePage
import hr.vlahov.domain.models.news.NewsSource
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    val newsSources: Flow<List<NewsSource>>

    suspend fun fetchTopHeadlines(
        keyword: String?,
        sources: List<NewsSource>,
        page: Int,
        pageSize: Int,
        country: String,
    ): NewsArticlePage

    suspend fun fetchEverything(
        keyword: String?,
        dateFrom: Long?,
        dateTo: Long?,
        sources: List<NewsSource>,
        page: Int,
        pageSize: Int,
    ): NewsArticlePage

    suspend fun saveNewsArticlesToDatabase(newsArticles: List<NewsArticle>)

    suspend fun fetchNewsArticle(originalArticleUrl: String): NewsArticle?

    suspend fun toggleLikeNewsArticle(newsArticle: NewsArticle, isLiked: Boolean)

    suspend fun fetchNewsSourcesByLanguage(language: String): List<NewsSource>

    suspend fun saveNewsSourcesToDatabase(newsSources: List<NewsSource>)
}
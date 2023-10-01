package hr.vlahov.domain.repositories

import hr.vlahov.domain.models.news.NewsArticlePage
import hr.vlahov.domain.models.news.NewsCategory

interface NewsRepository {
    suspend fun fetchTopHeadlines(
        keyword: String?,
        category: NewsCategory?,
        page: Int,
        pageSize: Int,
        country: String,
    ): NewsArticlePage

    suspend fun fetchEverything(): NewsArticlePage
}
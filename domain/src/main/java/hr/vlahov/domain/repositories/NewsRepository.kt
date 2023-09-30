package hr.vlahov.domain.repositories

import hr.vlahov.domain.models.news.NewsArticlePage

interface NewsRepository {
    suspend fun fetchTopHeadlines(keyword: String?, country: String): NewsArticlePage

    suspend fun fetchEverything(): NewsArticlePage
}
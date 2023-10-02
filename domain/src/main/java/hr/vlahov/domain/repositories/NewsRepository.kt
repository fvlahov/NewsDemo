package hr.vlahov.domain.repositories

import hr.vlahov.domain.models.news.NewsArticle
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

    suspend fun toggleLikeNewsArticle(newsArticle: NewsArticle, isLiked: Boolean)
}
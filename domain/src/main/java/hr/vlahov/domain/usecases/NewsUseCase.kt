package hr.vlahov.domain.usecases

import hr.vlahov.domain.models.news.NewsArticlePage
import hr.vlahov.domain.models.news.NewsCategory

interface NewsUseCase {
    suspend fun fetchTopHeadlines(
        keyword: String?,
        category: NewsCategory?,
        page: Int,
        pageSize: Int = 20,
        country: String = "us",
    ): NewsArticlePage

    suspend fun fetchEverything(): NewsArticlePage
}
package hr.vlahov.domain.usecases

import hr.vlahov.domain.models.news.NewsArticlePage

interface NewsUseCase {
    suspend fun fetchTopHeadlines(
        keyword: String?,
        country: String = "us",
    ): NewsArticlePage

    suspend fun fetchEverything(): NewsArticlePage
}
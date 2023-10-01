package hr.vlahov.data.usecases

import hr.vlahov.domain.models.news.NewsArticlePage
import hr.vlahov.domain.models.news.NewsCategory
import hr.vlahov.domain.repositories.NewsRepository
import hr.vlahov.domain.usecases.NewsUseCase
import javax.inject.Inject

class NewsUseCaseImpl @Inject constructor(
    private val newsRepository: NewsRepository,
) : NewsUseCase {

    override suspend fun fetchTopHeadlines(
        keyword: String?,
        category: NewsCategory?,
        page: Int,
        pageSize: Int,
        country: String,
    ): NewsArticlePage =
        newsRepository.fetchTopHeadlines(
            keyword = keyword,
            country = country,
            category = category,
            page = page,
            pageSize = pageSize
        )

    override suspend fun fetchEverything(): NewsArticlePage =
        newsRepository.fetchEverything()
}
package hr.vlahov.data.usecases

import hr.vlahov.domain.models.news.NewsArticlePage
import hr.vlahov.domain.repositories.NewsRepository
import hr.vlahov.domain.usecases.NewsUseCase
import javax.inject.Inject

class NewsUseCaseImpl @Inject constructor(
    private val newsRepository: NewsRepository,
) : NewsUseCase {
    override suspend fun fetchTopHeadlines(): NewsArticlePage =
        newsRepository.fetchTopHeadlines()

    override suspend fun fetchEverything(): NewsArticlePage =
        newsRepository.fetchEverything()
}
package hr.vlahov.data.repositories

import hr.vlahov.data.networking.apis.NewsApi
import hr.vlahov.domain.models.news.NewsArticlePage
import hr.vlahov.domain.repositories.NewsRepository
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val newsApi: NewsApi,
) : NewsRepository {

    override suspend fun fetchTopHeadlines(): NewsArticlePage {
        TODO("Not yet implemented")
    }

    override suspend fun fetchEverything(): NewsArticlePage {
        TODO("Not yet implemented")
    }
}
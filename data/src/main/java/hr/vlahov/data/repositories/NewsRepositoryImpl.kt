package hr.vlahov.data.repositories

import hr.vlahov.data.models.converters.toNewsArticlePage
import hr.vlahov.data.networking.apis.NewsApi
import hr.vlahov.domain.models.news.NewsArticlePage
import hr.vlahov.domain.repositories.NewsRepository
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val newsApi: NewsApi,
) : NewsRepository {

    override suspend fun fetchTopHeadlines(keyword: String?, country: String): NewsArticlePage {
        return newsApi.fetchTopHeadlines(
            searchQuery = keyword,
            country = country,
            pageSize = 20,
            page = 1
        ).toNewsArticlePage()
    }

    override suspend fun fetchEverything(): NewsArticlePage {
        TODO("Not yet implemented")
    }
}
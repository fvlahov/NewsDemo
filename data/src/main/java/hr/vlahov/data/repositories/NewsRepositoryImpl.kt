package hr.vlahov.data.repositories

import hr.vlahov.data.models.converters.toApiNewsCategory
import hr.vlahov.data.models.converters.toNewsArticlePage
import hr.vlahov.data.networking.apis.NewsApi
import hr.vlahov.domain.models.news.NewsArticlePage
import hr.vlahov.domain.models.news.NewsCategory
import hr.vlahov.domain.repositories.NewsRepository
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val newsApi: NewsApi,
) : NewsRepository {

    override suspend fun fetchTopHeadlines(
        keyword: String?,
        category: NewsCategory?,
        page: Int,
        pageSize: Int,
        country: String,
    ): NewsArticlePage {
        return newsApi.fetchTopHeadlines(
            keyword = keyword,
            country = country,
            category = category?.toApiNewsCategory(),
            pageSize = pageSize,
            page = page
        ).toNewsArticlePage()
    }

    override suspend fun fetchEverything(): NewsArticlePage {
        TODO("Not yet implemented")
    }
}
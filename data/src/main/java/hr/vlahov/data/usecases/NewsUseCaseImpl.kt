package hr.vlahov.data.usecases

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import hr.vlahov.domain.models.news.NewsArticle
import hr.vlahov.domain.models.news.NewsArticlePage
import hr.vlahov.domain.models.news.NewsSource
import hr.vlahov.domain.repositories.NewsRepository
import hr.vlahov.domain.usecases.NewsUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NewsUseCaseImpl @Inject constructor(
    private val newsRepository: NewsRepository,
) : NewsUseCase {

    override val newsSources: Flow<List<NewsSource>> = newsRepository.newsSources

    override fun getLikedNewsArticlesPagingData(pagingConfig: PagingConfig): Flow<PagingData<NewsArticle>> =
        newsRepository.getLikedNewsArticlesPagingData(pagingConfig)

    override suspend fun fetchTopHeadlines(
        keyword: String?,
        sources: List<NewsSource>,
        page: Int,
        pageSize: Int,
        country: String,
    ): NewsArticlePage =
        newsRepository.fetchTopHeadlines(
            keyword = keyword,
            country = country,
            page = page,
            pageSize = pageSize,
            sources = sources
        ).also { newsRepository.saveNewsArticlesToDatabase(it.items) }

    override suspend fun fetchEverything(
        keyword: String?,
        sources: List<NewsSource>,
        page: Int,
        dateFrom: Long?,
        dateTo: Long?,
        pageSize: Int,
    ): NewsArticlePage =
        newsRepository.fetchEverything(
            keyword = keyword,
            page = page,
            dateFrom = dateFrom,
            dateTo = dateTo,
            pageSize = pageSize,
            sources = sources
        ).also { newsRepository.saveNewsArticlesToDatabase(it.items) }

    override suspend fun toggleLikeNewsArticle(newsArticle: NewsArticle, isLiked: Boolean) {
        newsRepository.toggleLikeNewsArticle(newsArticle, isLiked)
    }

    override suspend fun preFetchNewsSources(language: String) {
        newsRepository.fetchNewsSourcesByLanguage(language).also {
            newsRepository.saveNewsSourcesToDatabase(it)
        }
    }

    override suspend fun fetchNewsArticleByUrl(originalArticleUrl: String): NewsArticle? =
        newsRepository.fetchNewsArticle(originalArticleUrl = originalArticleUrl)
}
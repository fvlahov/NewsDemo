package hr.vlahov.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import hr.vlahov.data.database.AppDatabase
import hr.vlahov.data.models.converters.toNewsArticle
import hr.vlahov.data.models.converters.toNewsArticleEntity
import hr.vlahov.data.models.converters.toNewsArticlePage
import hr.vlahov.data.models.converters.toNewsSourceEntity
import hr.vlahov.data.models.converters.toNewsSources
import hr.vlahov.data.models.database.LikedNewsArticleEntity
import hr.vlahov.data.networking.apis.NewsApi
import hr.vlahov.domain.models.news.NewsArticle
import hr.vlahov.domain.models.news.NewsArticlePage
import hr.vlahov.domain.models.news.NewsSource
import hr.vlahov.domain.repositories.NewsRepository
import hr.vlahov.domain.repositories.ProfileRepository
import hr.vlahov.domain.utils.toISO8601Date
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val newsApi: NewsApi,
    private val database: AppDatabase,
    private val profileRepository: ProfileRepository,
) : NewsRepository {

    override val newsSources: Flow<List<NewsSource>> =
        database.newsSourceDao().getAll().map { it.toNewsSources() }

    override fun getLikedNewsArticlesPagingData(pagingConfig: PagingConfig): Flow<PagingData<NewsArticle>> =
        Pager(
            config = pagingConfig,
            pagingSourceFactory = {
                database.likedNewsArticleDao()
                    .getLikedNewsArticlesForProfilePagingSource(profileRepository.fetchCurrentProfile()!!.name)
            }
        ).flow
            .map { likedNewsArticles ->
                likedNewsArticles.map { it.toNewsArticle() }
            }

    override suspend fun fetchTopHeadlines(
        keyword: String?,
        sources: List<NewsSource>,
        page: Int,
        pageSize: Int,
        country: String,
    ): NewsArticlePage {
        return newsApi.fetchTopHeadlines(
            keyword = keyword,
            country = country.takeIf { sources.isEmpty() },
            category = null, //TODO: Implement in the future
            sources = sources.takeIf { it.isNotEmpty() }?.joinToString(",") { it.id },
            pageSize = pageSize,
            page = page
        ).toNewsArticlePage(isNewsArticleLiked = { isNewsArticleLiked(it.originalArticleUrl) })
    }

    override suspend fun fetchEverything(
        keyword: String?,
        dateFrom: Long?,
        dateTo: Long?,
        sources: List<NewsSource>,
        page: Int,
        pageSize: Int,
    ): NewsArticlePage {
        return newsApi.fetchEverything(
            keyword = keyword,
            from = dateFrom?.toISO8601Date(),
            to = dateTo?.toISO8601Date(),
            sources = sources.joinToString(",") { it.id },
            pageSize = pageSize,
            page = page
        ).toNewsArticlePage(isNewsArticleLiked = { isNewsArticleLiked(it.originalArticleUrl) })
    }

    private suspend fun isNewsArticleLiked(articleUrl: String): Boolean {
        return database.likedNewsArticleDao()
            .getLikedArticle(
                originalArticleUrl = articleUrl,
                profileName = profileRepository.fetchCurrentProfile()!!.name
            ) != null
    }

    override suspend fun saveNewsArticlesToDatabase(newsArticles: List<NewsArticle>) {
        database.newsArticleDao().create(newsArticles.map { it.toNewsArticleEntity() })
    }

    override suspend fun fetchNewsArticle(originalArticleUrl: String): NewsArticle? {
        return database.newsArticleDao()
            .fetchNewsArticleByOriginalUrl(originalArticleUrl = originalArticleUrl)?.toNewsArticle()
            ?.copy(isLiked = isNewsArticleLiked(originalArticleUrl))
    }

    override suspend fun toggleLikeNewsArticle(newsArticle: NewsArticle, isLiked: Boolean) {
        if (isLiked)
            database.likedNewsArticleDao().create(
                LikedNewsArticleEntity(
                    newsArticle = newsArticle.toNewsArticleEntity(),
                    profileName = profileRepository.fetchCurrentProfile()?.name!!
                )
            )
        else
            database.likedNewsArticleDao()
                .deleteLikedArticleByOriginalArticleUrl(newsArticle.originalArticleUrl)
    }

    override suspend fun fetchNewsSourcesByLanguage(language: String): List<NewsSource> {
        return newsApi.fetchNewsSources(language = language).toNewsSources()
    }

    override suspend fun saveNewsSourcesToDatabase(newsSources: List<NewsSource>) {
        database.newsSourceDao().create(newsSources.map { it.toNewsSourceEntity() })
    }
}
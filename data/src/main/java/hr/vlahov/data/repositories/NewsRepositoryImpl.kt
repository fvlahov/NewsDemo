package hr.vlahov.data.repositories

import hr.vlahov.data.database.AppDatabase
import hr.vlahov.data.models.converters.toApiNewsCategory
import hr.vlahov.data.models.converters.toNewsArticleEntity
import hr.vlahov.data.models.converters.toNewsArticlePage
import hr.vlahov.data.models.database.LikedNewsArticleEntity
import hr.vlahov.data.networking.apis.NewsApi
import hr.vlahov.domain.models.news.NewsArticle
import hr.vlahov.domain.models.news.NewsArticlePage
import hr.vlahov.domain.models.news.NewsCategory
import hr.vlahov.domain.repositories.NewsRepository
import hr.vlahov.domain.repositories.ProfileRepository
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val newsApi: NewsApi,
    private val database: AppDatabase,
    private val profileRepository: ProfileRepository,
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
        ).toNewsArticlePage(isNewsArticleLiked = { apiNewsArticle ->
            database.likedNewsArticleDao()
                .getLikedArticleByOriginalArticleUrl(apiNewsArticle.originalArticleUrl) != null
        })
    }

    override suspend fun fetchEverything(): NewsArticlePage {
        TODO("Not yet implemented")
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
}
package hr.vlahov.data.models.converters

import hr.vlahov.data.models.api.news.ApiNewsArticle
import hr.vlahov.data.models.api.news.ApiNewsCategory
import hr.vlahov.data.models.api.news.ApiNewsListResponse
import hr.vlahov.data.models.api.news.ApiNewsSource
import hr.vlahov.data.models.api.news.ApiNewsSourceResponse
import hr.vlahov.data.models.database.LikedNewsArticleEntity
import hr.vlahov.data.models.database.NewsArticleEntity
import hr.vlahov.data.models.database.NewsSourceEntity
import hr.vlahov.domain.models.news.NewsArticle
import hr.vlahov.domain.models.news.NewsArticlePage
import hr.vlahov.domain.models.news.NewsCategory
import hr.vlahov.domain.models.news.NewsSource
import java.text.SimpleDateFormat
import java.util.Locale

suspend fun ApiNewsArticle.toNewsArticle(
    isNewsArticleLiked: suspend (ApiNewsArticle) -> Boolean,
) = NewsArticle(
    author = author,
    title = title,
    description = description,
    content = content,
    originalArticleUrl = originalArticleUrl,
    imageUrl = imageUrl,
    publishedAt = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US).parse(publishedAt)!!.time,
    isLiked = isNewsArticleLiked(this)
)

suspend fun ApiNewsListResponse.toNewsArticlePage(
    isNewsArticleLiked: suspend (ApiNewsArticle) -> Boolean,
) = NewsArticlePage(
    totalItems = totalResults,
    items = articles.map { it.toNewsArticle(isNewsArticleLiked) }
)

fun NewsCategory.toApiNewsCategory() = when (this) {
    NewsCategory.BUSINESS -> ApiNewsCategory.BUSINESS
    NewsCategory.ENTERTAINMENT -> ApiNewsCategory.ENTERTAINMENT
    NewsCategory.GENERAL -> ApiNewsCategory.GENERAL
    NewsCategory.HEALTH -> ApiNewsCategory.HEALTH
    NewsCategory.SCIENCE -> ApiNewsCategory.SCIENCE
    NewsCategory.SPORTS -> ApiNewsCategory.SPORTS
    NewsCategory.TECHNOLOGY -> ApiNewsCategory.TECHNOLOGY
}

fun LikedNewsArticleEntity.toNewsArticle() = NewsArticle(
    author = newsArticle.author,
    title = newsArticle.title,
    description = newsArticle.description,
    content = newsArticle.content,
    originalArticleUrl = newsArticle.originalArticleUrl,
    imageUrl = newsArticle.imageUrl,
    publishedAt = newsArticle.publishedAt,
    isLiked = true
)

fun Collection<LikedNewsArticleEntity>.toNewsArticles() = map { it.toNewsArticle() }

fun NewsArticle.toNewsArticleEntity() = NewsArticleEntity(
    originalArticleUrl = originalArticleUrl,
    imageUrl = imageUrl,
    author = author,
    title = title,
    description = description,
    publishedAt = publishedAt,
    content = content
)

fun NewsArticleEntity.toNewsArticle() = NewsArticle(
    author = author,
    title = title,
    description = description,
    content = content,
    originalArticleUrl = originalArticleUrl,
    imageUrl = imageUrl,
    publishedAt = publishedAt,
    isLiked = false
)

fun ApiNewsSource.toNewsSource() = NewsSource(
    id = id,
    name = name,
    description = description,
    url = url,
    country = country
)

fun NewsSourceEntity.toNewsSource() = NewsSource(
    id = id,
    name = name,
    description = description,
    url = url,
    country = country
)

fun ApiNewsSourceResponse.toNewsSources() = sources.map { it.toNewsSource() }

fun Collection<NewsSourceEntity>.toNewsSources() = map { it.toNewsSource() }

fun NewsSource.toNewsSourceEntity() = NewsSourceEntity(
    id = id,
    name = name,
    description = description,
    url = url,
    country = country
)

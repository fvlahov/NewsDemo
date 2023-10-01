package hr.vlahov.data.models.converters

import hr.vlahov.data.models.api.news.ApiNewsArticle
import hr.vlahov.data.models.api.news.ApiNewsCategory
import hr.vlahov.data.models.api.news.ApiNewsListResponse
import hr.vlahov.data.models.database.LikedNewsArticleEntity
import hr.vlahov.domain.models.news.NewsArticle
import hr.vlahov.domain.models.news.NewsArticlePage
import hr.vlahov.domain.models.news.NewsCategory

fun ApiNewsArticle.toNewsArticle() = NewsArticle(
    author = author,
    title = title,
    description = description,
    content = content,
    originalArticleUrl = originalArticleUrl,
    imageUrl = imageUrl
)

fun ApiNewsListResponse.toNewsArticlePage() = NewsArticlePage(
    totalItems = totalResults,
    items = articles.map { it.toNewsArticle() }
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
    imageUrl = newsArticle.imageUrl
)

fun Collection<LikedNewsArticleEntity>.toNewsArticles() = map { it.toNewsArticle() }

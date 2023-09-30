package hr.vlahov.data.models.converters

import hr.vlahov.data.models.api.news.ApiNewsArticle
import hr.vlahov.data.models.api.news.ApiNewsListResponse
import hr.vlahov.domain.models.news.NewsArticle
import hr.vlahov.domain.models.news.NewsArticlePage

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

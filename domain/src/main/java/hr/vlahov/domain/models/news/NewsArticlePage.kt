package hr.vlahov.domain.models.news

data class NewsArticlePage(
    val totalItems: Int,
    val items: List<NewsArticle>,
)
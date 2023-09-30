package hr.vlahov.domain.models.news

data class NewsArticle(
    val author: String,
    val title: String,
    val description: String,
    val content: String,
    val originalArticleUrl: String,
    val imageUrl: String?,
)
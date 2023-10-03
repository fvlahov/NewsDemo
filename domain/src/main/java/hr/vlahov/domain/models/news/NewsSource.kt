package hr.vlahov.domain.models.news

data class NewsSource(
    val id: String,
    val name: String,
    val description: String,
    val url: String,
    val country: String,
)

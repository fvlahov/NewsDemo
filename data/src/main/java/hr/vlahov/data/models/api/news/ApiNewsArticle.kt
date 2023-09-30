package hr.vlahov.data.models.api.news

import com.google.gson.annotations.SerializedName

data class ApiNewsArticle(
    @SerializedName("source")
    val source: ApiNewsSource,
    @SerializedName("author")
    val author: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("url")
    val originalArticleUrl: String,
    @SerializedName("urlToImage")
    val imageUrl: String?,
    @SerializedName("publishedAt")
    val publishedAt: String,
    @SerializedName("content")
    val content: String,
)
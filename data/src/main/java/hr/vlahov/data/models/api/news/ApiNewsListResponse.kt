package hr.vlahov.data.models.api.news

import com.google.gson.annotations.SerializedName

data class ApiNewsListResponse(
    @SerializedName("totalResults")
    val totalResults: Int,
    @SerializedName("articles")
    val articles: List<ApiNewsArticle>,
)
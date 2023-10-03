package hr.vlahov.data.models.api.news

import com.google.gson.annotations.SerializedName

data class ApiNewsSource(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("country")
    val country: String,
)
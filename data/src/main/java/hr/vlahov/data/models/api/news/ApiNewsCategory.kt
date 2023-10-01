package hr.vlahov.data.models.api.news

import com.google.gson.annotations.SerializedName

enum class ApiNewsCategory {
    @SerializedName("business")
    BUSINESS,

    @SerializedName("entertainment")
    ENTERTAINMENT,

    @SerializedName("general")
    GENERAL,

    @SerializedName("health")
    HEALTH,

    @SerializedName("science")
    SCIENCE,

    @SerializedName("sports")
    SPORTS,

    @SerializedName("technology")
    TECHNOLOGY,
}

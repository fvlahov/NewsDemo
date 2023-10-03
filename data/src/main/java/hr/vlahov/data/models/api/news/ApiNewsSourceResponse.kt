package hr.vlahov.data.models.api.news

import com.google.gson.annotations.SerializedName

data class ApiNewsSourceResponse(
    @SerializedName("sources")
    val sources: List<ApiNewsSource>,
)
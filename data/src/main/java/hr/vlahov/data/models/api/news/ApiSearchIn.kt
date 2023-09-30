package hr.vlahov.data.models.api.news

import com.google.gson.annotations.SerializedName


enum class ApiSearchIn {
    @SerializedName("title")
    TITLE,

    @SerializedName("description")
    DESCRIPTION,

    @SerializedName("content")
    CONTENT,
}

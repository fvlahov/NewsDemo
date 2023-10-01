package hr.vlahov.data.models.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "NewsArticles"
)
data class NewsArticleEntity(
    @PrimaryKey
    val originalArticleUrl: String,
    val imageUrl: String?,
    val author: String?,
    val title: String,
    val description: String?,
    val content: String?,
)
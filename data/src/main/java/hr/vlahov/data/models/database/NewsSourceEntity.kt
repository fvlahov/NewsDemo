package hr.vlahov.data.models.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "NewsSources",
)
data class NewsSourceEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String,
    val url: String,
    val country: String,
)
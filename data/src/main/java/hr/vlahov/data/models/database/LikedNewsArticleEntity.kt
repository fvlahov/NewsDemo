package hr.vlahov.data.models.database

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "LikedNewsArticles",
    foreignKeys = [ForeignKey(
        ProfileEntity::class,
        parentColumns = ["name"],
        childColumns = ["profileName"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class LikedNewsArticleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @Embedded
    val newsArticle: NewsArticleEntity,
    val profileName: String,
)
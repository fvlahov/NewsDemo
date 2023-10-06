package hr.vlahov.data.models.database

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.RoomWarnings

@SuppressWarnings(RoomWarnings.PRIMARY_KEY_FROM_EMBEDDED_IS_DROPPED)
@Entity(
    tableName = "LikedNewsArticles",
    foreignKeys = [ForeignKey(
        ProfileEntity::class,
        parentColumns = ["name"],
        childColumns = ["profileName"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("profileName")]
)
data class LikedNewsArticleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    @Embedded
    val newsArticle: NewsArticleEntity,
    val profileName: String,
)
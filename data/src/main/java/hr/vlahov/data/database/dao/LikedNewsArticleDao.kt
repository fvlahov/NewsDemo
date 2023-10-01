package hr.vlahov.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import hr.vlahov.data.models.database.LikedNewsArticleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LikedNewsArticleDao : CRUDDao<LikedNewsArticleEntity> {

    @Query("SELECT * FROM LikedNewsArticles WHERE profileName = :profileName")
    fun getAllForProfile(profileName: String): Flow<List<LikedNewsArticleEntity>>
}
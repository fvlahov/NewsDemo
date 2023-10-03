package hr.vlahov.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import hr.vlahov.data.models.database.NewsSourceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsSourceDao : CRUDDao<NewsSourceEntity> {
    @Query("SELECT * FROM NewsSources")
    fun getAll(): Flow<List<NewsSourceEntity>>
}
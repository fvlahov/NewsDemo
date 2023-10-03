package hr.vlahov.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import hr.vlahov.data.models.database.NewsArticleEntity

@Dao
interface NewsArticleDao : CRUDDao<NewsArticleEntity> {

    @Query("SELECT * FROM NewsArticles WHERE originalArticleUrl = :originalArticleUrl LIMIT 1")
    suspend fun fetchNewsArticleByOriginalUrl(originalArticleUrl: String): NewsArticleEntity?

}
package hr.vlahov.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import hr.vlahov.data.models.database.ProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfileDao : CRUDDao<ProfileEntity> {

    @Query("SELECT * FROM Profiles")
    fun getAll(): Flow<List<ProfileEntity>>

    @Query("SELECT * FROM Profiles WHERE name = :profileName")
    suspend fun getByName(profileName: String): ProfileEntity?
}
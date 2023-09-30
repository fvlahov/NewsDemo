package hr.vlahov.data.database.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

interface CRUDDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun create(item: T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun create(item: List<T>)

    @Delete
    suspend fun delete(item: T)

    @Update
    suspend fun update(item: T)
}
package hr.vlahov.data.models.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "Profiles"
)
data class ProfileEntity(
    @PrimaryKey
    val name: String,
)
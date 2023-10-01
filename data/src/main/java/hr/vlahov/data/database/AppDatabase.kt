package hr.vlahov.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import hr.vlahov.data.database.dao.LikedNewsArticleDao
import hr.vlahov.data.database.dao.ProfileDao
import hr.vlahov.data.models.database.LikedNewsArticleEntity
import hr.vlahov.data.models.database.NewsArticleEntity
import hr.vlahov.data.models.database.ProfileEntity

@Database(
    exportSchema = false,
    entities = [
        ProfileEntity::class,
        NewsArticleEntity::class,
        LikedNewsArticleEntity::class
    ],
    version = 2,
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun profileDao(): ProfileDao
    abstract fun newsArticleDao(): LikedNewsArticleDao

    companion object {
        fun build(context: Context): AppDatabase =
            Room.databaseBuilder(
                context = context,
                klass = AppDatabase::class.java,
                name = DATABASE_NAME
            )
                .addMigrations(*Migrations.allMigrations)
                .fallbackToDestructiveMigration()
                .build()

        private const val DATABASE_NAME = "app_database"
    }
}
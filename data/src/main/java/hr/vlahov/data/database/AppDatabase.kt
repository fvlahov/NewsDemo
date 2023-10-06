package hr.vlahov.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import hr.vlahov.data.database.dao.LikedNewsArticleDao
import hr.vlahov.data.database.dao.NewsArticleDao
import hr.vlahov.data.database.dao.NewsSourceDao
import hr.vlahov.data.database.dao.ProfileDao
import hr.vlahov.data.models.database.LikedNewsArticleEntity
import hr.vlahov.data.models.database.NewsArticleEntity
import hr.vlahov.data.models.database.NewsSourceEntity
import hr.vlahov.data.models.database.ProfileEntity

@Database(
    exportSchema = false,
    entities = [
        ProfileEntity::class,
        NewsArticleEntity::class,
        LikedNewsArticleEntity::class,
        NewsSourceEntity::class
    ],
    version = 1,
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun profileDao(): ProfileDao
    abstract fun likedNewsArticleDao(): LikedNewsArticleDao
    abstract fun newsSourceDao(): NewsSourceDao
    abstract fun newsArticleDao(): NewsArticleDao

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
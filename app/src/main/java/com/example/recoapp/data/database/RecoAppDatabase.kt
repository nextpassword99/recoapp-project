package com.example.recoapp.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.example.recoapp.data.dao.CategoryDao
import com.example.recoapp.data.dao.RecommendationDao
import com.example.recoapp.data.dao.UserDao
import com.example.recoapp.data.dao.UserRecommendationDao
import com.example.recoapp.data.entities.Category
import com.example.recoapp.data.entities.Recommendation
import com.example.recoapp.data.entities.User
import com.example.recoapp.data.entities.UserRecommendation

@Database(
    entities = [
        User::class,
        Category::class,
        Recommendation::class,
        UserRecommendation::class
    ],
    version = 1,
    exportSchema = false
)
abstract class RecoAppDatabase : RoomDatabase() {
    
    abstract fun userDao(): UserDao
    abstract fun categoryDao(): CategoryDao
    abstract fun recommendationDao(): RecommendationDao
    abstract fun userRecommendationDao(): UserRecommendationDao
    
    companion object {
        @Volatile
        private var INSTANCE: RecoAppDatabase? = null
        
        fun getDatabase(context: Context): RecoAppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RecoAppDatabase::class.java,
                    "recoapp_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
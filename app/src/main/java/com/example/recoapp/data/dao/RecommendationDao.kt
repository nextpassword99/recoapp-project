package com.example.recoapp.data.dao

import androidx.room.*
import com.example.recoapp.data.entities.Recommendation
import kotlinx.coroutines.flow.Flow

@Dao
interface RecommendationDao {
    
    @Query("SELECT * FROM recommendations ORDER BY createdAt DESC")
    fun getAllRecommendations(): Flow<List<Recommendation>>
    
    @Query("SELECT * FROM recommendations WHERE id = :id")
    suspend fun getRecommendationById(id: Long): Recommendation?
    
    @Query("SELECT * FROM recommendations WHERE categoryId = :categoryId ORDER BY rating DESC")
    fun getRecommendationsByCategory(categoryId: Long): Flow<List<Recommendation>>
    
    @Query("SELECT * FROM recommendations WHERE rating >= :minRating ORDER BY rating DESC")
    fun getRecommendationsByRating(minRating: Float): Flow<List<Recommendation>>
    
    @Query("SELECT * FROM recommendations WHERE title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%'")
    fun searchRecommendations(query: String): Flow<List<Recommendation>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecommendation(recommendation: Recommendation): Long
    
    @Update
    suspend fun updateRecommendation(recommendation: Recommendation)
    
    @Delete
    suspend fun deleteRecommendation(recommendation: Recommendation)
    
    @Query("DELETE FROM recommendations WHERE id = :id")
    suspend fun deleteRecommendationById(id: Long)
}
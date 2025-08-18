package com.example.recoapp.data.dao

import androidx.room.*
import com.example.recoapp.data.entities.UserRecommendation
import kotlinx.coroutines.flow.Flow

@Dao
interface UserRecommendationDao {
    
    @Query("SELECT * FROM user_recommendations WHERE userId = :userId")
    fun getUserRecommendations(userId: Long): Flow<List<UserRecommendation>>
    
    @Query("SELECT * FROM user_recommendations WHERE userId = :userId AND isFavorite = 1")
    fun getUserFavorites(userId: Long): Flow<List<UserRecommendation>>
    
    @Query("SELECT * FROM user_recommendations WHERE userId = :userId AND recommendationId = :recommendationId")
    suspend fun getUserRecommendation(userId: Long, recommendationId: Long): UserRecommendation?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserRecommendation(userRecommendation: UserRecommendation)
    
    @Update
    suspend fun updateUserRecommendation(userRecommendation: UserRecommendation)
    
    @Delete
    suspend fun deleteUserRecommendation(userRecommendation: UserRecommendation)
    
    @Query("DELETE FROM user_recommendations WHERE userId = :userId AND recommendationId = :recommendationId")
    suspend fun deleteUserRecommendation(userId: Long, recommendationId: Long)
    
    @Query("UPDATE user_recommendations SET isFavorite = :isFavorite WHERE userId = :userId AND recommendationId = :recommendationId")
    suspend fun updateFavoriteStatus(userId: Long, recommendationId: Long, isFavorite: Boolean)
}
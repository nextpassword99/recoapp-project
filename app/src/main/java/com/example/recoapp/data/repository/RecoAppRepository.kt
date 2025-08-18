package com.example.recoapp.data.repository

import com.example.recoapp.data.dao.CategoryDao
import com.example.recoapp.data.dao.RecommendationDao
import com.example.recoapp.data.dao.UserDao
import com.example.recoapp.data.dao.UserRecommendationDao
import com.example.recoapp.data.entities.Category
import com.example.recoapp.data.entities.Recommendation
import com.example.recoapp.data.entities.User
import com.example.recoapp.data.entities.UserRecommendation
import kotlinx.coroutines.flow.Flow

class RecoAppRepository(
    private val userDao: UserDao,
    private val categoryDao: CategoryDao,
    private val recommendationDao: RecommendationDao,
    private val userRecommendationDao: UserRecommendationDao
) {
    
    // User operations
    fun getAllUsers(): Flow<List<User>> = userDao.getAllUsers()
    suspend fun getUserById(id: Long): User? = userDao.getUserById(id)
    suspend fun getUserByEmail(email: String): User? = userDao.getUserByEmail(email)
    suspend fun insertUser(user: User): Long = userDao.insertUser(user)
    suspend fun updateUser(user: User) = userDao.updateUser(user)
    suspend fun deleteUser(user: User) = userDao.deleteUser(user)
    
    // Category operations
    fun getAllCategories(): Flow<List<Category>> = categoryDao.getAllCategories()
    suspend fun getCategoryById(id: Long): Category? = categoryDao.getCategoryById(id)
    suspend fun getCategoryByName(name: String): Category? = categoryDao.getCategoryByName(name)
    suspend fun insertCategory(category: Category): Long = categoryDao.insertCategory(category)
    suspend fun updateCategory(category: Category) = categoryDao.updateCategory(category)
    suspend fun deleteCategory(category: Category) = categoryDao.deleteCategory(category)
    
    // Recommendation operations
    fun getAllRecommendations(): Flow<List<Recommendation>> = recommendationDao.getAllRecommendations()
    suspend fun getRecommendationById(id: Long): Recommendation? = recommendationDao.getRecommendationById(id)
    fun getRecommendationsByCategory(categoryId: Long): Flow<List<Recommendation>> = 
        recommendationDao.getRecommendationsByCategory(categoryId)
    fun getRecommendationsByRating(minRating: Float): Flow<List<Recommendation>> = 
        recommendationDao.getRecommendationsByRating(minRating)
    fun searchRecommendations(query: String): Flow<List<Recommendation>> = 
        recommendationDao.searchRecommendations(query)
    suspend fun insertRecommendation(recommendation: Recommendation): Long = 
        recommendationDao.insertRecommendation(recommendation)
    suspend fun updateRecommendation(recommendation: Recommendation) = 
        recommendationDao.updateRecommendation(recommendation)
    suspend fun deleteRecommendation(recommendation: Recommendation) = 
        recommendationDao.deleteRecommendation(recommendation)
    
    // User-Recommendation operations
    fun getUserRecommendations(userId: Long): Flow<List<UserRecommendation>> = 
        userRecommendationDao.getUserRecommendations(userId)
    fun getUserFavorites(userId: Long): Flow<List<UserRecommendation>> = 
        userRecommendationDao.getUserFavorites(userId)
    suspend fun getUserRecommendation(userId: Long, recommendationId: Long): UserRecommendation? = 
        userRecommendationDao.getUserRecommendation(userId, recommendationId)
    suspend fun insertUserRecommendation(userRecommendation: UserRecommendation) = 
        userRecommendationDao.insertUserRecommendation(userRecommendation)
    suspend fun updateUserRecommendation(userRecommendation: UserRecommendation) = 
        userRecommendationDao.updateUserRecommendation(userRecommendation)
    suspend fun deleteUserRecommendation(userRecommendation: UserRecommendation) = 
        userRecommendationDao.deleteUserRecommendation(userRecommendation)
    suspend fun updateFavoriteStatus(userId: Long, recommendationId: Long, isFavorite: Boolean) = 
        userRecommendationDao.updateFavoriteStatus(userId, recommendationId, isFavorite)
}
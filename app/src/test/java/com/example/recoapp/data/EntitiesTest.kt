package com.example.recoapp.data

import com.example.recoapp.data.entities.Category
import com.example.recoapp.data.entities.Recommendation
import com.example.recoapp.data.entities.User
import com.example.recoapp.data.entities.UserRecommendation
import org.junit.Assert.*
import org.junit.Test

class EntitiesTest {
    
    @Test
    fun createUser() {
        val user = User(
            id = 1,
            name = "Test User", 
            email = "test@example.com",
            createdAt = System.currentTimeMillis()
        )
        
        assertEquals(1L, user.id)
        assertEquals("Test User", user.name)
        assertEquals("test@example.com", user.email)
        assertTrue(user.createdAt > 0)
    }
    
    @Test
    fun createCategory() {
        val category = Category(
            id = 1,
            name = "Movies",
            description = "Movie recommendations"
        )
        
        assertEquals(1L, category.id)
        assertEquals("Movies", category.name)
        assertEquals("Movie recommendations", category.description)
        assertTrue(category.createdAt > 0)
    }
    
    @Test
    fun createRecommendation() {
        val recommendation = Recommendation(
            id = 1,
            title = "Great Movie",
            description = "A really great movie",
            categoryId = 1,
            rating = 4.5f,
            imageUrl = "http://example.com/image.jpg"
        )
        
        assertEquals(1L, recommendation.id)
        assertEquals("Great Movie", recommendation.title)
        assertEquals("A really great movie", recommendation.description)
        assertEquals(1L, recommendation.categoryId)
        assertEquals(4.5f, recommendation.rating)
        assertEquals("http://example.com/image.jpg", recommendation.imageUrl)
        assertTrue(recommendation.createdAt > 0)
    }
    
    @Test
    fun createUserRecommendation() {
        val userRec = UserRecommendation(
            userId = 1,
            recommendationId = 2,
            isFavorite = true
        )
        
        assertEquals(1L, userRec.userId)
        assertEquals(2L, userRec.recommendationId)
        assertTrue(userRec.isFavorite)
        assertTrue(userRec.createdAt > 0)
    }
    
    @Test
    fun defaultValuesWork() {
        val user = User(name = "Test", email = "test@example.com")
        assertEquals(0L, user.id) // Default auto-generate value
        assertTrue(user.createdAt > 0) // Default timestamp
        
        val category = Category(name = "Test", description = null)
        assertEquals(0L, category.id)
        assertNull(category.description)
        
        val recommendation = Recommendation(
            title = "Test",
            description = "Test desc",
            categoryId = 1
        )
        assertEquals(0f, recommendation.rating) // Default rating
        assertNull(recommendation.imageUrl) // Default null
    }
}
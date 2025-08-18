package com.example.recoapp.data.database

import com.example.recoapp.data.entities.Category
import com.example.recoapp.data.entities.Recommendation
import com.example.recoapp.data.entities.User

object DatabaseUtils {
    
    fun getSampleUsers(): List<User> = listOf(
        User(name = "John Doe", email = "john@example.com"),
        User(name = "Jane Smith", email = "jane@example.com"),
        User(name = "Bob Johnson", email = "bob@example.com")
    )
    
    fun getSampleCategories(): List<Category> = listOf(
        Category(name = "Movies", description = "Film recommendations"),
        Category(name = "Books", description = "Book recommendations"),
        Category(name = "Music", description = "Music and album recommendations"),
        Category(name = "Restaurants", description = "Food and restaurant recommendations"),
        Category(name = "Travel", description = "Travel destination recommendations")
    )
    
    fun getSampleRecommendations(): List<Recommendation> = listOf(
        Recommendation(
            title = "The Shawshank Redemption",
            description = "A classic drama about hope and friendship",
            categoryId = 1, // Movies
            rating = 4.9f,
            imageUrl = null
        ),
        Recommendation(
            title = "Inception",
            description = "A mind-bending science fiction thriller",
            categoryId = 1, // Movies
            rating = 4.7f,
            imageUrl = null
        ),
        Recommendation(
            title = "1984",
            description = "George Orwell's dystopian masterpiece",
            categoryId = 2, // Books
            rating = 4.6f,
            imageUrl = null
        ),
        Recommendation(
            title = "To Kill a Mockingbird",
            description = "Harper Lee's classic novel",
            categoryId = 2, // Books
            rating = 4.5f,
            imageUrl = null
        ),
        Recommendation(
            title = "Abbey Road",
            description = "The Beatles' iconic album",
            categoryId = 3, // Music
            rating = 4.8f,
            imageUrl = null
        )
    )
    
    suspend fun populateDatabase(database: RecoAppDatabase) {
        // Insert sample categories
        getSampleCategories().forEach { category ->
            database.categoryDao().insertCategory(category)
        }
        
        // Insert sample users  
        getSampleUsers().forEach { user ->
            database.userDao().insertUser(user)
        }
        
        // Insert sample recommendations
        getSampleRecommendations().forEach { recommendation ->
            database.recommendationDao().insertRecommendation(recommendation)
        }
    }
}
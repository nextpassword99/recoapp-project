# RecoApp Database Implementation

## Overview

This implementation provides a complete SQLite database using Android's Room persistence library for the RecoApp recommendation system.

## Database Schema

### Entities

1. **User** (`users` table)
   - `id`: Primary key (auto-generated)
   - `name`: User's display name
   - `email`: User's email address
   - `createdAt`: Timestamp of user creation

2. **Category** (`categories` table)
   - `id`: Primary key (auto-generated)
   - `name`: Category name
   - `description`: Category description
   - `createdAt`: Timestamp of category creation

3. **Recommendation** (`recommendations` table)
   - `id`: Primary key (auto-generated)
   - `title`: Recommendation title
   - `description`: Recommendation description
   - `categoryId`: Foreign key to Category
   - `rating`: Recommendation rating (0.0 to 5.0)
   - `imageUrl`: Optional image URL
   - `createdAt`: Timestamp of recommendation creation

4. **UserRecommendation** (`user_recommendations` table)
   - `userId`: Foreign key to User
   - `recommendationId`: Foreign key to Recommendation
   - `isFavorite`: Boolean flag for favorites
   - `createdAt`: Timestamp when user interacted with recommendation

## Data Access Objects (DAOs)

- **UserDao**: Operations for user management
- **CategoryDao**: Operations for category management
- **RecommendationDao**: Operations for recommendation management with search and filtering
- **UserRecommendationDao**: Operations for user-recommendation relationships

## Repository Pattern

The `RecoAppRepository` class provides a clean API layer that encapsulates all database operations and provides a single point of access for the UI layer.

## Usage Example

```kotlin
// Initialize database
val database = RecoAppDatabase.getDatabase(context)
val repository = RecoAppRepository(
    database.userDao(),
    database.categoryDao(),
    database.recommendationDao(),
    database.userRecommendationDao()
)

// Insert a new user
val user = User(name = "John Doe", email = "john@example.com")
val userId = repository.insertUser(user)

// Get all categories (Flow for reactive updates)
repository.getAllCategories().collect { categories ->
    // Handle categories list
}
```

## Testing

The implementation includes unit tests in `DatabaseTest.kt` that validate:
- Entity insertion and retrieval
- DAO operations
- Reactive Flow updates
- Foreign key relationships

## Features

- **Reactive Updates**: Uses Kotlin Flow for reactive database queries
- **Foreign Key Constraints**: Ensures data integrity with cascading deletes
- **Search Functionality**: Full-text search for recommendations
- **Rating System**: Support for recommendation ratings
- **User Favorites**: Track user preferences and favorites
- **Sample Data**: Utility functions to populate database with sample data

## Database Location

The SQLite database file is created as `recoapp_database` in the app's internal storage.
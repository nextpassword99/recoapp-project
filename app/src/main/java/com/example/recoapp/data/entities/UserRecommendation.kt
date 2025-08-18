package com.example.recoapp.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "user_recommendations",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Recommendation::class,
            parentColumns = ["id"],
            childColumns = ["recommendationId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    primaryKeys = ["userId", "recommendationId"],
    indices = [
        Index(value = ["userId"]),
        Index(value = ["recommendationId"])
    ]
)
data class UserRecommendation(
    val userId: Long,
    val recommendationId: Long,
    val isFavorite: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
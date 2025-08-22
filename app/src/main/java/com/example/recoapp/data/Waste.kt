package com.example.recoapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import java.util.Date
import java.util.UUID

@Entity(tableName = "waste")
data class Waste(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),

    @ColumnInfo(name = "type")
    val type: String,

    @ColumnInfo(name = "quantity")
    val quantity: Double,

    @ColumnInfo(name = "location")
    val location: String,

    @ColumnInfo(name = "date")
    val date: Date,

    @ColumnInfo(name = "comments")
    val comment: String? = null,

    @ColumnInfo(name = "modified_at")
    val modifiedAt: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "deleted")
    val deleted: Boolean = false
)

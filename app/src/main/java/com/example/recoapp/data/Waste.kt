package com.example.recoapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import java.util.Date

@Entity(tableName = "waste")
data class Waste(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "type")
    val type: String,

    @ColumnInfo(name = "quantity")
    val quantity: Double,

    @ColumnInfo(name = "location")
    val location: String,

    @ColumnInfo(name = "date")
    val date: Date,

    @ColumnInfo(name = "comments")
    val comment: String? = null
)

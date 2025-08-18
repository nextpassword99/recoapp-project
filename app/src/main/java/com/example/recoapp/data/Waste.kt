package com.example.recoapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import java.util.Date

@Entity("waste")
data class Waste (
    @PrimaryKey(true)
    val id: Int = 0,

    @ColumnInfo("type")
    val type: String,

    @ColumnInfo("quantity")
    val quantity: String,

    @ColumnInfo("location")
    val location: String,

    @ColumnInfo("date")
    val date: Date,

    @ColumnInfo("comments")
    val comment: String?,

)
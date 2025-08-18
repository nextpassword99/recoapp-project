package com.example.recoapp.data

import androidx.room.*
@Dao
interface WasteDao {
    @Insert
    suspend fun insert(waste: Waste)

    @Update
    suspend fun update(waste: Waste)

    @Delete
    suspend fun delete(waste: Waste)

    @Query("SELECT * FROM waste ORDER BY date DESC")
    suspend fun query(waste: Waste)

    suspend fun getAllWaste(): List<Waste>
}
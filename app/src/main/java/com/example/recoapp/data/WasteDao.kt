package com.example.recoapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WasteDao {
    @Insert
    suspend fun insert(waste: Waste)

    @Update
    suspend fun update(waste: Waste)

    @Delete
    suspend fun delete(waste: Waste)

    @Query("SELECT * FROM waste ORDER BY date DESC")
    fun getAllWaste(): Flow<List<Waste>>


    @Query("SELECT * FROM waste WHERE (:type IS NULL OR type = :type) AND date BETWEEN :startDate AND :endDate")
    suspend fun getWasteByFilters(startDate: Long, endDate: Long, type: String?): List<Waste>
}
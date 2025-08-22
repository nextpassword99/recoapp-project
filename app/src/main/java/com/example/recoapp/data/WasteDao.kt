package com.example.recoapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WasteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(waste: Waste)

    @Update
    suspend fun update(waste: Waste)

    @Delete
    suspend fun delete(waste: Waste)

    @Query("SELECT * FROM waste WHERE deleted = 0 ORDER BY date DESC")
    fun getAllWaste(): Flow<List<Waste>>


    @Query("SELECT * FROM waste WHERE (:type IS NULL OR type = :type) AND date BETWEEN :startDate AND :endDate")
    suspend fun getWasteByFilters(startDate: Long, endDate: Long, type: String?): List<Waste>

    @Query("SELECT * FROM waste WHERE modified_at > :since")
    suspend fun getModifiedSince(since: Long): List<Waste>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(items: List<Waste>)

    @Query("DELETE FROM waste WHERE id = :id")
    suspend fun hardDeleteById(id: String)

    @Query("SELECT * FROM waste WHERE id = :id LIMIT 1")
    suspend fun findById(id: String): Waste?
}
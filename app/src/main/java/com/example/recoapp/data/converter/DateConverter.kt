package com.example.recoapp.data.converter

import androidx.room.TypeConverter
import java.util.Date

/**
 * Convertidor de tipos para Room Database
 * 
 * Permite convertir objetos Date a Long (timestamp) y viceversa
 * para almacenar fechas en la base de datos SQLite
 */
class DateConverter {
    
    /**
     * Convierte un timestamp (Long) a Date
     */
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }
    
    /**
     * Convierte un Date a timestamp (Long)
     */
    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}
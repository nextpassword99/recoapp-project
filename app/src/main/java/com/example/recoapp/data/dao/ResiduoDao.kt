package com.example.recoapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.recoapp.data.entity.Residuo
import java.util.Date

/**
 * Data Access Object (DAO) para operaciones CRUD de residuos
 * 
 * Define todas las operaciones de base de datos para la entidad Residuo
 * incluyendo funciones de filtrado y reportes para ECOLIM S.A.C.
 */
@Dao
interface ResiduoDao {
    
    /**
     * Obtiene todos los residuos ordenados por fecha (más reciente primero)
     */
    @Query("SELECT * FROM residuos ORDER BY fecha DESC")
    fun getAllResiduos(): LiveData<List<Residuo>>
    
    /**
     * Obtiene un residuo específico por su ID
     */
    @Query("SELECT * FROM residuos WHERE id = :id")
    suspend fun getResiduoById(id: Long): Residuo?
    
    /**
     * Inserta un nuevo residuo en la base de datos
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResiduo(residuo: Residuo): Long
    
    /**
     * Actualiza un residuo existente
     */
    @Update
    suspend fun updateResiduo(residuo: Residuo)
    
    /**
     * Elimina un residuo específico
     */
    @Delete
    suspend fun deleteResiduo(residuo: Residuo)
    
    /**
     * Elimina todos los residuos
     */
    @Query("DELETE FROM residuos")
    suspend fun deleteAllResiduos()
    
    /**
     * Filtra residuos por tipo
     */
    @Query("SELECT * FROM residuos WHERE tipo = :tipo ORDER BY fecha DESC")
    fun getResiduosByTipo(tipo: String): LiveData<List<Residuo>>
    
    /**
     * Filtra residuos por rango de fechas
     */
    @Query("SELECT * FROM residuos WHERE fecha BETWEEN :fechaInicio AND :fechaFin ORDER BY fecha DESC")
    fun getResiduosByFecha(fechaInicio: Date, fechaFin: Date): LiveData<List<Residuo>>
    
    /**
     * Filtra residuos por ubicación
     */
    @Query("SELECT * FROM residuos WHERE ubicacion LIKE '%' || :ubicacion || '%' ORDER BY fecha DESC")
    fun getResiduosByUbicacion(ubicacion: String): LiveData<List<Residuo>>
    
    /**
     * Obtiene la cantidad total de residuos recolectados
     */
    @Query("SELECT SUM(cantidad) FROM residuos")
    suspend fun getTotalCantidad(): Double?
    
    /**
     * Obtiene estadísticas por tipo de residuo
     */
    @Query("SELECT tipo, SUM(cantidad) as total FROM residuos GROUP BY tipo ORDER BY total DESC")
    suspend fun getEstadisticasPorTipo(): List<EstadisticaTipo>
    
    /**
     * Obtiene los residuos más recientes (últimos 10)
     */
    @Query("SELECT * FROM residuos ORDER BY fecha DESC LIMIT 10")
    suspend fun getResiduosRecientes(): List<Residuo>
}

/**
 * Data class para estadísticas por tipo de residuo
 */
data class EstadisticaTipo(
    val tipo: String,
    val total: Double
)
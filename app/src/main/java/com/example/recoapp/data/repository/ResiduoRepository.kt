package com.example.recoapp.data.repository

import androidx.lifecycle.LiveData
import com.example.recoapp.data.dao.EstadisticaTipo
import com.example.recoapp.data.dao.ResiduoDao
import com.example.recoapp.data.entity.Residuo
import java.util.Date

/**
 * Repositorio para gestionar los datos de residuos
 * 
 * Esta clase actúa como una capa de abstracción entre el DAO y los ViewModels,
 * implementando el patrón Repository para una arquitectura limpia.
 */
class ResiduoRepository(private val residuoDao: ResiduoDao) {
    
    /**
     * Obtiene todos los residuos como LiveData
     */
    fun getAllResiduos(): LiveData<List<Residuo>> = residuoDao.getAllResiduos()
    
    /**
     * Obtiene un residuo específico por ID
     */
    suspend fun getResiduoById(id: Long): Residuo? = residuoDao.getResiduoById(id)
    
    /**
     * Inserta un nuevo residuo
     */
    suspend fun insertResiduo(residuo: Residuo): Long = residuoDao.insertResiduo(residuo)
    
    /**
     * Actualiza un residuo existente
     */
    suspend fun updateResiduo(residuo: Residuo) = residuoDao.updateResiduo(residuo)
    
    /**
     * Elimina un residuo
     */
    suspend fun deleteResiduo(residuo: Residuo) = residuoDao.deleteResiduo(residuo)
    
    /**
     * Filtra residuos por tipo
     */
    fun getResiduosByTipo(tipo: String): LiveData<List<Residuo>> = 
        residuoDao.getResiduosByTipo(tipo)
    
    /**
     * Filtra residuos por rango de fechas
     */
    fun getResiduosByFecha(fechaInicio: Date, fechaFin: Date): LiveData<List<Residuo>> = 
        residuoDao.getResiduosByFecha(fechaInicio, fechaFin)
    
    /**
     * Filtra residuos por ubicación
     */
    fun getResiduosByUbicacion(ubicacion: String): LiveData<List<Residuo>> = 
        residuoDao.getResiduosByUbicacion(ubicacion)
    
    /**
     * Obtiene la cantidad total recolectada
     */
    suspend fun getTotalCantidad(): Double = residuoDao.getTotalCantidad() ?: 0.0
    
    /**
     * Obtiene estadísticas por tipo de residuo
     */
    suspend fun getEstadisticasPorTipo(): List<EstadisticaTipo> = 
        residuoDao.getEstadisticasPorTipo()
    
    /**
     * Obtiene los residuos más recientes
     */
    suspend fun getResiduosRecientes(): List<Residuo> = residuoDao.getResiduosRecientes()
}
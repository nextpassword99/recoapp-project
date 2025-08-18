package com.example.recoapp.ui.reports

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.recoapp.data.database.RecoAppDatabase
import com.example.recoapp.data.entity.Residuo
import com.example.recoapp.data.repository.ResiduoRepository
import com.example.recoapp.ui.reports.adapter.WasteBreakdownItem
import kotlinx.coroutines.launch
import java.util.*

/**
 * ViewModel para el Fragment de reportes
 * 
 * Gestiona los datos y estadísticas de residuos aplicando filtros
 * para generar reportes detallados para ECOLIM S.A.C.
 */
class ReportsViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository: ResiduoRepository
    
    private val _totalRecords = MutableLiveData<Int>()
    private val _totalWeight = MutableLiveData<Double>()
    private val _breakdown = MutableLiveData<List<WasteBreakdownItem>>()
    private val _isLoading = MutableLiveData<Boolean>()
    
    val totalRecords: LiveData<Int> = _totalRecords
    val totalWeight: LiveData<Double> = _totalWeight
    val breakdown: LiveData<List<WasteBreakdownItem>> = _breakdown
    val isLoading: LiveData<Boolean> = _isLoading
    
    private var currentResiduos: List<Residuo> = emptyList()

    init {
        val residuoDao = RecoAppDatabase.getDatabase(application).residuoDao()
        repository = ResiduoRepository(residuoDao)
    }

    /**
     * Carga todos los datos sin filtros
     */
    fun loadAllData() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                // Obtener todos los residuos
                repository.getAllResiduos().observeForever { residuos ->
                    currentResiduos = residuos ?: emptyList()
                    updateStatistics(currentResiduos)
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                _isLoading.value = false
            }
        }
    }

    /**
     * Aplica filtros a los datos
     */
    fun applyFilters(type: String?, dateFrom: Date?, dateTo: Date?) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                var filteredList = currentResiduos

                // Filtrar por tipo si se especifica
                type?.let { selectedType ->
                    filteredList = filteredList.filter { it.tipo == selectedType }
                }

                // Filtrar por rango de fechas
                if (dateFrom != null && dateTo != null) {
                    // Ajustar las fechas para incluir todo el día
                    val fromCalendar = Calendar.getInstance().apply {
                        time = dateFrom
                        set(Calendar.HOUR_OF_DAY, 0)
                        set(Calendar.MINUTE, 0)
                        set(Calendar.SECOND, 0)
                        set(Calendar.MILLISECOND, 0)
                    }
                    
                    val toCalendar = Calendar.getInstance().apply {
                        time = dateTo
                        set(Calendar.HOUR_OF_DAY, 23)
                        set(Calendar.MINUTE, 59)
                        set(Calendar.SECOND, 59)
                        set(Calendar.MILLISECOND, 999)
                    }

                    filteredList = filteredList.filter { residuo ->
                        val residuoTime = residuo.fecha.time
                        residuoTime >= fromCalendar.timeInMillis && residuoTime <= toCalendar.timeInMillis
                    }
                } else if (dateFrom != null) {
                    // Solo fecha desde
                    val fromCalendar = Calendar.getInstance().apply {
                        time = dateFrom
                        set(Calendar.HOUR_OF_DAY, 0)
                        set(Calendar.MINUTE, 0)
                        set(Calendar.SECOND, 0)
                        set(Calendar.MILLISECOND, 0)
                    }
                    
                    filteredList = filteredList.filter { 
                        it.fecha.time >= fromCalendar.timeInMillis 
                    }
                } else if (dateTo != null) {
                    // Solo fecha hasta
                    val toCalendar = Calendar.getInstance().apply {
                        time = dateTo
                        set(Calendar.HOUR_OF_DAY, 23)
                        set(Calendar.MINUTE, 59)
                        set(Calendar.SECOND, 59)
                        set(Calendar.MILLISECOND, 999)
                    }
                    
                    filteredList = filteredList.filter { 
                        it.fecha.time <= toCalendar.timeInMillis 
                    }
                }

                updateStatistics(filteredList)
                _isLoading.value = false
                
            } catch (e: Exception) {
                _isLoading.value = false
            }
        }
    }

    /**
     * Actualiza las estadísticas basadas en la lista de residuos
     */
    private fun updateStatistics(residuos: List<Residuo>) {
        // Totales generales
        _totalRecords.value = residuos.size
        _totalWeight.value = residuos.sumOf { it.cantidad }

        // Desglose por tipo
        val groupedByType = residuos.groupBy { it.tipo }
        val breakdownItems = groupedByType.map { (tipo, residuosDelTipo) ->
            WasteBreakdownItem(
                tipo = tipo,
                count = residuosDelTipo.size,
                totalWeight = residuosDelTipo.sumOf { it.cantidad }
            )
        }.sortedByDescending { it.totalWeight }

        _breakdown.value = breakdownItems
    }

    /**
     * Obtiene estadísticas avanzadas de la base de datos
     */
    fun getAdvancedStatistics() {
        viewModelScope.launch {
            try {
                val stats = repository.getEstadisticasPorTipo()
                // Aquí se podrían procesar estadísticas más complejas si es necesario
            } catch (e: Exception) {
                // Manejar error
            }
        }
    }
}
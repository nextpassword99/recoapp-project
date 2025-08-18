package com.example.recoapp.ui.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.recoapp.data.database.RecoAppDatabase
import com.example.recoapp.data.entity.Residuo
import com.example.recoapp.data.repository.ResiduoRepository
import kotlinx.coroutines.launch

/**
 * ViewModel para el Fragment de historial de residuos
 * 
 * Gestiona los datos del historial y proporciona funcionalidades
 * de búsqueda y eliminación para ECOLIM S.A.C.
 */
class HistoryViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository: ResiduoRepository
    private val _residuos = MutableLiveData<List<Residuo>>()
    private val _deleteResult = MutableLiveData<Boolean>()
    
    val residuos: LiveData<List<Residuo>> = _residuos
    val deleteResult: LiveData<Boolean> = _deleteResult
    
    private var allResiduos: List<Residuo> = emptyList()

    init {
        val residuoDao = RecoAppDatabase.getDatabase(application).residuoDao()
        repository = ResiduoRepository(residuoDao)
        
        // Observar todos los residuos
        repository.getAllResiduos().observeForever { residuosList ->
            allResiduos = residuosList ?: emptyList()
            _residuos.value = allResiduos
        }
    }

    /**
     * Busca residuos que coincidan con la consulta
     */
    fun searchWaste(query: String) {
        if (query.isEmpty()) {
            _residuos.value = allResiduos
        } else {
            val filteredList = allResiduos.filter { residuo ->
                residuo.tipo.contains(query, ignoreCase = true) ||
                residuo.ubicacion.contains(query, ignoreCase = true) ||
                residuo.comentarios.contains(query, ignoreCase = true)
            }
            _residuos.value = filteredList
        }
    }

    /**
     * Elimina un residuo de la base de datos
     */
    fun deleteWaste(residuo: Residuo) {
        viewModelScope.launch {
            try {
                repository.deleteResiduo(residuo)
                _deleteResult.value = true
            } catch (e: Exception) {
                _deleteResult.value = false
            }
        }
    }

    /**
     * Filtra residuos por tipo
     */
    fun filterByType(tipo: String) {
        if (tipo == "Todos") {
            _residuos.value = allResiduos
        } else {
            val filteredList = allResiduos.filter { it.tipo == tipo }
            _residuos.value = filteredList
        }
    }

    /**
     * Obtiene estadísticas de los residuos
     */
    fun getStatistics(): Pair<Int, Double> {
        val count = allResiduos.size
        val totalWeight = allResiduos.sumOf { it.cantidad }
        return Pair(count, totalWeight)
    }
}
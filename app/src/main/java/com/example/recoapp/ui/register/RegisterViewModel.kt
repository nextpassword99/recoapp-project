package com.example.recoapp.ui.register

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.recoapp.data.database.RecoAppDatabase
import com.example.recoapp.data.entity.Residuo
import com.example.recoapp.data.repository.ResiduoRepository
import kotlinx.coroutines.launch
import java.util.*

/**
 * ViewModel para el Fragment de registro de residuos
 * 
 * Gestiona la lógica de negocio para registrar nuevos residuos
 * en la base de datos local de ECOLIM S.A.C.
 */
class RegisterViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository: ResiduoRepository
    private val _saveResult = MutableLiveData<Boolean>()
    
    val saveResult: LiveData<Boolean> = _saveResult

    init {
        val residuoDao = RecoAppDatabase.getDatabase(application).residuoDao()
        repository = ResiduoRepository(residuoDao)
    }

    /**
     * Guarda un nuevo registro de residuo en la base de datos
     */
    fun saveWaste(
        tipo: String,
        cantidad: Double,
        ubicacion: String,
        fecha: Date,
        comentarios: String
    ) {
        viewModelScope.launch {
            try {
                val residuo = Residuo(
                    tipo = tipo,
                    cantidad = cantidad,
                    ubicacion = ubicacion,
                    fecha = fecha,
                    comentarios = comentarios
                )
                
                val id = repository.insertResiduo(residuo)
                _saveResult.value = id > 0
                
            } catch (e: Exception) {
                _saveResult.value = false
            }
        }
    }
}
package com.example.recoapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * ViewModel para el Fragment de inicio
 * 
 * Gestiona los datos y la lógica de negocio para la pantalla principal
 * de RecoApp - Sistema de Gestión de Residuos ECOLIM S.A.C.
 */
class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Bienvenido a RecoApp\n\nSistema de Gestión de Residuos Sólidos para ECOLIM S.A.C.\n\nDigitaliza el proceso de recolección y registro de residuos de manera eficiente y confiable."
    }
    val text: LiveData<String> = _text
}
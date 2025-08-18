package com.example.recoapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

/**
 * Entidad que representa un residuo sólido en la base de datos
 * 
 * Esta clase define la estructura de los datos de residuos que se almacenan
 * localmente usando Room Database para ECOLIM S.A.C.
 */
@Entity(tableName = "residuos")
data class Residuo(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    val tipo: String,           // Tipo de residuo (orgánico, reciclable, etc.)
    val cantidad: Double,       // Cantidad en kilogramos
    val ubicacion: String,      // Ubicación donde se recolectó
    val fecha: Date,            // Fecha y hora de recolección
    val comentarios: String = ""// Comentarios adicionales
) {
    /**
     * Constructor secundario para facilitar la creación sin ID
     */
    constructor(
        tipo: String,
        cantidad: Double,
        ubicacion: String,
        fecha: Date,
        comentarios: String = ""
    ) : this(0, tipo, cantidad, ubicacion, fecha, comentarios)
}
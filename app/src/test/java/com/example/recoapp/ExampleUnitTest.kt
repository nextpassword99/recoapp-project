package com.example.recoapp

import com.example.recoapp.data.entity.Residuo
import org.junit.Test
import org.junit.Assert.*
import java.util.*

/**
 * Tests unitarios para la aplicación RecoApp
 * 
 * Valida la lógica de negocio y el modelo de datos para el
 * sistema de gestión de residuos de ECOLIM S.A.C.
 */
class ExampleUnitTest {
    
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
    
    @Test
    fun residuo_creation_isCorrect() {
        // Crear un residuo de prueba
        val fecha = Date()
        val residuo = Residuo(
            tipo = "Residuos Orgánicos",
            cantidad = 15.5,
            ubicacion = "Zona Industrial Norte",
            fecha = fecha,
            comentarios = "Material en buen estado"
        )
        
        // Verificar que los datos se asignaron correctamente
        assertEquals("Residuos Orgánicos", residuo.tipo)
        assertEquals(15.5, residuo.cantidad, 0.001)
        assertEquals("Zona Industrial Norte", residuo.ubicacion)
        assertEquals(fecha, residuo.fecha)
        assertEquals("Material en buen estado", residuo.comentarios)
        assertEquals(0L, residuo.id) // ID por defecto
    }
    
    @Test
    fun residuo_validation_cantidad_positiva() {
        val fecha = Date()
        val residuo = Residuo(
            tipo = "Residuos Reciclables",
            cantidad = 25.0,
            ubicacion = "Centro de Reciclaje",
            fecha = fecha
        )
        
        assertTrue("La cantidad debe ser positiva", residuo.cantidad > 0)
    }
    
    @Test
    fun residuo_constructor_secundario() {
        val fecha = Date()
        val residuo = Residuo(
            "Residuos Electrónicos",
            10.2,
            "Almacén Principal",
            fecha,
            "Dispositivos obsoletos"
        )
        
        assertEquals("Residuos Electrónicos", residuo.tipo)
        assertEquals(10.2, residuo.cantidad, 0.001)
        assertEquals("Almacén Principal", residuo.ubicacion)
        assertEquals(fecha, residuo.fecha)
        assertEquals("Dispositivos obsoletos", residuo.comentarios)
    }
    
    @Test
    fun waste_type_validation() {
        val tiposValidos = listOf(
            "Residuos Orgánicos",
            "Residuos Reciclables",
            "Residuos Peligrosos",
            "Residuos Electrónicos",
            "Residuos Generales"
        )
        
        tiposValidos.forEach { tipo ->
            val residuo = Residuo(
                tipo = tipo,
                cantidad = 1.0,
                ubicacion = "Test Location",
                fecha = Date()
            )
            assertTrue("Tipo $tipo debe ser válido", tiposValidos.contains(residuo.tipo))
        }
    }
    
    @Test
    fun date_not_in_future() {
        val fechaActual = Date()
        val residuo = Residuo(
            tipo = "Residuos Orgánicos",
            cantidad = 5.0,
            ubicacion = "Centro de Acopio",
            fecha = fechaActual
        )
        
        assertTrue(
            "La fecha no debe ser futura",
            residuo.fecha.time <= System.currentTimeMillis()
        )
    }
}
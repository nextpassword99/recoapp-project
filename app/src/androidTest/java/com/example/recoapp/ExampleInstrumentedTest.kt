package com.example.recoapp

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.ext.junit.rules.ActivityScenarioRule

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Rule

import org.junit.Assert.*

/**
 * Instrumented test para RecoApp
 * 
 * Tests que se ejecutan en un dispositivo Android o emulador
 * para validar la integración y funcionalidad de la UI.
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)
    
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.recoapp", appContext.packageName)
    }
    
    @Test
    fun app_launches_successfully() {
        // Verificar que la aplicación se inicia correctamente
        activityRule.scenario.onActivity { activity ->
            assertNotNull("MainActivity debe estar inicializada", activity)
            assertTrue("Activity debe estar en estado resumed", activity.hasWindowFocus())
        }
    }
    
    @Test
    fun app_name_resource_exists() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val appName = appContext.getString(R.string.app_name)
        assertEquals("RecoApp - Gestión de Residuos", appName)
    }
    
    @Test
    fun navigation_menu_strings_exist() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        
        // Verificar que existen todas las cadenas de navegación
        val navHome = appContext.getString(R.string.nav_home)
        val navRegister = appContext.getString(R.string.nav_register)
        val navHistory = appContext.getString(R.string.nav_history)
        val navReports = appContext.getString(R.string.nav_reports)
        
        assertEquals("Inicio", navHome)
        assertEquals("Registrar Residuo", navRegister)
        assertEquals("Historial", navHistory)
        assertEquals("Reportes", navReports)
    }
    
    @Test
    fun waste_types_strings_exist() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        
        // Verificar que existen todas las cadenas de tipos de residuo
        assertNotNull(appContext.getString(R.string.organic_waste))
        assertNotNull(appContext.getString(R.string.recyclable_waste))
        assertNotNull(appContext.getString(R.string.hazardous_waste))
        assertNotNull(appContext.getString(R.string.electronic_waste))
        assertNotNull(appContext.getString(R.string.general_waste))
    }
}
package com.example.recoapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.recoapp.data.converter.DateConverter
import com.example.recoapp.data.dao.ResiduoDao
import com.example.recoapp.data.entity.Residuo

/**
 * Base de datos Room principal de la aplicación
 * 
 * Esta clase representa la base de datos SQLite local para el sistema
 * de gestión de residuos sólidos de ECOLIM S.A.C.
 */
@Database(
    entities = [Residuo::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class RecoAppDatabase : RoomDatabase() {
    
    /**
     * DAO para operaciones de residuos
     */
    abstract fun residuoDao(): ResiduoDao
    
    companion object {
        // Singleton para evitar múltiples instancias de la base de datos
        @Volatile
        private var INSTANCE: RecoAppDatabase? = null
        
        /**
         * Obtiene la instancia única de la base de datos
         * 
         * @param context Contexto de la aplicación
         * @return Instancia de RecoAppDatabase
         */
        fun getDatabase(context: Context): RecoAppDatabase {
            // Si ya existe una instancia, la retornamos
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RecoAppDatabase::class.java,
                    "reco_app_database"
                )
                    .fallbackToDestructiveMigration() // Para desarrollo, elimina la DB si cambia el schema
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
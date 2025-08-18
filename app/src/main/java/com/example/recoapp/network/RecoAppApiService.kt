package com.example.recoapp.network

import com.example.recoapp.data.entity.Residuo
import retrofit2.Response
import retrofit2.http.*

/**
 * API Service para sincronización con servidor remoto
 * 
 * Define endpoints RESTful para sincronizar datos de residuos
 * con el servidor central de ECOLIM S.A.C.
 * 
 * Nota: Esta es una simulación - en producción se conectaría
 * a un servidor real con endpoints implementados.
 */
interface RecoAppApiService {
    
    /**
     * Obtiene todos los residuos del servidor
     */
    @GET("residuos")
    suspend fun getAllResiduos(): Response<List<ResiduoDto>>
    
    /**
     * Envía un residuo al servidor
     */
    @POST("residuos")
    suspend fun uploadResiduo(@Body residuo: ResiduoDto): Response<ResiduoDto>
    
    /**
     * Actualiza un residuo en el servidor
     */
    @PUT("residuos/{id}")
    suspend fun updateResiduo(
        @Path("id") id: Long,
        @Body residuo: ResiduoDto
    ): Response<ResiduoDto>
    
    /**
     * Elimina un residuo del servidor
     */
    @DELETE("residuos/{id}")
    suspend fun deleteResiduo(@Path("id") id: Long): Response<Void>
    
    /**
     * Sincroniza residuos modificados después de una fecha
     */
    @GET("residuos/sync")
    suspend fun syncResiduosSince(
        @Query("since") timestamp: Long
    ): Response<SyncResponse>
    
    /**
     * Obtiene estadísticas del servidor
     */
    @GET("estadisticas")
    suspend fun getEstadisticas(): Response<EstadisticasDto>
}

/**
 * DTO (Data Transfer Object) para residuos en API
 */
data class ResiduoDto(
    val id: Long? = null,
    val tipo: String,
    val cantidad: Double,
    val ubicacion: String,
    val fecha: String, // ISO 8601 format
    val comentarios: String,
    val empresaId: String = "ECOLIM",
    val usuarioId: String? = null,
    val sincronizado: Boolean = false
)

/**
 * Respuesta de sincronización
 */
data class SyncResponse(
    val residuos: List<ResiduoDto>,
    val ultimaActualizacion: String,
    val totalSincronizados: Int
)

/**
 * DTO para estadísticas
 */
data class EstadisticasDto(
    val totalResiduos: Int,
    val pesoTotal: Double,
    val residuosPorTipo: Map<String, Double>,
    val fechaUltimaActualizacion: String
)

/**
 * Extensión para convertir Residuo a ResiduoDto
 */
fun Residuo.toDto(): ResiduoDto {
    return ResiduoDto(
        id = if (id == 0L) null else id,
        tipo = tipo,
        cantidad = cantidad,
        ubicacion = ubicacion,
        fecha = java.text.SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            java.util.Locale.getDefault()
        ).format(fecha),
        comentarios = comentarios
    )
}

/**
 * Extensión para convertir ResiduoDto a Residuo
 */
fun ResiduoDto.toEntity(): Residuo {
    val fecha = try {
        java.text.SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            java.util.Locale.getDefault()
        ).parse(fecha) ?: java.util.Date()
    } catch (e: Exception) {
        java.util.Date()
    }
    
    return Residuo(
        id = id ?: 0L,
        tipo = tipo,
        cantidad = cantidad,
        ubicacion = ubicacion,
        fecha = fecha,
        comentarios = comentarios
    )
}
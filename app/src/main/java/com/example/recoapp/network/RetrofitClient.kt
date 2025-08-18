package com.example.recoapp.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Cliente Retrofit para comunicación con API REST
 * 
 * Configura y proporciona instancia de Retrofit para sincronizar
 * datos de residuos con servidor remoto de ECOLIM S.A.C.
 * 
 * Nota: La URL base es simulada - en producción se usaría
 * la URL real del servidor de la empresa.
 */
object RetrofitClient {
    
    // URL base simulada para demostración
    // En producción, reemplazar con URL real del servidor
    private const val BASE_URL = "https://api.ecolim.com/v1/"
    
    // Mock URL para testing con JSONPlaceholder o similar
    private const val MOCK_URL = "https://jsonplaceholder.typicode.com/"
    
    /**
     * Cliente OkHttp configurado con timeouts e interceptores
     */
    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder().apply {
            // Timeouts
            connectTimeout(30, TimeUnit.SECONDS)
            readTimeout(30, TimeUnit.SECONDS)
            writeTimeout(30, TimeUnit.SECONDS)
            
            // Interceptor de logging para desarrollo
            addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            
            // Interceptor para headers comunes
            addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "application/json")
                    .addHeader("User-Agent", "RecoApp-Android/1.0")
                    .addHeader("X-Company-ID", "ECOLIM")
                    .build()
                chain.proceed(request)
            }
        }.build()
    }
    
    /**
     * Instancia de Retrofit configurada
     */
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL) // Cambiar a MOCK_URL para testing
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    /**
     * Servicio API para operaciones de residuos
     */
    val apiService: RecoAppApiService by lazy {
        retrofit.create(RecoAppApiService::class.java)
    }
    
    /**
     * Mock service para testing (puede usar JSONPlaceholder)
     */
    val mockService: RecoAppApiService by lazy {
        Retrofit.Builder()
            .baseUrl(MOCK_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RecoAppApiService::class.java)
    }
}

/**
 * Manejo de estados de la API
 */
sealed class ApiResult<T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error<T>(val message: String, val code: Int? = null) : ApiResult<T>()
    data class Loading<T>(val isLoading: Boolean = true) : ApiResult<T>()
}

/**
 * Extensión para manejo de respuestas Retrofit
 */
suspend fun <T> safeApiCall(apiCall: suspend () -> retrofit2.Response<T>): ApiResult<T> {
    return try {
        val response = apiCall()
        if (response.isSuccessful) {
            response.body()?.let {
                ApiResult.Success(it)
            } ?: ApiResult.Error("Respuesta vacía del servidor")
        } else {
            ApiResult.Error(
                message = "Error ${response.code()}: ${response.message()}",
                code = response.code()
            )
        }
    } catch (e: Exception) {
        ApiResult.Error(
            message = "Error de conexión: ${e.localizedMessage ?: e.message}",
            code = null
        )
    }
}
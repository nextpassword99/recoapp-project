package com.example.recoapp.network

import retrofit2.http.*

data class WasteDto(
    val id: String,
    val type: String?,
    val quantity: Double?,
    val location: String?,
    val date: Long?,
    val comment: String?,
    val deleted: Boolean = false,
    val modifiedAt: Long
)

data class PullResponse(val items: List<WasteDto>)
data class PushRequest(val items: List<WasteDto>)
data class PushResponse(val results: List<ResultDto>)
data class ResultDto(val id: String?, val status: String, val reason: String? = null)

interface SyncApi {
    @GET("api/sync/wastes")
    suspend fun pull(@Query("since") since: Long): PullResponse

    @POST("api/sync/wastes")
    suspend fun push(@Body body: PushRequest): PushResponse
}

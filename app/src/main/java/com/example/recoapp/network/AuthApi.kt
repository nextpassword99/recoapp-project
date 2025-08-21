package com.example.recoapp.network

import retrofit2.http.Body
import retrofit2.http.POST

// DTOs
data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class UserDto(
    val id: Int,
    val name: String,
    val email: String
)

data class AuthResponse(
    val token: String,
    val user: UserDto
)

interface AuthApi {
    @POST("api/auth/register")
    suspend fun register(@Body body: RegisterRequest): AuthResponse

    @POST("api/auth/login")
    suspend fun login(@Body body: LoginRequest): AuthResponse
}

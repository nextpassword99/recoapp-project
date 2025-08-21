package com.example.recoapp.network

class AuthRepository {
    private val api: AuthApi = ApiService.retrofit.create(AuthApi::class.java)

    suspend fun login(email: String, password: String) =
        api.login(LoginRequest(email, password))

    suspend fun register(name: String, email: String, password: String) =
        api.register(RegisterRequest(name, email, password))
}

package com.example.kou_proje


import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Response

interface ApiService {
    @POST("api/Users/Registration")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<String>  // API "User registered successfully" döndüğü için Response<String> kullanıyoruz

    @POST("api/Users/Login")
    suspend fun login(@Body loginDto: LoginDto): Response<LoginResponse>
}
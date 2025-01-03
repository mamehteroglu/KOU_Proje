package com.example.kou_proje

import android.content.Intent



data class RegisterRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String
)

data class LoginDto(
    val email: String,
    val password: String
)

data class LoginResponse(
    val token: String,
    val user: User
)

data class User(
    val userId: Int,
    val firstName: String,
    val lastName: String,
    val email: String
)
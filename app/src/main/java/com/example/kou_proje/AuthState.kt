package com.example.kou_proje

import android.content.Intent

sealed class AuthState {
    data object Initial : AuthState()
    data object Loading : AuthState()
    data class SignInStarted(val intent: Intent) : AuthState()
    data class Success(val userData: UserData) : AuthState()
    data class Error(val message: String) : AuthState()
    data object SignedOut : AuthState()
}

data class UserData(
    val id: String,
    val name: String,
    val email: String,
    val photoUrl: String?
)
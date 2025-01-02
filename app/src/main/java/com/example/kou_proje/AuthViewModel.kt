package com.example.kou_proje

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class AuthViewModel(
    private val apiService: ApiService
) : ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Initial)
    val authState = _authState.asStateFlow()

    fun startSignIn(intent: Intent) {
        _authState.value = AuthState.SignInStarted(intent)
    }

    fun register(firstName: String, lastName: String, email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val response = apiService.register(
                    RegisterRequest(
                        firstName = firstName,
                        lastName = lastName,
                        email = email,
                        password = password
                    )
                )
                if (response.isSuccessful) {
                    _authState.value = AuthState.Success("Kayıt başarılı")
                } else {
                    val errorBody = response.errorBody()?.string()
                    _authState.value = AuthState.Error(errorBody ?: "Kayıt başarısız")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _authState.value = AuthState.Error(e.message ?: "Bir hata oluştu")
            }
        }
    }
    class Factory(private val apiService: ApiService) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AuthViewModel(apiService) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val response = apiService.login(LoginDto(email, password))
                if (response.isSuccessful) {
                    _authState.value = AuthState.Success(response.body()?.token ?: "")
                } else {
                    _authState.value = AuthState.Error("Giriş başarısız")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Bir hata oluştu")
            }
        }
    }
}

sealed class AuthState {
    object Initial : AuthState()
    object Loading : AuthState()
    data class SignInStarted(val intent: Intent) : AuthState()
    object SignedOut : AuthState()
    data class Success(val token: String) : AuthState()
    data class Error(val message: String) : AuthState()
}
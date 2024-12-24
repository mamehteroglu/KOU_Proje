package com.example.kou_proje

import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class GoogleAuthViewModel(application: Application) : AndroidViewModel(application) {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Initial)
    val authState = _authState.asStateFlow()

    init {
        Log.d("GoogleAuth", "ViewModel initialized")
        checkCurrentUser()
    }

    private fun checkCurrentUser() {
        val account = GoogleSignIn.getLastSignedInAccount(getApplication())
        Log.d("GoogleAuth", "Last signed in account: ${account?.email}")
        account?.let {
            _authState.value = AuthState.Success(
                UserData(
                    id = it.id ?: "",
                    name = it.displayName ?: "",
                    email = it.email ?: "",
                    photoUrl = it.photoUrl?.toString()
                )
            )
        }
    }

    private val googleAuthClient by lazy {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestId()
            .requestProfile()
            .requestIdToken(getApplication<Application>().getString(R.string.web_client_id))
            .build()

        GoogleSignIn.getClient(getApplication(), gso).also {
            Log.d("GoogleAuth", "GoogleSignInClient created")
        }
    }

    fun signIn() {
        viewModelScope.launch {
            try {
                Log.d("GoogleAuth", "Starting sign in process")
                _authState.value = AuthState.Loading
                val signInIntent = googleAuthClient.signInIntent.also {
                    Log.d("GoogleAuth", "Sign in intent created")
                }
                _authState.value = AuthState.SignInStarted(signInIntent)
            } catch (e: Exception) {
                Log.e("GoogleAuth", "Error creating sign in intent", e)
                handleSignInError("Giriş başlatılamadı: ${e.message}")
            }
        }
    }

    fun handleSignInResult(result: Intent?) {
        Log.d("GoogleAuth", "Handling sign in result")

        if (result == null) {
            Log.e("GoogleAuth", "Sign in result intent is null")
            handleSignInError("Giriş sonucu alınamadı")
            return
        }

        try {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result)
            val account = task.getResult(ApiException::class.java)

            Log.d("GoogleAuth", "Successfully signed in as: ${account.email}")

            _authState.value = AuthState.Success(
                UserData(
                    id = account.id ?: "",
                    name = account.displayName ?: "",
                    email = account.email ?: "",
                    photoUrl = account.photoUrl?.toString()
                )
            )
        } catch (e: ApiException) {
            Log.e("GoogleAuth", "Sign in failed with status code: ${e.statusCode}", e)
            val errorMessage = when (e.statusCode) {
                12501 -> "Giriş iptal edildi"
                7 -> "Ağ bağlantısı hatası"
                else -> "Giriş başarısız oldu (Kod: ${e.statusCode})"
            }
            handleSignInError(errorMessage)
        } catch (e: Exception) {
            Log.e("GoogleAuth", "Unexpected error during sign in", e)
            handleSignInError("Beklenmeyen bir hata oluştu: ${e.message}")
        }
    }

    fun handleSignInError(message: String) {
        Log.e("GoogleAuth", "Sign in error: $message")
        _authState.value = AuthState.Error(message)
    }

    fun signOut() {
        viewModelScope.launch {
            try {
                Log.d("GoogleAuth", "Starting sign out process")
                googleAuthClient.signOut().await()
                _authState.value = AuthState.SignedOut
                Log.d("GoogleAuth", "Sign out successful")
            } catch (e: Exception) {
                Log.e("GoogleAuth", "Sign out failed", e)
                _authState.value = AuthState.Error("Çıkış yapılırken hata oluştu")
            }
        }
    }
}
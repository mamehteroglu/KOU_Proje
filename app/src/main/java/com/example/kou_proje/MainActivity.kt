package com.example.kou_proje

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize

class MainActivity : ComponentActivity() {
    private lateinit var authViewModel: GoogleAuthViewModel

    private val launcher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            Log.d("GoogleAuth", "Sign in result received")
            authViewModel.handleSignInResult(result.data)
        } else {
            Log.e("GoogleAuth", "Sign in failed: ${result.resultCode}")
            authViewModel.handleSignInError("Giriş başarısız oldu")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authViewModel = GoogleAuthViewModel(application)

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    AuthContent(
                        viewModel = authViewModel,
                        onSignInStarted = { intent ->
                            launcher.launch(intent)
                        }
                    )
                }
            }
        }
    }
}
package com.example.kou_proje

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.LaunchedEffect

class LoginActivity : ComponentActivity() {
    private lateinit var viewModel: GoogleAuthViewModel

    private val launcher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            Log.d("GoogleAuth", "Sign in result received")
            viewModel.handleSignInResult(result.data)
        } else {
            Log.e("GoogleAuth", "Sign in failed: ${result.resultCode}")
            viewModel.handleSignInError("Giriş başarısız oldu")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = GoogleAuthViewModel(application)

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AuthContent(
                        viewModel = viewModel,
                        onSignInStarted = { intent ->
                            launcher.launch(intent)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun AuthContent(
    viewModel: GoogleAuthViewModel,
    onSignInStarted: (android.content.Intent) -> Unit
) {
    val authState by viewModel.authState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (val state = authState) {
            AuthState.Initial -> {
                Button(onClick = { viewModel.signIn() }) {
                    Text("Google ile Giriş Yap")
                }
            }
            AuthState.Loading -> {
                CircularProgressIndicator()
            }
            is AuthState.Success -> {
                UserProfileCard(
                    userData = state.userData,
                    onSignOutClick = { viewModel.signOut() }  // ViewModel'den signOut çağrılıyor
                )
            }
            is AuthState.Error -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error
                    )
                    Button(onClick = { viewModel.signIn() }) {
                        Text("Tekrar Dene")
                    }
                }
            }
            AuthState.SignedOut -> {
                Button(onClick = { viewModel.signIn() }) {
                    Text("Google ile Giriş Yap")
                }
            }
            is AuthState.SignInStarted -> {
                LaunchedEffect(Unit) {
                    onSignInStarted(state.intent)
                }
            }
        }
    }
}

@Composable
private fun UserProfileCard(
    userData: UserData,
    onSignOutClick: () -> Unit  // SignOut callback parametresi eklendi
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AsyncImage(
                model = userData.photoUrl,
                contentDescription = "Profil fotoğrafı",
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
            )

            Text(
                text = userData.name,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = userData.email,
                style = MaterialTheme.typography.bodyMedium
            )

            Button(
                onClick = onSignOutClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Çıkış Yap")
            }
        }
    }
}
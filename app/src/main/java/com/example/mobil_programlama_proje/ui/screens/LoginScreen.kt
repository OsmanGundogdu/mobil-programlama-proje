package com.example.mobil_programlama_proje.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mobil_programlama_proje.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit // Yeni eklenen navigasyon parametresi
) {
    // Sadece email tutuyoruz
    var email by remember { mutableStateOf("") }

    val loginResult by viewModel.loginResult.observeAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Tekrar Hoş Geldiniz", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(32.dp))

        // Sadece Email Girişi
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email Adresi") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Giriş Butonu
        Button(
            onClick = { viewModel.login(email) }, // Sadece email gönderiyoruz
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium
        ) {
            Text("Giriş Yap")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Kayıt sayfasına yönlendirme
        TextButton(onClick = onNavigateToRegister) {
            Text("Hesabın yok mu? Hemen Kaydol")
        }

        // Giriş başarılı olduğunda yapılacak işlem
        LaunchedEffect(loginResult) {
            if (loginResult == true) {
                viewModel.resetStates()
                onLoginSuccess()
            }
        }
    }
}
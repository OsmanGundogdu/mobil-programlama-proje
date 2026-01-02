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
fun RegisterScreen(
    viewModel: AuthViewModel,
    onRegisterSuccess: () -> Unit, // Kayıt başarılı olunca ne yapacağını (örn: Login'e dön) buraya yazacağız
    onNavigateToLogin: () -> Unit  // "Zaten hesabım var"a tıklarsa
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    val registerResult by viewModel.registerResult.observeAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Yeni Hesap Oluştur", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(32.dp))

        // İsim Girişi
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Adınız Soyadınız") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Email Girişi
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email Adresi") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Kayıt Ol Butonu
        Button(
            onClick = {
                if (name.isNotBlank() && email.isNotBlank()) {
                    viewModel.register(name, email)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Kaydol")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Zaten hesabın var mı butonu
        TextButton(onClick = onNavigateToLogin) {
            Text("Zaten bir hesabın var mı? Giriş Yap")
        }

        // Kayıt başarılı ise tetiklenir
        LaunchedEffect(registerResult) {
            if (registerResult == true) {
                viewModel.resetStates() // State'i temizle ki tekrar tetiklenmesin
                onRegisterSuccess()
            }
        }
    }
}
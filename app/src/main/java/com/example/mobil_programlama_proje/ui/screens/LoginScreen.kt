package com.example.mobil_programlama_proje.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.mobil_programlama_proje.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onLoginSuccess: () -> Unit
) {
    // Kullanıcının yazdıklarını tutan geçici hafıza
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // ViewModel'deki sonucu dinliyoruz
    val loginResult by viewModel.loginResult.observeAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Hoş Geldiniz", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(32.dp))

        // Email Girişi
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email Adresi") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Şifre Girişi
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Şifre") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Giriş Butonu
        Button(
            onClick = { viewModel.login(email, password) },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium
        ) {
            Text("Giriş Yap")
        }

        // Giriş başarılı olduğunda yapılacak işlem
        LaunchedEffect(loginResult) {
            if (loginResult == true) {
                onLoginSuccess()
            }
        }
    }
}
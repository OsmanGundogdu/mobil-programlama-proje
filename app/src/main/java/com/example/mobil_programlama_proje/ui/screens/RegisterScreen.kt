package com.example.mobil_programlama_proje.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.mobil_programlama_proje.viewmodel.AuthViewModel

@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    val registerResult by viewModel.registerResult.observeAsState()
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Yeni Hesap Oluştur", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Adınız Soyadınız") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email Adresi") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (name.isNotBlank() && email.isNotBlank()) {
                    viewModel.register(name, email)
                } else {
                    Toast.makeText(context, "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Kaydol")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onNavigateToLogin) {
            Text("Zaten bir hesabın var mı? Giriş Yap")
        }

        // --- SONUÇ DİNLEME KISMI ---
        LaunchedEffect(registerResult) {
            if (registerResult == true) {
                Toast.makeText(context, "Kayıt Başarılı! Şimdi giriş yapabilirsiniz.", Toast.LENGTH_LONG).show()
                viewModel.resetStates()
                onRegisterSuccess() // Giriş ekranına geri atar
            } else if (registerResult == false) {
                Toast.makeText(context, "Kayıt başarısız oldu.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
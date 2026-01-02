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
fun LoginScreen(
    viewModel: AuthViewModel,
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    val loginResult by viewModel.loginResult.observeAsState()
    val context = LocalContext.current // Toast mesajı için gerekli

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Giriş Yap", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email Adresi") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (email.isNotBlank()) {
                    viewModel.login(email)
                } else {
                    Toast.makeText(context, "Lütfen email girin", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Giriş Yap")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onNavigateToRegister) {
            Text("Hesabın yok mu? Hemen Kaydol")
        }

        // --- SONUÇ DİNLEME KISMI ---
        LaunchedEffect(loginResult) {
            if (loginResult == true) {
                Toast.makeText(context, "Giriş Başarılı!", Toast.LENGTH_SHORT).show()
                viewModel.resetStates() // Tekrar giriş yapılabilir olsun diye
                onLoginSuccess()
            } else if (loginResult == false) {
                // EĞER KULLANICI BULUNAMAZSA BURASI ÇALIŞACAK
                Toast.makeText(context, "Kullanıcı bulunamadı! Lütfen önce kayıt olun.", Toast.LENGTH_LONG).show()
                viewModel.resetStates() // Hatayı sıfırla ki tekrar basabilsin
            }
        }
    }
}
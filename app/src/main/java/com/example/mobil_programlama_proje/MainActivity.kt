package com.example.mobil_programlama_proje

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.mobil_programlama_proje.navigation.NoteAppNavigation
import com.example.mobil_programlama_proje.ui.theme.Mobil_programlama_projeTheme
import java.util.concurrent.TimeUnit

/**
 * Main activity that hosts the Compose navigation graph.
 * Sets up the application theme and navigation structure.
 */
class MainActivity : ComponentActivity() {

    private lateinit var connectivityManager: ConnectivityManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // =====================
        // BACKUP WORKER
        // =====================
        val backupRequest =
            PeriodicWorkRequest.Builder(
                com.example.mobil_programlama_proje.worker.BackupWorker::class.java,
                15,
                TimeUnit.MINUTES
            ).build()

        WorkManager.getInstance(applicationContext)
            .enqueueUniquePeriodicWork(
                "backup_worker",
                ExistingPeriodicWorkPolicy.UPDATE,
                backupRequest
            )

        // =====================
        // CONNECTIVITY
        // =====================
        connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        enableEdgeToEdge()
        setContent {
            Mobil_programlama_projeTheme {

                // INTERNET STATE
                val isConnected = remember { mutableStateOf(isInternetAvailable()) }

                // NETWORK CALLBACK
                val networkCallback = object : ConnectivityManager.NetworkCallback() {
                    override fun onAvailable(network: Network) {
                        isConnected.value = true
                    }

                    override fun onLost(network: Network) {
                        isConnected.value = false
                    }
                }

                val request = NetworkRequest.Builder()
                    .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    .build()

                connectivityManager.registerNetworkCallback(request, networkCallback)

                val navController = rememberNavController()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NoteAppNavigation(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding),
                        isConnected = isConnected.value // 👈 NAVIGATION’A AKTARILDI
                    )
                }
            }
        }
    }

    // =====================
    // INTERNET CHECK
    // =====================
    private fun isInternetAvailable(): Boolean {
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities =
            connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    androidx.compose.material3.Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Mobil_programlama_projeTheme {
        Greeting("Android")
    }
}

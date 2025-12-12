package com.example.mobil_programlama_proje

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.navigation.compose.rememberNavController
import com.example.mobil_programlama_proje.navigation.NoteAppNavigation
import com.example.mobil_programlama_proje.ui.theme.Mobil_programlama_projeTheme
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.ExistingWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkRequest
import java.util.concurrent.TimeUnit


/**
 * Main activity that hosts the Compose navigation graph.
 * Sets up the application theme and navigation structure.
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

        enableEdgeToEdge()
        setContent {
            Mobil_programlama_projeTheme {
                val navController = rememberNavController()
                
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NoteAppNavigation(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
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
package com.example.mobil_programlama_proje

import android.Manifest
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.mobil_programlama_proje.navigation.NoteAppNavigation
import com.example.mobil_programlama_proje.sensor.LocationHelper
import com.example.mobil_programlama_proje.sensor.ShakeDetector
import com.example.mobil_programlama_proje.ui.theme.Mobil_programlama_projeTheme
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {

    private lateinit var sensorManager: SensorManager
    private lateinit var shakeDetector: ShakeDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // LOCATION PERMISSION
        checkLocationPermission()

        // BACKUP WORKER
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

        // SENSOR (SHAKE)
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        shakeDetector = ShakeDetector {
            Toast.makeText(this, "Telefon sallandi!", Toast.LENGTH_SHORT).show()
        }

        sensorManager.registerListener(
            shakeDetector,
            accelerometer,
            SensorManager.SENSOR_DELAY_NORMAL
        )

        // COMPOSE
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

    // ==========================
    // LOCATION PERMISSION
    // ==========================
    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                100
            )
        } else {
            getLocationSafely()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 100 &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            getLocationSafely()
        }
    }

    private fun getLocationSafely() {
        val locationHelper = LocationHelper(this)
        val location = locationHelper.getLastLocation()

        location?.let {
            Toast.makeText(
                this,
                "Lat: ${it.latitude}, Lon: ${it.longitude}",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    // ==========================
    // SENSOR LIFECYCLE
    // ==========================
    override fun onResume() {
        super.onResume()
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager.registerListener(
            shakeDetector,
            accelerometer,
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(shakeDetector)
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(text = "Hello $name!", modifier = modifier)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Mobil_programlama_projeTheme {
        Greeting("Android")
    }
}

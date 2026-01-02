//package com.example.mobil_programlama_proje.worker
//
//import android.content.Context
//import android.util.Log
//import androidx.work.CoroutineWorker
//import androidx.work.WorkerParameters
//import com.example.mobil_programlama_proje.database.AppDatabase
//import com.google.gson.Gson // JSON dönüşümü için
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.withContext
//
//class BackupWorker(
//    context: Context,
//    workerParams: WorkerParameters
//) : CoroutineWorker(context, workerParams) {
//
//    override suspend fun doWork(): Result {
//        return withContext(Dispatchers.IO) {
//            try {
//                Log.i("BackupWorker", "🔄 Yedekleme işlemi başlatılıyor...")
//
//                // 1. Veritabanından notları çek
//                val database = AppDatabase.getInstance(applicationContext)
//                val noteList = database.noteDao().getAllNotesSync()
//
//                if (noteList.isEmpty()) {
//                    Log.i("BackupWorker", "⚠️ Yedeklenecek not bulunamadı.")
//                    return@withContext Result.success()
//                }
//
//                // 2. Notları JSON formatına çevir (Sunucuya gönderilecek paket)
//                val gson = Gson()
//                val jsonPayload = gson.toJson(noteList)
//
//                Log.d("BackupWorker", "📦 Paket Hazırlandı (JSON): $jsonPayload")
//
//                // 3. Sunucuya Gönderme Simülasyonu (API Çağrısı)
//                val isSuccess = fakeApiCall(jsonPayload)
//
//                if (isSuccess) {
//                    Log.i("BackupWorker", "✅ BAŞARILI: ${noteList.size} adet not buluta yedeklendi.")
//                    Result.success()
//                } else {
//                    Log.e("BackupWorker", "❌ Sunucu hatası! Daha sonra tekrar denenecek.")
//                    Result.retry() // WorkManager bunu sonra tekrar dener
//                }
//
//            } catch (e: Exception) {
//                Log.e("BackupWorker", "❌ Kritik Hata: ${e.localizedMessage}")
//                Result.failure()
//            }
//        }
//    }
//
//    // Gerçek bir API çağrısını taklit eden fonksiyon
//    private suspend fun fakeApiCall(data: String): Boolean {
//        // Sanki internete yükleniyormuş gibi 2 saniye bekle
//        delay(2000)
//
//        // %100 başarılı kabul ediyoruz (Buraya gerçek Retrofit kodu gelecek ileride)
//        return true
//    }
//}

package com.example.mobil_programlama_proje.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.mobil_programlama_proje.database.AppDatabase
import com.google.gson.Gson // JSON çevirici
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class BackupWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try {
                Log.i("BackupWorker", "🚀 Yedekleme Worker'ı Çalıştı!")

                // 1. Veritabanından notları çek
                val database = AppDatabase.getInstance(applicationContext)
                val noteList = database.noteDao().getAllNotesSync()

                if (noteList.isEmpty()) {
                    Log.i("BackupWorker", "⚠️ Yedeklenecek not bulunamadı.")
                    return@withContext Result.success()
                }

                // 2. Notları JSON formatına çevir (Sunucuya gidecek paket)
                val gson = Gson()
                val jsonPayload = gson.toJson(noteList)

                // Logcat'te bu JSON'u göreceksin
                Log.d("BackupWorker", "📦 Paket Hazırlandı (JSON): $jsonPayload")

                // 3. Sunucuya Gönderme Simülasyonu
                val isSuccess = fakeApiCall(jsonPayload)

                if (isSuccess) {
                    Log.i("BackupWorker", "✅ BAŞARILI: ${noteList.size} adet not buluta yedeklendi.")
                    Result.success()
                } else {
                    Log.e("BackupWorker", "❌ Sunucu hatası! Daha sonra tekrar denenecek.")
                    Result.retry() // Başarısız olursa sonra tekrar dener
                }

            } catch (e: Exception) {
                Log.e("BackupWorker", "❌ Kritik Hata: ${e.localizedMessage}")
                Result.failure()
            }
        }
    }

    // Gerçek API olmadığı için "mış gibi" yapıyoruz
    private suspend fun fakeApiCall(data: String): Boolean {
        // Yükleniyor efekti (2 saniye bekle)
        delay(2000)
        return true
    }
}
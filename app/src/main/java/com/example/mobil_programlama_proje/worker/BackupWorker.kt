package com.example.mobil_programlama_proje.worker;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class BackupWorker extends Worker {

    public BackupWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        // TODO: Room Database üzerinden notları çek
        // TODO: API varsa server’a yedek gönder
        // Şimdilik test amaçlı log basıyoruz. sonra değişecek bura

        System.out.println("Worker Çalıştı! Notlar yedeklendi.");

        return Result.success();
    }
}
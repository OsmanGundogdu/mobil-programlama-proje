package com.example.mobil_programlama_proje.connectivity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class ConnectivityReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {

        val helper = ConnectivityHelper(context)

        if (helper.isInternetAvailable()) {
            Toast.makeText(
                context,
                "Internet baglantisi aktif (${helper.connectionType()})",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(
                context,
                "Internet yok. Offline not kaydediliyor.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}

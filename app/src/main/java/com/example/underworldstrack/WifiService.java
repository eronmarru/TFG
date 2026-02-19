package com.example.underworldstrack;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class WifiService extends Service {

    private static final String TAG = "WifiService";
    private boolean enEjecucion = false;
    private boolean wifiActivo = false;
    private Tester tester;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "Servicio WifiService creado!");
        tester = new Tester();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(1002, createNotification("Monitor WiFi iniciado"));

        if (!enEjecucion) {
            enEjecucion = true;
            tester.start();
            Log.i(TAG, "Servicio WifiService arrancado!");
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        enEjecucion = false;
        if (tester != null) tester.interrupt();
        Log.i(TAG, "Servicio WifiService destruido!");
        super.onDestroy();
    }

    private class Tester extends Thread {
        @Override
        public void run() {
            while (enEjecucion) {
                try {
                    boolean nuevoEstado = estaConectadoAWifi();
                    if (wifiActivo != nuevoEstado) {
                        wifiActivo = nuevoEstado;
                        String estado = wifiActivo ? "WiFi ACTIVADO" : "WiFi DESACTIVADO";

                        Log.i(TAG, estado);

                        stopForeground(false);
                        startForeground(1002, createNotification(estado));
                    }

                    Thread.sleep(3000);

                } catch (InterruptedException e) {
                    enEjecucion = false;
                    break;
                }
            }
        }

        private boolean estaConectadoAWifi() {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

            if (cm != null) {
                Network network = cm.getActiveNetwork();
                if (network != null) {
                    NetworkCapabilities caps = cm.getNetworkCapabilities(network);
                    return caps != null && caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI);
                }
            }

            return false;
        }
    }

    private Notification createNotification(String texto) {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_IMMUTABLE
        );

        return new NotificationCompat.Builder(this, MainActivity.CHANNEL_ID)
            .setContentTitle("WiFi Monitor")
            .setContentText(texto)
            .setSmallIcon(android.R.drawable.ic_menu_info_details)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build();
    }
}

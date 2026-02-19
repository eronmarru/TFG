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

    private static final String TAG = "WifiService";  // Etiqueta para logs
    private boolean enEjecucion = false;              // Controla si el hilo principal está corriendo
    private boolean wifiActivo = false;               // Estado actual del WiFi (true=conectado, false=desconectado)
    private Tester tester;                            // Hilo que monitorea el WiFi cada 3 segundos

    @Override
    public IBinder onBind(Intent intent) {
        return null; // No es un servicio ligado (no se conecta a otras apps)
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "Servicio WifiService creado!");

        // Solo inicializamos el hilo, NO lo arrancamos ni hacemos foreground aquí
        tester = new Tester();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // OBLIGATORIO para Android 14+: Servicios foreground deben llamarse en onStartCommand
        // ID 1002 es único para esta notificación del servicio
        startForeground(1002, createNotification("Monitor WiFi iniciado"));


        if (!enEjecucion) {
            enEjecucion = true;      // Marca que el servicio está activo
            tester.start();          // Arranca el hilo de monitoreo
            Log.i(TAG, "Servicio WifiService arrancado!");
        }

        return START_STICKY;         // Reinicia automáticamente si el sistema lo mata
    }

    @Override
    public void onDestroy() {
        enEjecucion = false;         // Detiene el bucle del hilo
        if (tester != null) tester.interrupt();  // Interrumpe el hilo limpiamente
        Log.i(TAG, "Servicio WifiService destruido!");
        super.onDestroy();
    }

    // Clase interna que hace el trabajo pesado en background
    private class Tester extends Thread {
        @Override
        public void run() {
            // Bucle infinito mientras el servicio esté activo
            while (enEjecucion) {
                try {
                    boolean nuevoEstado = estaConectadoAWifi();  // Chequea estado actual del WiFi

                    // Solo actúa si cambió el estado (optimización)
                    if (wifiActivo != nuevoEstado) {
                        wifiActivo = nuevoEstado;                // Actualiza estado global
                        String estado = wifiActivo ? "WiFi ACTIVADO" : "WiFi DESACTIVADO";

                        Log.i(TAG, estado);

                        // Actualiza la notificación SIN destruir el servicio (false)
                        stopForeground(false);
                        startForeground(1002, createNotification(estado));
                    }

                    Thread.sleep(3000);  // Espera 3 segundos antes del próximo chequeo

                } catch (InterruptedException e) {
                    // El servicio se está destruyendo, salimos limpiamente
                    enEjecucion = false;
                    break;
                }
            }
        }

        // Método moderno (API 23+) para detectar conexión WiFi activa
        private boolean estaConectadoAWifi() {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

            if (cm != null) {
                Network network = cm.getActiveNetwork();  // Red activa actual
                if (network != null) {
                    NetworkCapabilities caps = cm.getNetworkCapabilities(network);
                    // Verifica si la red activa usa transporte WiFi
                    return caps != null && caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI);
                }
            }

            return false;  // No hay conexión WiFi
        }
    }

    // Crea la notificación persistente que mantiene vivo el servicio
    private Notification createNotification(String texto) {
        // Al tocar la notificación, abre MainActivity
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_IMMUTABLE  // Inmutable para Android 12+
        );

        return new NotificationCompat.Builder(this, MainActivity.CHANNEL_ID)  // Canal creado en MainActivity
            .setContentTitle("WiFi Monitor")                                    // Título fijo
            .setContentText(texto)                                              // Texto dinámico (ACTIVADO/DESACTIVADO)
            .setSmallIcon(android.R.drawable.ic_menu_info_details)              // Icono pequeño obligatorio
            .setContentIntent(pendingIntent)                                    // Acción al tocar
            .setOngoing(true)                                                   // No se puede deslizar para quitar
            .build();
    }
}

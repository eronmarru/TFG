package com.example.underworldstrack;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlarmReciver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "underworld_channel";  // ID del canal creado en MainActivity
    private static final int NOTIFICATION_ID = 999;                  // ID único para esta notificación de alarma

    // Método que se ejecuta automáticamente cuando llega la alarma programada
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("AlarmReciver", "¡ALARMA RECIBIDA!");  // Log para debuggear que la alarma funciona

        // Crea notificación recordatorio del juego diario
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.icon)           // Icono pequeño obligatorio
                        .setContentTitle("Underworlds Tracker")  // Título de la notificación
                        .setContentText("Son las 15:30: ¡toca jugar para no oxidarte!")  // Mensaje recordatorio
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)  // Prioridad media
                        .setAutoCancel(true);                    // Se quita sola al tocarla

        // Envía la notificación al sistema
        NotificationManagerCompat manager = NotificationManagerCompat.from(context);
        manager.notify(NOTIFICATION_ID, builder.build());

        Log.d("AlarmReciver", "Notificación enviada OK");  // Confirma que se envió
    }
}

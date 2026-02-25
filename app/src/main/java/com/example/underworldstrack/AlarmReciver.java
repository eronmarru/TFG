package com.example.underworldstrack;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

/**
 * BroadcastReceiver que maneja las alarmas programadas.
 * Envía una notificación al usuario cuando se recibe la alarma (ej. recordatorio diario).
 */
public class AlarmReciver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "underworld_channel";
    private static final int NOTIFICATION_ID = 999;

    /**
     * Método llamado cuando se recibe el broadcast de la alarma.
     * Crea y muestra una notificación push.
     * @param context Contexto de la aplicación
     * @param intent Intent recibido
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("AlarmReciver", "¡ALARMA RECIBIDA!");

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.icon)
                        .setContentTitle("Underworlds Tracker")
                        .setContentText("Son las 15:30: ¡toca jugar para no oxidarte!")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setAutoCancel(true);

        NotificationManagerCompat manager = NotificationManagerCompat.from(context);
        manager.notify(NOTIFICATION_ID, builder.build());

        Log.d("AlarmReciver", "Notificación enviada OK");
    }
}

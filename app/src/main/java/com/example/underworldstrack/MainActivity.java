package com.example.underworldstrack;

import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.Calendar;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    private static final int REQ_NOTIF = 1001;           // Código único para identificar respuesta de permiso notificaciones
    public static final String CHANNEL_ID = "underworld_channel";  // ID del canal usado por WifiService
    private CountDownTimer contador;                     // Temporizador que muestra toast cada minuto

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);                         // Activa modo edge-to-edge (pantalla completa)
        setContentView(R.layout.activity_main);

        // Inicia temporizador que avisa cada minuto
        iniciarContadorToast();

        // Configuración esencial para notificaciones
        createNotificationChannel();                     // Crea canal para Android 8+
        checkAndRequestNotificationPermission();         // Pide permiso POST_NOTIFICATIONS en Android 13+

        // Programa alarma diaria para juego a las 16:50
        scheduleDailyGameAlarm();

        // Configuración de padding para barras del sistema (edge-to-edge)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Botones de navegación del juego
        Button btnEntrar = findViewById(R.id.btn_entrar);
        btnEntrar.setOnClickListener(v -> startActivity(new Intent(this, Board.class)));  // Abre Board activity

        Button btn_en_ex = findViewById(R.id.btn_en_ex);
        btn_en_ex.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://criticoyculo.com/"))));  // Abre URL en navegador

        // Botones de control del servicio WiFi
        Button btnStartWifi = findViewById(R.id.btn_start_wifi);
        btnStartWifi.setOnClickListener(v -> {
            Intent serviceIntent = new Intent(this, WifiService.class);
            // Usa startForegroundService en Android 8+, startService en versiones anteriores
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(serviceIntent);
            } else {
                startService(serviceIntent);
            }
        });

        Button btnStopWifi = findViewById(R.id.btn_stop_wifi);
        btnStopWifi.setOnClickListener(v -> stopService(new Intent(this, WifiService.class)));  // Detiene el servicio WiFi
    }

    // Temporizador que muestra toast cada 60 segundos (reinicia automáticamente)
    private void iniciarContadorToast() {
        if (contador != null) {
            contador.cancel();  // Cancela temporizador anterior si existe
        }

        contador = new CountDownTimer(300000, 1000) {     // 60s total, tick cada 1s
            @Override
            public void onTick(long millisUntilFinished) {
                Log.d("Tiempo", "Ha pasado un segundo"); //mensaje en el log, para comprobar que funciona bien
            }

            @Override
            public void onFinish() {
                // Muestra toast cuando terminan los 60s
                Toast.makeText(MainActivity.this,
                        "Ya llevas 5 minutos, ¿encuentras lo que buscas?",
                        Toast.LENGTH_LONG).show();
                iniciarContadorToast();  // Reinicia el ciclo
            }
        };
        contador.start();
    }

    // Programa alarma diaria a las 16:50 que dispara AlarmReciver
    private void scheduleDailyGameAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReciver.class);  // Broadcast receiver para el juego diario
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);  // Actualiza si ya existe

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 15);          // 15:30 todos los días
        calendar.set(Calendar.MINUTE, 30);
        calendar.set(Calendar.SECOND, 0);
        // Si ya pasó la hora de hoy, programa para mañana
        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        if (alarmManager != null) {
            // Alarma repetitiva diaria que despierta el dispositivo
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    // Crea canal de notificaciones requerido por Android 8+
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Underworld Notifications", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Avisos de bandas");
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) manager.createNotificationChannel(channel);
        }
    }

    // Solicita permiso de notificaciones solo en Android 13+
    private void checkAndRequestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQ_NOTIF);
        }
    }
}

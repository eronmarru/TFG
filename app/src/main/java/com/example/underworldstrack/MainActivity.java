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
import android.view.View;
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

/**
 * Actividad principal de la aplicación.
 * Gestiona el menú inicial, permisos, notificaciones, servicios en segundo plano (WiFi)
 * y alarmas periódicas.
 */
public class MainActivity extends AppCompatActivity {
    private static final int REQ_NOTIF = 1001; // Código de solicitud de permisos de notificación
    public static final String CHANNEL_ID = "underworld_channel"; // ID del canal de notificaciones
    private CountDownTimer contador; // Temporizador para mostrar mensajes periódicos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Inicia un contador que muestra un Toast cada 5 minutos
        iniciarContadorToast();

        // Configuración de notificaciones
        createNotificationChannel();
        checkAndRequestNotificationPermission();

        // Programar alarma diaria
        scheduleDailyGameAlarm();

        // Ajuste de insets para diseño EdgeToEdge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Botón para entrar al tablero principal
        Button btnEntrar = findViewById(R.id.btn_entrar);
        btnEntrar.setOnClickListener(v -> startActivity(new Intent(this, Board.class)));

        // Botón para abrir enlace externo
        Button btn_en_ex = findViewById(R.id.btn_en_ex);
        btn_en_ex.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://criticoyculo.com/"))));

        // Botón para iniciar el servicio WiFi
        Button btnStartWifi = findViewById(R.id.btn_start_wifi);
        btnStartWifi.setOnClickListener(v -> {
            Intent serviceIntent = new Intent(this, WifiService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(serviceIntent);
            } else {
                startService(serviceIntent);
            }
        });

        // Botón para detener el servicio WiFi
        Button btnStopWifi = findViewById(R.id.btn_stop_wifi);
        btnStopWifi.setOnClickListener(v -> stopService(new Intent(this, WifiService.class)));
    }

    /**
     * Mantiene la actividad en modo pantalla completa inmersiva.
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
    }

    /**
     * Inicia un temporizador que muestra un mensaje cada 5 minutos.
     * Útil para recordar al usuario que la app sigue activa o sugerir ayuda.
     */
    private void iniciarContadorToast() {
        if (contador != null) {
            contador.cancel();
        }

        contador = new CountDownTimer(300000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.d("Tiempo", "Ha pasado un segundo");
            }

            @Override
            public void onFinish() {
                Toast.makeText(MainActivity.this,
                        "Ya llevas 5 minutos, ¿encuentras lo que buscas?",
                        Toast.LENGTH_LONG).show();
                iniciarContadorToast();
            }
        };
        contador.start();
    }

    /**
     * Programa una alarma diaria a las 15:30.
     * Utiliza AlarmManager para lanzar AlarmReciver.
     */
    private void scheduleDailyGameAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReciver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 15);
        calendar.set(Calendar.MINUTE, 30);
        calendar.set(Calendar.SECOND, 0);
        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    /**
     * Crea el canal de notificaciones necesario para Android O y superiores.
     */
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Underworld Notifications", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Avisos de bandas");
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) manager.createNotificationChannel(channel);
        }
    }

    /**
     * Verifica y solicita permisos de notificación para Android 13 (Tiramisu) y superiores.
     */
    private void checkAndRequestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQ_NOTIF);
        }
    }
}

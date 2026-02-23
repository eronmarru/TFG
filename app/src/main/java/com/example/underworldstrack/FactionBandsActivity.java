package com.example.underworldstrack;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FactionBandsActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private RecyclerView recyclerView;
    private FactionBandAdapter adapter;
    public static final String CHANNEL_ID = "underworld_channel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faction_bands);

        String faction = getIntent().getStringExtra("FACTION");
        if (faction == null) {
            Toast.makeText(this, "Error: Facción no especificada", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        databaseHelper = new DatabaseHelper(this);
        List<Band> bands = null;
        try {
            bands = databaseHelper.getBandsByFaction(faction);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al cargar bandas: " + e.getMessage(), Toast.LENGTH_LONG).show();
            // Retornar o mostrar lista vacía para evitar crash
            bands = new java.util.ArrayList<>();
        }

        recyclerView = findViewById(R.id.recyclerFactionBands);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new FactionBandAdapter(this, bands, band -> {
            showNotification(band);
            openBandDetail(band);
        });

        recyclerView.setAdapter(adapter);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
    }

    private void showNotification(Band band) {
        String title = band.getName();
        String text = "Esta banda es de la facción " + band.getDisplayFaction();

        // Intent para abrir BandDetailActivity al pulsar la notificación
        Intent intent = new Intent(this, BandDetailActivity.class);
        intent.putExtra("BAND_ID", band.getId());
        intent.putExtra("BAND_NAME", band.getName());
        intent.putExtra("BAND_DESC", band.getDescription());
        intent.putExtra("BAND_FACTION", band.getDisplayFaction());
        intent.putExtra("BAND_IMAGE_RES_ID", band.getImageResId());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                band.getId(), // ID único para cada notificación
                intent,
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                        ? PendingIntent.FLAG_IMMUTABLE
                        : 0
        );

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.icon) // Asegúrate de que existe o usa ic_launcher
                        .setContentTitle(title)
                        .setContentText(text)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);

        NotificationManagerCompat manager = NotificationManagerCompat.from(this);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                manager.areNotificationsEnabled()) {
            try {
                manager.notify(band.getId(), builder.build());
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
    }

    private void openBandDetail(Band band) {
        Intent intent = new Intent(this, BandDetailActivity.class);
        intent.putExtra("BAND_ID", band.getId());
        intent.putExtra("BAND_NAME", band.getName());
        intent.putExtra("BAND_DESC", band.getDescription());
        intent.putExtra("BAND_FACTION", band.getDisplayFaction());
        intent.putExtra("BAND_IMAGE_RES_ID", band.getImageResId());
        startActivity(intent);
    }

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
}

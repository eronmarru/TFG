package com.example.underworldstrack;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Bands extends AppCompatActivity {
    public static final String CHANNEL_ID = "underworld_channel";  // ID del canal de notificaciones (compartido)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bands);  // Carga layout con botones de facción

        // Configurar botones de facción
        setupFactionButton(R.id.btn_caos, "Caos");
        setupFactionButton(R.id.btn_muerte, "Muerte");
        setupFactionButton(R.id.btn_destruccion, "Destruccion");
        setupFactionButton(R.id.btn_orden, "Orden");

        // Botón para volver a Board (pantalla del tablero)
        Button btnvolverBanda = findViewById(R.id.btn_volverBoard);
        btnvolverBanda.setOnClickListener(v -> {
            Intent volver_banda = new Intent(Bands.this, Board.class);
            startActivity(volver_banda);
        });

        // Botón para volver a MainActivity (pantalla principal)
        Button btnvolverinicio = findViewById(R.id.btn_volverPrincipal);
        btnvolverinicio.setOnClickListener(v -> {
            Intent volver_inicio = new Intent(Bands.this, MainActivity.class);
            startActivity(volver_inicio);
        });
    }

    private void setupFactionButton(int buttonId, String faction) {
        Button button = findViewById(buttonId);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(Bands.this, FactionBandsActivity.class);
            intent.putExtra("FACTION", faction);
            startActivity(intent);
        });
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

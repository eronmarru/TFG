package com.example.underworldstrack;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * Activity principal del tablero de juego.
 * Sirve como menú central para acceder a diferentes funcionalidades de la aplicación,
 * como el constructor de mazos, mis mazos, tirador de dados, etc.
 * También permite rotar elementos visuales del tablero.
 */
public class Board extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board);

        // Configuración de botones de navegación

        // Botón para volver a la actividad principal (MainActivity)
        Button btnVolver = findViewById(R.id.btn_volver);
        btnVolver.setOnClickListener(v -> {
            Intent ir_campo = new Intent(Board.this, MainActivity.class);
            startActivity(ir_campo);
        });

        // Botón para ir a la selección de bandas
        Button btnirBanda = findViewById(R.id.btn_irBanda);
        btnirBanda.setOnClickListener(v -> {
            Intent ir_banda = new Intent(Board.this, Bands.class);
            startActivity(ir_banda);
        });

        // Botón para ir al constructor de mazos
        Button btnDeckBuilder = findViewById(R.id.btn_deck_builder);
        btnDeckBuilder.setOnClickListener(v -> {
            Intent intent = new Intent(Board.this, DeckBuilderActivity.class);
            startActivity(intent);
        });

        // Botón para ver los mazos del usuario
        Button btnMyDecks = findViewById(R.id.btn_my_decks);
        btnMyDecks.setOnClickListener(v -> {
            Intent intent = new Intent(Board.this, UserDecksActivity.class);
            startActivity(intent);
        });

        // Botón para ir al tirador de dados
        Button btnDiceRoller = findViewById(R.id.btn_dice_roller);
        btnDiceRoller.setOnClickListener(v -> {
            Intent intent = new Intent(Board.this, DiceRollerActivity.class);
            startActivity(intent);
        });

        // Botón para ver los mazos rivales
        Button btnRivalDecks = findViewById(R.id.btn_rival_decks);
        btnRivalDecks.setOnClickListener(v -> {
            Intent intent = new Intent(Board.this, RivalDecksListActivity.class);
            startActivity(intent);
        });

        // Botón para ir a la sección de videos
        Button btnVideos = findViewById(R.id.btn_videos);
        btnVideos.setOnClickListener(v -> {
            Intent intent = new Intent(Board.this, VideosActivity.class);
            startActivity(intent);
        });

        // Configuración de rotación de elementos del tablero
        // Permite rotar las imágenes del tablero 90 grados al hacer clic

        ImageView rotar1 = findViewById(R.id.eBoard1);
        rotar1.setOnClickListener(v -> {
            float current = rotar1.getRotation();
            rotar1.setRotation(current+90f);
        });
        
        ImageView rotar2 = findViewById(R.id.eBoard2);
        rotar2.setOnClickListener(v -> {
            float current = rotar2.getRotation();
            rotar2.setRotation(current+90f);
        });
        
        ImageView rotar3 = findViewById(R.id.sBoard1);
        rotar3.setOnClickListener(v -> {
            float current = rotar3.getRotation();
            rotar3.setRotation(current+90f);
        });
        
        ImageView rotar4 = findViewById(R.id.sBoard2);
        rotar4.setOnClickListener(v -> {
            float current = rotar4.getRotation();
            rotar4.setRotation(current+90f);
        });
    }

    /**
     * Asegura que la aplicación se mantenga en modo pantalla completa (inmersivo).
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
}

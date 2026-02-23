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

public class Board extends AppCompatActivity {
    
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board);  // Carga el layout del tablero de juego

        /*Este codigo lo que hace es crear un vinculo a la pagina principal,
        mediante un boton.
        */
        Button btnVolver = findViewById(R.id.btn_volver);
        btnVolver.setOnClickListener(v -> {
            Intent ir_campo = new Intent(Board.this, MainActivity.class);  // Intent para volver a MainActivity
            startActivity(ir_campo);                                        // Lanza MainActivity
        });

        Button btnirBanda = findViewById(R.id.btn_irBanda);
        btnirBanda.setOnClickListener(v -> {
            Intent ir_banda = new Intent(Board.this, Bands.class);  // Intent para ir a pantalla de bandas
            startActivity(ir_banda);                                // Lanza Bands activity
        });

        Button btnDeckBuilder = findViewById(R.id.btn_deck_builder);
        btnDeckBuilder.setOnClickListener(v -> {
            Intent intent = new Intent(Board.this, DeckBuilderActivity.class);
            startActivity(intent);
        });

        Button btnMyDecks = findViewById(R.id.btn_my_decks);
        btnMyDecks.setOnClickListener(v -> {
            Intent intent = new Intent(Board.this, UserDecksActivity.class);
            startActivity(intent);
        });

        Button btnDiceRoller = findViewById(R.id.btn_dice_roller);
        btnDiceRoller.setOnClickListener(v -> {
            Intent intent = new Intent(Board.this, DiceRollerActivity.class);
            startActivity(intent);
        });

        Button btnRivalDecks = findViewById(R.id.btn_rival_decks);
        btnRivalDecks.setOnClickListener(v -> {
            Intent intent = new Intent(Board.this, RivalDecksListActivity.class);
            startActivity(intent);
        });

        Button btnVideos = findViewById(R.id.btn_videos);
        btnVideos.setOnClickListener(v -> {
            Intent intent = new Intent(Board.this, VideosActivity.class);
            startActivity(intent);
        });

        /*Los siguientes 4 eventos hacen lo mismo pero en imagenes diferentes
        al pulsar en una de la imagenes se recoge en una variable la rotacion
        que tiene y se coloca a 90 grados mas.
        */
        ImageView rotar1 = findViewById(R.id.eBoard1);  // Primera imagen del tablero
        rotar1.setOnClickListener(v -> {
            float current = rotar1.getRotation();        // Guarda rotación actual
            rotar1.setRotation(current+90f);             // Suma 90 grados
        });
        
        ImageView rotar2 = findViewById(R.id.eBoard2);  // Segunda imagen
        rotar2.setOnClickListener(v -> {
            float current = rotar2.getRotation();
            rotar2.setRotation(current+90f);
        });
        
        ImageView rotar3 = findViewById(R.id.sBoard1);  // Tercera imagen
        rotar3.setOnClickListener(v -> {
            float current = rotar3.getRotation();
            rotar3.setRotation(current+90f);
        });
        
        ImageView rotar4 = findViewById(R.id.sBoard2);  // Cuarta imagen
        rotar4.setOnClickListener(v -> {
            float current = rotar4.getRotation();
            rotar4.setRotation(current+90f);
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

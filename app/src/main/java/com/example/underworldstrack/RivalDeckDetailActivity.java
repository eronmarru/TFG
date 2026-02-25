package com.example.underworldstrack;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Activity que muestra los detalles de un Mazo Rival preconstruido.
 * Muestra la lista de cartas incluidas en el mazo.
 */
public class RivalDeckDetailActivity extends AppCompatActivity {

    private RecyclerView rvCards;
    private TextView tvDeckTitle;
    private Button btnBack;
    private DatabaseHelper dbHelper;
    private int deckId;
    private String deckName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rival_deck_detail);

        // Obtener datos del Intent
        deckId = getIntent().getIntExtra("DECK_ID", -1);
        deckName = getIntent().getStringExtra("DECK_NAME");

        rvCards = findViewById(R.id.rvCards);
        tvDeckTitle = findViewById(R.id.tvDeckTitle);
        btnBack = findViewById(R.id.btnBack);

        tvDeckTitle.setText(deckName != null ? deckName : "Detalle del Mazo");

        rvCards.setLayoutManager(new LinearLayoutManager(this));
        dbHelper = new DatabaseHelper(this);

        if (deckId != -1) {
            loadCards();
        }

        btnBack.setOnClickListener(v -> finish());
    }

    /**
     * Carga las cartas del mazo seleccionado y las muestra en la lista.
     * Utiliza CardAdapter en modo solo lectura.
     */
    private void loadCards() {
        // En este contexto, getCardsForDecks se usa para obtener cartas de un solo mazo
        List<Card> cards = dbHelper.getCardsForDecks(deckId, deckId);
        
        Map<Integer, String> deckNames = new HashMap<>();
        deckNames.put(deckId, deckName);

        CardAdapter adapter = new CardAdapter(cards, deckNames);
        adapter.setReadOnly(true); 
        rvCards.setAdapter(adapter);
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

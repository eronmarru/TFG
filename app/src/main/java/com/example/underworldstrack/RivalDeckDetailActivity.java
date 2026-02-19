package com.example.underworldstrack;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private void loadCards() {
        // We reuse getCardsForDecks but pass the same ID twice to get cards for just this deck
        List<Card> cards = dbHelper.getCardsForDecks(deckId, deckId);
        
        Map<Integer, String> deckNames = new HashMap<>();
        deckNames.put(deckId, deckName);

        CardAdapter adapter = new CardAdapter(cards, deckNames);
        adapter.setReadOnly(true); // User can only view, not select
        rvCards.setAdapter(adapter);
    }
}

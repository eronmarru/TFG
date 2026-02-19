package com.example.underworldstrack;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class RivalDecksListActivity extends AppCompatActivity {

    private RecyclerView rvRivalDecks;
    private Button btnBack;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rival_decks_list);

        rvRivalDecks = findViewById(R.id.rvRivalDecks);
        btnBack = findViewById(R.id.btnBack);
        
        rvRivalDecks.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DatabaseHelper(this);
        loadRivalDecks();

        btnBack.setOnClickListener(v -> finish());
    }

    private void loadRivalDecks() {
        List<RivalDeck> decks = dbHelper.getAllRivalDecks();
        RivalDeckAdapter adapter = new RivalDeckAdapter(decks, deck -> {
            Intent intent = new Intent(RivalDecksListActivity.this, RivalDeckDetailActivity.class);
            intent.putExtra("DECK_ID", deck.getId());
            intent.putExtra("DECK_NAME", deck.getName());
            startActivity(intent);
        });
        rvRivalDecks.setAdapter(adapter);
    }
}

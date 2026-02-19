package com.example.underworldstrack;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import android.widget.Button;
import android.widget.Toast;
import android.app.AlertDialog;

public class UserDeckDetailActivity extends AppCompatActivity {

    private RecyclerView rvCards;
    private TextView tvDeckName;
    private Button btnBack, btnDeleteDeck;
    private DatabaseHelper dbHelper;
    private int userDeckId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_deck_detail);

        tvDeckName = findViewById(R.id.tvDeckName);
        rvCards = findViewById(R.id.rvCards);
        btnBack = findViewById(R.id.btnBack);
        btnDeleteDeck = findViewById(R.id.btnDeleteDeck);

        rvCards.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DatabaseHelper(this);

        if (getIntent().hasExtra("USER_DECK_ID")) {
            userDeckId = getIntent().getIntExtra("USER_DECK_ID", -1);
            String deckName = getIntent().getStringExtra("USER_DECK_NAME");
            tvDeckName.setText(deckName);
            loadCards();
        }

        btnBack.setOnClickListener(v -> finish());

        btnDeleteDeck.setOnClickListener(v -> confirmDeleteDeck());
    }

    private void confirmDeleteDeck() {
        new AlertDialog.Builder(this)
            .setTitle("Eliminar Mazo")
            .setMessage("¿Estás seguro de que quieres eliminar este mazo?")
            .setPositiveButton("Sí", (dialog, which) -> {
                if (dbHelper.deleteUserDeck(userDeckId)) {
                    Toast.makeText(this, "Mazo eliminado correctamente", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Error al eliminar el mazo", Toast.LENGTH_SHORT).show();
                }
            })
            .setNegativeButton("No", null)
            .show();
    }

    private void loadCards() {
        List<Card> cards = dbHelper.getCardsForUserDeck(userDeckId);
        CardAdapter adapter = new CardAdapter(cards, null);
        adapter.setReadOnly(true);
        rvCards.setAdapter(adapter);
    }
}
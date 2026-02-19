package com.example.underworldstrack;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.EditText;
import android.text.InputType;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.ArrayList;

public class DeckBuilderActivity extends AppCompatActivity {

    private Spinner spinnerDeck1;
    private Spinner spinnerDeck2;
    private Button btnMixDecks;
    private Button btnSaveDeck;
    private Button btnBack;
    private RecyclerView rvCards;
    private DatabaseHelper dbHelper;
    private List<RivalDeck> rivalDecks;
    private CardAdapter cardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deck_builder);

        spinnerDeck1 = findViewById(R.id.spinnerDeck1);
        spinnerDeck2 = findViewById(R.id.spinnerDeck2);
        btnMixDecks = findViewById(R.id.btnMixDecks);
        btnSaveDeck = findViewById(R.id.btnSaveDeck);
        btnBack = findViewById(R.id.btnBack);
        rvCards = findViewById(R.id.rvCards);
        rvCards.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DatabaseHelper(this);
        loadRivalDecks();

        btnMixDecks.setOnClickListener(v -> mixDecks());
        btnSaveDeck.setOnClickListener(v -> saveDeck());
        btnBack.setOnClickListener(v -> finish());
    }

    private void loadRivalDecks() {
        rivalDecks = dbHelper.getAllRivalDecks();
        List<String> deckNames = new ArrayList<>();
        for (RivalDeck deck : rivalDecks) {
            deckNames.add(deck.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, deckNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        
        spinnerDeck1.setAdapter(adapter);
        spinnerDeck2.setAdapter(adapter);
    }

    private void mixDecks() {
        int index1 = spinnerDeck1.getSelectedItemPosition();
        int index2 = spinnerDeck2.getSelectedItemPosition();

        if (index1 >= 0 && index2 >= 0) {
            RivalDeck deck1 = rivalDecks.get(index1);
            RivalDeck deck2 = rivalDecks.get(index2);
            
            List<Card> cards = dbHelper.getCardsForDecks(deck1.getId(), deck2.getId());
            
            java.util.Map<Integer, String> deckNames = new java.util.HashMap<>();
            deckNames.put(deck1.getId(), deck1.getName());
            deckNames.put(deck2.getId(), deck2.getName());
            
            cardAdapter = new CardAdapter(cards, deckNames);
            rvCards.setAdapter(cardAdapter);
        }
    }

    private void saveDeck() {
        if (cardAdapter == null) {
            Toast.makeText(this, "Primero mezcla los mazos", Toast.LENGTH_SHORT).show();
            return;
        }

        List<Integer> selectedCardIds = cardAdapter.getSelectedCardIds();
        if (selectedCardIds.isEmpty()) {
            Toast.makeText(this, "Selecciona al menos una carta", Toast.LENGTH_SHORT).show();
            return;
        }

        // Dialog para nombre del mazo
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Nombre del Mazo");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String deckName = input.getText().toString();
            if (!deckName.isEmpty()) {
                int index1 = spinnerDeck1.getSelectedItemPosition();
                int index2 = spinnerDeck2.getSelectedItemPosition();
                RivalDeck deck1 = rivalDecks.get(index1);
                RivalDeck deck2 = rivalDecks.get(index2);

                long result = dbHelper.saveUserDeck(deckName, deck1.getId(), deck2.getId(), selectedCardIds);
                if (result != -1) {
                    Toast.makeText(DeckBuilderActivity.this, "Mazo guardado correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DeckBuilderActivity.this, "Error al guardar el mazo", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(DeckBuilderActivity.this, "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());

        builder.show();
    }
}

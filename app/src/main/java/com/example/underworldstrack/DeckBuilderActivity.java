package com.example.underworldstrack;

import android.os.Bundle;
import android.view.View;
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

/**
 * Activity para la construcción de mazos personalizados (Nemesis).
 * Permite seleccionar dos mazos rivales, combinarlos, seleccionar cartas y guardar el mazo resultante.
 */
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

        // Inicialización de componentes
        spinnerDeck1 = findViewById(R.id.spinnerDeck1);
        spinnerDeck2 = findViewById(R.id.spinnerDeck2);
        btnMixDecks = findViewById(R.id.btnMixDecks);
        btnSaveDeck = findViewById(R.id.btnSaveDeck);
        btnBack = findViewById(R.id.btnBack);
        rvCards = findViewById(R.id.rvCards);
        rvCards.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DatabaseHelper(this);
        loadRivalDecks();

        // Configuración de listeners
        btnMixDecks.setOnClickListener(v -> mixDecks());
        btnSaveDeck.setOnClickListener(v -> saveDeck());
        btnBack.setOnClickListener(v -> finish());
    }

    /**
     * Carga los mazos rivales disponibles en los Spinners.
     */
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

    /**
     * Combina las cartas de los dos mazos seleccionados y las muestra en la lista.
     */
    private void mixDecks() {
        int index1 = spinnerDeck1.getSelectedItemPosition();
        int index2 = spinnerDeck2.getSelectedItemPosition();

        if (index1 >= 0 && index2 >= 0) {
            RivalDeck deck1 = rivalDecks.get(index1);
            RivalDeck deck2 = rivalDecks.get(index2);
            
            // Obtener cartas combinadas de ambos mazos
            List<Card> cards = dbHelper.getCardsForDecks(deck1.getId(), deck2.getId());
            
            // Mapa para mostrar a qué mazo pertenece cada carta
            java.util.Map<Integer, String> deckNames = new java.util.HashMap<>();
            deckNames.put(deck1.getId(), deck1.getName());
            deckNames.put(deck2.getId(), deck2.getName());
            
            cardAdapter = new CardAdapter(cards, deckNames);
            rvCards.setAdapter(cardAdapter);
        }
    }

    /**
     * Guarda el mazo personalizado creado por el usuario.
     * Solicita un nombre para el mazo y valida que se hayan seleccionado cartas.
     */
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

        // Diálogo para ingresar el nombre del mazo
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

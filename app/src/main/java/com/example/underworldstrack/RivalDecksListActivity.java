package com.example.underworldstrack;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

/**
 * Activity que muestra la lista de todos los mazos rivales (preconstruidos) disponibles.
 */
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

    /**
     * Carga la lista de mazos rivales y configura el adaptador.
     * Al hacer clic en un mazo, navega a sus detalles.
     */
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

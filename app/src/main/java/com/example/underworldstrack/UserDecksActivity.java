package com.example.underworldstrack;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import android.widget.Button;

public class UserDecksActivity extends AppCompatActivity {

    private RecyclerView rvUserDecks;
    private Button btnBack;
    private UserDeckAdapter adapter;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_decks);

        rvUserDecks = findViewById(R.id.rvUserDecks);
        btnBack = findViewById(R.id.btnBack);
        
        rvUserDecks.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DatabaseHelper(this);
        loadUserDecks();

        btnBack.setOnClickListener(v -> finish());
    }

    private void loadUserDecks() {
        List<UserDeck> userDecks = dbHelper.getUserDecks();
        adapter = new UserDeckAdapter(this, userDecks);
        rvUserDecks.setAdapter(adapter);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        loadUserDecks();
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

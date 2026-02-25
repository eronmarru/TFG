package com.example.underworldstrack;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity que muestra los detalles de una banda seleccionada.
 * Muestra información como nombre, descripción e imágenes de los luchadores.
 * Permite alternar entre la versión normal e inspirada de los luchadores.
 */
public class BandDetailActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private RecyclerView recyclerFighters;
    private TextView tvBandName, tvDescription;
    private ImageView ivBandImage;
    private FighterImageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_band_detail);

        // Obtener datos pasados por Intent
        int bandId = getIntent().getIntExtra("BAND_ID", -1);
        String bandName = getIntent().getStringExtra("BAND_NAME");
        String bandDesc = getIntent().getStringExtra("BAND_DESC");
        int bandImageResId = getIntent().getIntExtra("BAND_IMAGE_RES_ID", 0);

        if (bandId == -1) {
            Toast.makeText(this, "Error: Banda no válida", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Inicializar vistas
        tvBandName = findViewById(R.id.tv_detail_band_name);
        tvDescription = findViewById(R.id.tv_detail_description);
        recyclerFighters = findViewById(R.id.recycler_fighters);
        ivBandImage = findViewById(R.id.iv_detail_band_image);

        tvBandName.setText(bandName);
        tvDescription.setText(bandDesc);

        if (bandImageResId != 0) {
            ivBandImage.setImageResource(bandImageResId);
            ivBandImage.setOnClickListener(v -> showExpandedImage(bandImageResId));
        }

        // Cargar imágenes de luchadores desde la base de datos
        databaseHelper = new DatabaseHelper(this);
        List<Integer> fighterImages = databaseHelper.getFighterImagesForBand(bandId);

        // Procesar imágenes para agrupar versión normal e inspirada
        List<Fighter> fighters = new ArrayList<>();
        for (Integer imgId : fighterImages) {
            try {
                String entryName = getResources().getResourceEntryName(imgId);
                // Si es una imagen inspirada, se salta porque se asociará con la base
                if (entryName.endsWith("_inspired")) {
                    continue;
                }

                // Buscar si existe versión inspirada
                int inspiredId = 0;
                String inspiredName = entryName + "_inspired";
                int id = getResources().getIdentifier(inspiredName, "drawable", getPackageName());
                if (id != 0) {
                    inspiredId = id;
                }

                fighters.add(new Fighter(imgId, inspiredId));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Configurar RecyclerView
        adapter = new FighterImageAdapter(fighters);
        recyclerFighters.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerFighters.setAdapter(adapter);

        // Configurar botones de navegación
        Button btnBack = findViewById(R.id.btn_back_detail);
        Button btnFactions = findViewById(R.id.btn_factions_detail);
        Button btnHome = findViewById(R.id.btn_home_detail);

        btnBack.setOnClickListener(v -> finish());

        btnFactions.setOnClickListener(v -> {
            Intent intent = new Intent(BandDetailActivity.this, Bands.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(BandDetailActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
    }

    /**
     * Muestra la imagen de la banda expandida en pantalla completa.
     */
    private void showExpandedImage(int imageResId) {
        Dialog dialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);

        ImageView fullImageView = new ImageView(this);
        fullImageView.setImageResource(imageResId);
        fullImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        fullImageView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        fullImageView.setOnClickListener(v -> dialog.dismiss());

        dialog.setContentView(fullImageView);
        dialog.show();
    }

    /**
     * Clase interna para representar un luchador con sus dos estados (normal e inspirado).
     */
    private static class Fighter {
        int baseImageId;
        int inspiredImageId;
        boolean isInspiredShowing;

        Fighter(int base, int inspired) {
            this.baseImageId = base;
            this.inspiredImageId = inspired;
            this.isInspiredShowing = false;
        }
    }

    /**
     * Adaptador para mostrar las imágenes de los luchadores en el RecyclerView.
     */
    private class FighterImageAdapter extends RecyclerView.Adapter<FighterImageAdapter.ViewHolder> {
        private List<Fighter> fighters;

        public FighterImageAdapter(List<Fighter> fighters) {
            this.fighters = fighters;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fighter_image, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Fighter fighter = fighters.get(position);

            // Mostrar imagen según el estado actual (inspirado o normal)
            if (fighter.isInspiredShowing && fighter.inspiredImageId != 0) {
                holder.imageView.setImageResource(fighter.inspiredImageId);
            } else {
                holder.imageView.setImageResource(fighter.baseImageId);
            }

            // Alternar estado al hacer clic
            holder.imageView.setOnClickListener(v -> {
                if (fighter.inspiredImageId != 0) {
                    fighter.isInspiredShowing = !fighter.isInspiredShowing;
                    notifyItemChanged(position);
                } else {
                    Toast.makeText(BandDetailActivity.this, "No hay versión inspirada disponible", Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return fighters.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.iv_fighter_card);
            }
        }
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

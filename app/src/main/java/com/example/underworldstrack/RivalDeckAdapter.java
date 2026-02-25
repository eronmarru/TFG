package com.example.underworldstrack;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

/**
 * Adaptador para mostrar la lista de Mazos Rivales.
 * Maneja la carga dinámica de iconos de mazos basados en el nombre.
 */
public class RivalDeckAdapter extends RecyclerView.Adapter<RivalDeckAdapter.RivalDeckViewHolder> {

    private List<RivalDeck> rivalDecks;
    private OnItemClickListener listener;

    /**
     * Interfaz para callbacks de clic en un mazo rival.
     */
    public interface OnItemClickListener {
        void onItemClick(RivalDeck deck);
    }

    public RivalDeckAdapter(List<RivalDeck> rivalDecks, OnItemClickListener listener) {
        this.rivalDecks = rivalDecks;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RivalDeckViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rival_deck, parent, false);
        return new RivalDeckViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RivalDeckViewHolder holder, int position) {
        RivalDeck deck = rivalDecks.get(position);
        holder.tvName.setText(deck.getName());
        holder.tvType.setText(deck.getType());
        holder.tvDescription.setText(deck.getDescription());

        // Lógica para determinar el nombre del recurso de icono basado en el nombre del mazo
        String originalName = deck.getName().toLowerCase().trim();
        
        // Limpieza de sufijos comunes
        if (originalName.endsWith(" rivals deck")) {
            originalName = originalName.substring(0, originalName.length() - 12);
        } else if (originalName.endsWith("rivals deck")) {
             originalName = originalName.substring(0, originalName.length() - 11);
        }
        
        // Formateo para nombre de recurso válido (snake_case)
        String deckName = originalName.trim().replaceAll("\\s+", "_").replace("-", "_");
        
        String resourceName = deckName + "_icon";

        // Intentar cargar icono específico con sufijo _icon
        int resId = holder.itemView.getContext().getResources().getIdentifier(
                resourceName, "drawable", holder.itemView.getContext().getPackageName());

        if (resId != 0) {
            holder.ivIcon.setImageResource(resId);
            holder.ivIcon.setBackground(null);
        } else {
             // Si falla, intentar cargar por nombre directo
             int resIdNoIcon = holder.itemView.getContext().getResources().getIdentifier(
                deckName, "drawable", holder.itemView.getContext().getPackageName());
             
             if (resIdNoIcon != 0) {
                 holder.ivIcon.setImageResource(resIdNoIcon);
                 holder.ivIcon.setBackground(null);
             } else {
                 // Si no se encuentra imagen, usar icono por defecto
                 holder.ivIcon.setImageResource(android.R.drawable.ic_menu_help);
             }
        }

        holder.itemView.setOnClickListener(v -> listener.onItemClick(deck));
    }

    @Override
    public int getItemCount() {
        return rivalDecks.size();
    }

    static class RivalDeckViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvType, tvDescription;
        ImageView ivIcon;

        public RivalDeckViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvRivalDeckName);
            tvType = itemView.findViewById(R.id.tvRivalDeckType);
            tvDescription = itemView.findViewById(R.id.tvRivalDeckDescription);
            ivIcon = itemView.findViewById(R.id.ivRivalDeckIcon);
        }
    }
}

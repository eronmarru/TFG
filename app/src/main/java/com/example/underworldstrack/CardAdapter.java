package com.example.underworldstrack;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.app.Dialog;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Adaptador para mostrar una lista de Cartas en un RecyclerView.
 * Permite seleccionar cartas (checkbox) y ver detalles expandidos.
 * Soporta modo de solo lectura.
 */
public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    private List<Card> cardList; // Lista de cartas a mostrar
    private Set<Integer> selectedCards = new HashSet<>(); // Conjunto de IDs de cartas seleccionadas
    private java.util.Map<Integer, String> deckNames; // Mapa de IDs de mazo a nombres
    private boolean isReadOnly = false; // Flag para modo solo lectura (sin selección)

    /**
     * Constructor del adaptador.
     * @param cardList Lista de cartas
     * @param deckNames Mapa de nombres de mazos
     */
    public CardAdapter(List<Card> cardList, java.util.Map<Integer, String> deckNames) {
        this.cardList = cardList;
        this.deckNames = deckNames;
    }

    /**
     * Establece el modo de solo lectura.
     * @param readOnly true para ocultar checkboxes de selección
     */
    public void setReadOnly(boolean readOnly) {
        this.isReadOnly = readOnly;
        notifyDataSetChanged();
    }

    /**
     * Obtiene la lista de IDs de las cartas seleccionadas.
     * @return Lista de IDs
     */
    public List<Integer> getSelectedCardIds() {
        return new ArrayList<>(selectedCards);
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        Card card = cardList.get(position);
        holder.tvName.setText(card.getName());
        holder.tvType.setText(card.getType());
        
        // Mostrar detalle según tipo de carta (Gloria para objetivos, Coste/Restricción para otros)
        String detail = "";
        if ("Objective".equals(card.getType())) {
            detail = "Glory: " + card.getGlory();
        } else {
            detail = card.getCostRestriction() != null ? card.getCostRestriction() : "";
        }
        holder.tvDetail.setText(detail);
        
        // Mostrar nombre del mazo si está disponible
        String deckName = deckNames != null ? deckNames.get(card.getRivalDeckId()) : null;
        if (deckName != null) {
            holder.tvDeckName.setText(deckName);
            holder.tvDeckName.setVisibility(View.VISIBLE);
        } else {
            holder.tvDeckName.setVisibility(View.GONE);
        }

        // Mostrar efecto de la carta
        String effect = card.getEffect();
        if (effect != null && !effect.isEmpty()) {
            holder.tvCardEffect.setText(effect);
            holder.tvCardEffect.setVisibility(View.VISIBLE);
        } else {
            holder.tvCardEffect.setVisibility(View.GONE);
        }

        // Listener para mostrar la imagen de la carta en un diálogo
        holder.itemView.setOnClickListener(v -> {
            showCardImageDialog(v.getContext(), card);
        });

        // Configurar checkbox de selección
        if (isReadOnly) {
            holder.cbSelect.setVisibility(View.GONE);
        } else {
            holder.cbSelect.setVisibility(View.VISIBLE);
            
            // Evitar disparar listener al configurar estado
            holder.cbSelect.setOnCheckedChangeListener(null);
            holder.cbSelect.setChecked(selectedCards.contains(card.getId()));
            
            holder.cbSelect.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    selectedCards.add(card.getId());
                } else {
                    selectedCards.remove(card.getId());
                }
            });
        }
    }

    /**
     * Muestra un diálogo con la imagen ampliada de la carta.
     */
    private void showCardImageDialog(android.content.Context context, Card card) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_card_image);
        
        ImageView ivCardImage = dialog.findViewById(R.id.ivCardImage);
        
        String imageName = card.getImage();
        if (imageName != null && !imageName.isEmpty()) {
            int resId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
            if (resId != 0) {
                ivCardImage.setImageResource(resId);
            } else {
                ivCardImage.setImageResource(R.mipmap.ic_launcher);
            }
        }
        
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    static class CardViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvType, tvDetail, tvDeckName, tvCardEffect;
        CheckBox cbSelect;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvCardName);
            tvType = itemView.findViewById(R.id.tvCardType);
            tvDetail = itemView.findViewById(R.id.tvCardDetail);
            tvDeckName = itemView.findViewById(R.id.tvDeckName);
            tvCardEffect = itemView.findViewById(R.id.tvCardEffect);
            cbSelect = itemView.findViewById(R.id.cbSelectCard);
        }
    }
}

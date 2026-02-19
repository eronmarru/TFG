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

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    private List<Card> cardList;
    private Set<Integer> selectedCards = new HashSet<>();
    private java.util.Map<Integer, String> deckNames;
    private boolean isReadOnly = false;

    public CardAdapter(List<Card> cardList, java.util.Map<Integer, String> deckNames) {
        this.cardList = cardList;
        this.deckNames = deckNames;
    }

    public void setReadOnly(boolean readOnly) {
        this.isReadOnly = readOnly;
        notifyDataSetChanged();
    }

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
        
        String detail = "";
        if ("Objective".equals(card.getType())) {
            detail = "Glory: " + card.getGlory();
        } else {
            detail = card.getCostRestriction() != null ? card.getCostRestriction() : "";
        }
        holder.tvDetail.setText(detail);
        
        // Show deck name
        String deckName = deckNames != null ? deckNames.get(card.getRivalDeckId()) : null;
        if (deckName != null) {
            holder.tvDeckName.setText(deckName);
            holder.tvDeckName.setVisibility(View.VISIBLE);
        } else {
            holder.tvDeckName.setVisibility(View.GONE);
        }

        // Show card effect
        String effect = card.getEffect();
        if (effect != null && !effect.isEmpty()) {
            holder.tvCardEffect.setText(effect);
            holder.tvCardEffect.setVisibility(View.VISIBLE);
        } else {
            holder.tvCardEffect.setVisibility(View.GONE);
        }

        // Show card image on item click (except checkbox)
        holder.itemView.setOnClickListener(v -> {
            showCardImageDialog(v.getContext(), card);
        });

        if (isReadOnly) {
            holder.cbSelect.setVisibility(View.GONE);
        } else {
            holder.cbSelect.setVisibility(View.VISIBLE);
            
            // Manage selection
            holder.cbSelect.setOnCheckedChangeListener(null); // Remove listener to avoid recycling issues
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

    private void showCardImageDialog(android.content.Context context, Card card) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_card_image);
        
        ImageView ivCardImage = dialog.findViewById(R.id.ivCardImage);
        
        // Get resource ID from string
        String imageName = card.getImage();
        if (imageName != null && !imageName.isEmpty()) {
            int resId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
            if (resId != 0) {
                ivCardImage.setImageResource(resId);
            } else {
                ivCardImage.setImageResource(R.mipmap.ic_launcher); // Placeholder or default image
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

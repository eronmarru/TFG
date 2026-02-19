package com.example.underworldstrack;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FactionBandAdapter extends RecyclerView.Adapter<FactionBandAdapter.ViewHolder> {

    private List<Band> bandList;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Band band);
    }

    public FactionBandAdapter(Context context, List<Band> bandList, OnItemClickListener listener) {
        this.context = context;
        this.bandList = bandList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_faction_band, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Band band = bandList.get(position);
        holder.tvName.setText(band.getName());
        holder.tvInspiration.setText(band.getDescription()); // Muestra la condición de inspiración (primeras líneas)
        
        // Usar imagen del líder
        int imageResId = band.getLeaderImageResId();
        if (imageResId == 0) {
            imageResId = band.getImageResId(); // Fallback
        }
        holder.ivLeader.setImageResource(imageResId);

        holder.itemView.setOnClickListener(v -> listener.onItemClick(band));
    }

    @Override
    public int getItemCount() {
        return bandList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivLeader;
        TextView tvName;
        TextView tvInspiration;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivLeader = itemView.findViewById(R.id.iv_leader);
            tvName = itemView.findViewById(R.id.tv_band_name);
            tvInspiration = itemView.findViewById(R.id.tv_inspiration);
        }
    }
}
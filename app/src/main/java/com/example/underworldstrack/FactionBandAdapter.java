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

/**
 * Adaptador para mostrar las bandas de una facción específica.
 * Incluye un listener para manejar clics en los elementos.
 */
public class FactionBandAdapter extends RecyclerView.Adapter<FactionBandAdapter.ViewHolder> {

    private List<Band> bandList; // Lista de bandas
    private Context context;
    private OnItemClickListener listener; // Interfaz de callback para clics

    /**
     * Interfaz para manejar eventos de clic en los items de la lista.
     */
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
        holder.tvInspiration.setText(band.getDescription());
        
        // Usar imagen del líder si está disponible, sino la imagen genérica de la banda
        int imageResId = band.getLeaderImageResId();
        if (imageResId == 0) {
            imageResId = band.getImageResId();
        }
        holder.ivLeader.setImageResource(imageResId);

        // Configurar click listener
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

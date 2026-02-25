package com.example.underworldstrack;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * Adaptador para mostrar una lista de Bandas en un RecyclerView.
 * Gestiona la visualización de cada elemento de banda y sus interacciones.
 */
public class BandAdapter extends RecyclerView.Adapter<BandAdapter.ViewHolder> {

    private List<Band> bands; // Lista de datos de bandas
    private Context context; // Contexto de la aplicación

    /**
     * Constructor del adaptador.
     * @param context Contexto
     * @param bands Lista de bandas a mostrar
     */
    public BandAdapter(Context context, List<Band> bands) {
        this.context = context;
        this.bands = bands;
    }

    /**
     * Clase ViewHolder que mantiene las referencias a las vistas de cada elemento de la lista.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgBand;
        TextView txtBandName, txtBandDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgBand = itemView.findViewById(R.id.imgBand);
            txtBandName = itemView.findViewById(R.id.txtBandName);
            txtBandDescription = itemView.findViewById(R.id.txtBandDescription);
        }
    }

    /**
     * Crea nuevas vistas (invocado por el layout manager).
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_band, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Reemplaza el contenido de una vista (invocado por el layout manager).
     * Configura los datos de la banda en la posición dada y maneja el clic en la imagen.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Band band = bands.get(position);

        holder.imgBand.setImageResource(band.getImageResId());
        holder.txtBandName.setText(band.getName());
        holder.txtBandDescription.setText(band.getDescription());

        // Configurar listener para mostrar una notificación al hacer clic en la imagen de la banda
        holder.imgBand.setOnClickListener(v -> {
            String title = band.getName();
            String text = "Esta banda es de la facción " + band.getDisplayFaction();

            Intent intent = new Intent(context, Bands.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            PendingIntent pendingIntent = PendingIntent.getActivity(
                    context,
                    0,
                    intent,
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                            ? PendingIntent.FLAG_IMMUTABLE
                            : 0
            );

            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(context, Bands.CHANNEL_ID)
                            .setSmallIcon(R.drawable.icon)
                            .setContentTitle(title)
                            .setContentText(text)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setContentIntent(pendingIntent)
                            .setAutoCancel(true);

            NotificationManagerCompat manager = NotificationManagerCompat.from(context);
            int notificationId = position + 1;

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                    NotificationManagerCompat.from(context).areNotificationsEnabled()) {
                manager.notify(notificationId, builder.build());
            }
        });
    }

    /**
     * Devuelve el tamaño de la lista de datos.
     */
    @Override
    public int getItemCount() {
        return bands.size();
    }
}
